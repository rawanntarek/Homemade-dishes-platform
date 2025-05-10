package com.example.sellerservice;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AcknowledgmentPublisher {
    private static final String EXCHANGE_NAME="confirmation";

    public static void sendConfirmation(String orderID,String customerName,String status,String message) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try(Connection connection= connectionFactory.newConnection();
            Channel channel= connection.createChannel())
        {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String confirmation_message="Order ID: "+orderID+" Customer name: "+customerName+" your Order status became: "+status+" stock Availability: "+message;
            channel.basicPublish(EXCHANGE_NAME,"",null,confirmation_message.getBytes());
            System.out.println("sent confirmation message"+confirmation_message);
        }

    }
}
