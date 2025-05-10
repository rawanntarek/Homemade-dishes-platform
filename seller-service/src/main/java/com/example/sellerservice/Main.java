package com.example.sellerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import org.bson.Document;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Main {
    public static final String BASE_URI = "http://localhost:8082/seller-service/api/";
    private static final String EXCHANGE_NAME = "orders";

    public static boolean isThereAvaialbleStock(List<dish_order> dishes) {
        MongoCollection<Document> collection=SellerDB.getDb().getCollection("dishes");
        for(dish_order dish:dishes)
        {
            Document doc=collection.find(new Document("\n" +
                    "DishName",dish.getDishName()).append("companyName",dish.getCompanyName())).first();
            if(doc==null)
            {
                return false;
            }
            int amountOfDishes=doc.getInteger("DishAmount");
            if(dish.getAmount()>amountOfDishes)
            {
                return false;
            }
        }
        return true;
    }
    public static void AcknowledgmentPublisher(String orderID,String customerName,String status,String message) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try(Connection connection= connectionFactory.newConnection();
        Channel channel= connection.createChannel())
        {
            String EXCHANGE_NAME="confirmation";
            channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT);
            String confirmation_message="Order ID: "+orderID+" Customer name: "+customerName+" your Order status became: "+status+" stock Availability: "+message;
            channel.basicPublish(EXCHANGE_NAME,"",null,confirmation_message.getBytes());
            System.out.println("sent confirmation message to order id"+orderID +" customer name: "+customerName);
        }
    }
    public static void saveOrder(Order order,String status) {
        MongoCollection<Document> collection=SellerDB.getDb().getCollection("orders");
        Document doc=new Document().append("orderId", order.getOrderId()).
                append("customerName",order.getCustomerName())
                .append("dishes",order.getDishes())
                .append("status",status);
        collection.insertOne(doc);
    }
    public static void orderSubscriber() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("Waiting for messages");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("Received '" + message + "'");
            try{
                ObjectMapper mapper = new ObjectMapper();
                Order order = mapper.readValue(message, Order.class);
                boolean checkStock=isThereAvaialbleStock(order.getDishes());
                String Status="";
                String Message="";
                if(checkStock)
                {
                    Status="completed";
                    Message="there is a stock available";
                }
                else
                {
                    Status="rejected";
                    Message="there is no stock available";
                }
                AcknowledgmentPublisher(order.getOrderId(),order.getCustomerName(),Status,Message);
                saveOrder(order,Status);
            }catch (Exception e) {
                e.printStackTrace();
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
    // Start the server
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.example.sellerservice.endpoints");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        final HttpServer server = startServer();
        System.out.printf("Server started at %s%nPress Ctrl+C to stop...%n", BASE_URI);
        // Keep server running
        try {
            orderSubscriber();
            Thread.currentThread().join(); // keep server alive
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
