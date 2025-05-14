package com.example.sellerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.bson.Document;

public class LogPublisher {
    private static final String log_exchange="logs";
    public static void log(String serviceName,String severity,String message)
    {
        String routing_key = serviceName.replaceAll("\\s+", "") + "_" + severity;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection= factory.newConnection();
            Channel channel= connection.createChannel())
        {
            channel.exchangeDeclare(log_exchange, BuiltinExchangeType.TOPIC);
            LogMsg logMsg=new LogMsg(serviceName,severity,message);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(logMsg);
            channel.basicPublish(log_exchange,routing_key,null,json.getBytes());

            SellerDB sellerDB=new SellerDB();
            MongoCollection<Document> collection=sellerDB.getDb().getCollection("logs");
            Document doc=new Document("serviceName",serviceName)
                    .append("severity",severity)
                    .append("message",message);
            collection.insertOne(doc);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
