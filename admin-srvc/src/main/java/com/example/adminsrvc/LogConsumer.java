package com.example.adminsrvc;

import com.example.adminsrvc.EJB.DBConnection;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import org.bson.Document;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Singleton
@Startup
public class LogConsumer {
    @EJB
    DBConnection dbConnection;
    public static String Log_exchange="logs";
    @PostConstruct
    public void recieveLog() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection= connectionFactory.newConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(Log_exchange, BuiltinExchangeType.TOPIC);
        String queueName=channel.queueDeclare().getQueue();
        channel.queueBind(queueName,Log_exchange,"Customer Service_Error");
        channel.queueBind(queueName,Log_exchange,"Seller Service_Error");

        System.out.println("waiting for logs");
        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            try {
                String message = new String(delivery.getBody());
                System.out.println("Received message: " + message);

                MongoCollection<Document> collection = dbConnection.getDb().getCollection("logs");
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
