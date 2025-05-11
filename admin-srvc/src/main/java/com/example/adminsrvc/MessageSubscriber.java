package com.example.adminsrvc;

import com.example.adminsrvc.EJB.DBConnection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import org.bson.Document;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@Singleton
@Startup
public class MessageSubscriber {

    @EJB
    DBConnection dbConnection;
    // Method to receive the failed payment messages
    @PostConstruct
    public void receiveFailedPaymentMessage() throws IOException, TimeoutException {
        System.out.println("Waiting for failed payment messages...");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "paymentfailed";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");
        System.out.println("Waiting for failed payment messages...");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received message: " + message);
            ObjectMapper mapper = new ObjectMapper();
            ConfirmationOrder order = mapper.readValue(message, ConfirmationOrder.class);
            MongoCollection<Document> collection=dbConnection.getDb().getCollection("notifications");
            collection.insertOne(Document.parse(message));

        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
