package com.example.customerservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.util.List;

public class OrderPublisher {
private static final String EXCHANGE_NAME = "orders";
public static void placeOrder(List<Order> orders) {
    ConnectionFactory factory=new ConnectionFactory();
    factory.setHost("localhost");
    try(Connection connection=factory.newConnection();
        Channel channel= connection.createChannel()
    ){
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT);
        ObjectMapper mapper=new ObjectMapper();
        String orderMessage=mapper.writeValueAsString(orders);
        channel.basicPublish(EXCHANGE_NAME,"",null,orderMessage.getBytes());

            }
    catch(Exception e){
        e.printStackTrace();
    }
}
}
