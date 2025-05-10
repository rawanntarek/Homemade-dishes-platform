package com.example.customerservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class OrderPublisher {
    private static final AcknowledgmentSubscriber subscriber = new AcknowledgmentSubscriber();
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
        new Thread(() -> {
            try {
                subscriber.RecieveConfirmation();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }).start();
            }
    catch(Exception e){
        e.printStackTrace();
    }
}
}
