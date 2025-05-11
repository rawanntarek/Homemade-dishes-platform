package com.example.customerservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class OrderPublisher {
private static final String EXCHANGE_NAME = "orders";
private static final String PAYMENT_EXCHANGE = "payment_responses";

public static void placeOrder(Order order) {
    if(order.getOrderId() == null) {
        order.setOrderId(UUID.randomUUID().toString());
    }
    ConnectionFactory factory=new ConnectionFactory();
    factory.setHost("localhost");
    try(Connection connection=factory.newConnection();
        Channel channel= connection.createChannel()
    ){
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT);
        ObjectMapper mapper=new ObjectMapper();
        String orderMessage=mapper.writeValueAsString(order);
        channel.basicPublish(EXCHANGE_NAME,"",null,orderMessage.getBytes());
        System.out.println("sent order with id: "+order.getOrderId());
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

public static void RecieveOrderConfirmation() throws IOException, TimeoutException {
    ConnectionFactory factory=new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    String responseExchange="order_responses";
    channel.exchangeDeclare(responseExchange,BuiltinExchangeType.FANOUT);
    String queueName="confirmationsQueue";
    channel.queueDeclare(queueName,true,false,false,null);
    channel.queueBind(queueName,responseExchange,"");
    System.out.println("waiting for confirmation");
    
    DeliverCallback deliverCallback=(consumerTag,delivery)->{
        String message=new String(delivery.getBody());
        ObjectMapper objectMapper=new ObjectMapper();
        ConfirmationOrder confirmationOrder=objectMapper.readValue(message,ConfirmationOrder.class);
        System.out.println(confirmationOrder.getOrderId()+": "+confirmationOrder.getStatus()+": "+confirmationOrder.getMessage()+": "+confirmationOrder.getCustomerName()+" : Order amount: "+confirmationOrder.getTotalOrderAmount());
        
        if ("payment in progress".equals(confirmationOrder.getStatus())) {
            processPayment(confirmationOrder, channel);
        } else {
            updateOrderInDatabase(confirmationOrder);
        }
    };
    channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
}

private static void processPayment(ConfirmationOrder confirmationOrder, Channel channel) {
    try {
        MongoCollection<Document> customerCollection = CustomerDB.getDb().getCollection("customers");
        Document customerDoc = customerCollection.find(new Document("username", confirmationOrder.getCustomerName())).first();
        
        if (customerDoc != null) {
            double currentBalance;
            Object balanceObj = customerDoc.get("balance");
            if (balanceObj instanceof Integer) {
                currentBalance = ((Integer) balanceObj).doubleValue();
            } else if (balanceObj instanceof Double) {
                currentBalance = (Double) balanceObj;
            } else {
                throw new IllegalStateException("Balance is neither Integer nor Double");
            }
            
            double orderAmount = confirmationOrder.getTotalOrderAmount();
            
            if (currentBalance >= orderAmount) {
                // Update customer balance
                double newBalance = currentBalance - orderAmount;
                customerCollection.updateOne(
                    new Document("username", confirmationOrder.getCustomerName()),
                    new Document("$set", new Document("balance", newBalance))
                );
                
                // Send payment confirmation
                ConfirmationOrder paymentConfirmation = new ConfirmationOrder(
                    confirmationOrder.getCustomerName(),
                    "payment completed",
                    "Payment successful",
                    confirmationOrder.getOrderId(),
                    confirmationOrder.getTotalOrderAmount()
                );
                
                channel.exchangeDeclare(PAYMENT_EXCHANGE, BuiltinExchangeType.FANOUT);
                String paymentMessage = new ObjectMapper().writeValueAsString(paymentConfirmation);
                channel.basicPublish(PAYMENT_EXCHANGE, "", null, paymentMessage.getBytes());
                
                // Update order status
                updateOrderInDatabase(paymentConfirmation);
            } else {
                // Send payment rejection
                ConfirmationOrder paymentRejection = new ConfirmationOrder(
                    confirmationOrder.getCustomerName(),
                    "payment rejected",
                    "Insufficient balance",
                    confirmationOrder.getOrderId(),
                    confirmationOrder.getTotalOrderAmount()
                );
                
                channel.exchangeDeclare(PAYMENT_EXCHANGE, BuiltinExchangeType.FANOUT);
                String paymentMessage = new ObjectMapper().writeValueAsString(paymentRejection);
                channel.basicPublish(PAYMENT_EXCHANGE, "", null, paymentMessage.getBytes());
                
                // Update order status
                updateOrderInDatabase(paymentRejection);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private static void updateOrderInDatabase(ConfirmationOrder confirmationOrder) {
    String orderId = confirmationOrder.getOrderId();
    String status = confirmationOrder.getStatus();
    MongoCollection<Document> collection = CustomerDB.getDb().getCollection("orders");
    Document query = new Document("orderId", orderId);
    Document update = new Document("$set", new Document("status", status));
    collection.updateOne(query, update);
    System.out.println("Updated order status in database: " + status);
}
}

