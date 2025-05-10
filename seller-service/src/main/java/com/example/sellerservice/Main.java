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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Main {
    public static final String BASE_URI = "http://localhost:8082/seller-service/api/";
    private static final String EXCHANGE_NAME = "orders";
    static AcknowledgmentPublisher ap=new AcknowledgmentPublisher();
    public static boolean isThereAvaialbleStock(List<dish_order> dishes) {
        MongoCollection<Document> collection=SellerDB.getDb().getCollection("dishes");
        for(dish_order dish:dishes) {
            Document doc=collection.find(new Document("DishName",dish.getDishName()).append("CompanyName",dish.getCompanyName())).first();
            if(doc==null)
            {
                System.out.println("Dish Not Found");
                return false;
            }
            int dishAmount=doc.getInteger("DishAmount");
            System.out.println("Dish Amount: "+dishAmount);
            if(dishAmount<dish.getAmount())
            {
                return false;
            }

        }
        return true;
    }

    public static void saveOrder(Order order,String status) {
        MongoCollection<Document> collection=SellerDB.getDb().getCollection("orders");
        Document doc=new Document()
                .append("orderId", order.getOrderId()).
                append("customerName",order.getCustomerName())
                .append("status",status);
        List<Document> dishes=new ArrayList<>();
        for(dish_order dish:order.getDishes())
        {
            Document dishDoc=new Document()
                    .append("dishName",dish.getDishName())
                    .append("companyName",dish.getCompanyName())
                    .append("amount",dish.getAmount())
                    .append("price",dish.getPrice());
            dishes.add(dishDoc);
        }
        doc.append("dishes",dishes);
        collection.insertOne(doc);
        System.out.println("Order added");
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
                ap.sendConfirmation(order.getOrderId(),order.getCustomerName(),Status,Message);
                ap.sendMinimumChargeConfirmation(order.getOrderId(),order.getCustomerName(), order.getTotalPrice());
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
