package com.example.customerservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class OrderPublisher {
private static final String EXCHANGE_NAME = "orders";

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
            System.out.println(confirmationOrder.getOrderId()+": "+confirmationOrder.getStatus()+": "+confirmationOrder.getMessage()+": "+confirmationOrder.getCustomerName());
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
    }


}

