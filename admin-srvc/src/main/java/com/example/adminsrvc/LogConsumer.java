package com.example.adminsrvc;

import com.example.adminsrvc.EJB.DBConnection;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import org.bson.Document;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class LogConsumer {
    public static String Log_exchange="logs";
    public static void recieveLog() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection= connectionFactory.newConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(Log_exchange, BuiltinExchangeType.TOPIC,true);
        String queueName=channel.queueDeclare().getQueue();
        channel.queueBind(queueName,Log_exchange,"*_Error");
        System.out.println("waiting for logs");
        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            try {
                String message = new String(delivery.getBody());
                DBConnection db = new DBConnection();
                MongoCollection<Document> collection = db.getDb().getCollection("logs");
                Document doc = new Document("message", message);
                collection.insertOne(doc);
            }
            catch(Exception e) {
                e.printStackTrace();
            }

        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});




    }
}
