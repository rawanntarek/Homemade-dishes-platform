package com.example.sellerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AcknowledgmentPublisher {
    private static final String EXCHANGE_NAME="confirmation";
    private static final String QUEUE_NAME = "confirmation-queue";

    public static void sendConfirmation(String orderID,String customerName,String status,String message) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try(Connection connection= connectionFactory.newConnection();
            Channel channel= connection.createChannel())
        {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            ConfirmationOrder confirmationOrder = new ConfirmationOrder(customerName, status, message, orderID);
            ObjectMapper objectMapper = new ObjectMapper();
            String confirmation_message = objectMapper.writeValueAsString(confirmationOrder);

            channel.basicPublish(EXCHANGE_NAME,"",null,confirmation_message.getBytes());
            System.out.println("sent confirmation message"+confirmation_message);
        }

    }
}
