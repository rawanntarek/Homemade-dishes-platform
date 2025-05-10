package com.example.customerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import org.bson.Document;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AcknowledgmentSubscriber {
    private static final String EXCHANGE_NAME="confirmation";
    public static void recieveConfirmation() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection= factory.newConnection();
            Channel channel= connection.createChannel())
        {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String queueName=channel.queueDeclare().getQueue();
            channel.queueBind(queueName,EXCHANGE_NAME,"");
            System.out.println("Waiting for messages");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                System.out.println("Received '" + message + "'");
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    ConfirmationOrder confirmationOrder = mapper.readValue(message, ConfirmationOrder.class);
                    String orderId = confirmationOrder.getOrderId();
                    String customerName = confirmationOrder.getCustomerName();
                    String orderMessage = confirmationOrder.getMessage();
                    String status=confirmationOrder.getStatus();
                    updateOrderInDatabase(confirmationOrder);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            };
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
