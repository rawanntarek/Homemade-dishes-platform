package com.example.adminsrvc;

import com.rabbitmq.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageSubscriber {

    // Method to receive the failed payment messages
    public static void receiveFailedPaymentMessage() throws IOException, TimeoutException {
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
            ConfirmationOrder failedPayment = mapper.readValue(message, ConfirmationOrder.class);
            System.out.println("Failed Payment Order ID: " + failedPayment.getOrderId());
            System.out.println("Customer Name: " + failedPayment.getCustomerName());
            System.out.println("Status: " + failedPayment.getStatus());
            System.out.println("Message: " + failedPayment.getMessage());
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
