package com.example.customerservice;
import com.rabbitmq.client.*;

public class OrderPublisher {
private static final String EXCHANGE_NAME = "orders";
public static void placeOrder(Order order) {
    ConnectionFactory factory=new ConnectionFactory();
    factory.setHost("localhost");
    try(Connection connection=factory.newConnection();
        Channel channel= connection.createChannel()
    ){
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT);
            }
    catch(Exception e){
        e.printStackTrace();
    }
}
}
