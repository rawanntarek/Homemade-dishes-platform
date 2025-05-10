package com.example.customerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import org.bson.Document;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;

public class Main {
    public static final String BASE_URI = "http://localhost:8083/customer-service/api/";

    private static final String EXCHANGE_NAME="confirmation";
    String queueName = "confirmation-queue";

    public static void AcknowledgementSubscriber()throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection= factory.newConnection();
            Channel channel= connection.createChannel())
        {
            String queueName = "confirmation-queue";
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName,EXCHANGE_NAME,"");
            System.out.println("Waiting for confirmation messages");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(),"UTF-8");
                System.out.println("Received '" + message + "'");
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    ConfirmationOrder confirmationOrder = mapper.readValue(message, ConfirmationOrder.class);
                    String orderId = confirmationOrder.getOrderId();
                    String customerName = confirmationOrder.getCustomerName();
                    String orderMessage = confirmationOrder.getMessage();
                    String status=confirmationOrder.getStatus();
                    System.out.println("received '" + orderId + "'"+customerName+":"+orderMessage+":"+status);
                    updateOrderInDatabase(confirmationOrder);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        }
    }
    private static void updateOrderInDatabase(ConfirmationOrder confirmationOrder) {
        String orderId = confirmationOrder.getOrderId();
        String status = confirmationOrder.getStatus();
        MongoCollection<Document> collection = CustomerDB.getDb().getCollection("orders");
        Document query = new Document("orderId", orderId);
        Document update = new Document("$set", new Document("status", status));
        collection.updateOne(query, update);
        System.out.println("Updated order status in database: " + status);
    }

    // Start the server
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.example.customerservice.endpoints");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        final HttpServer server = startServer();
        System.out.printf("Server started at %s%nPress Ctrl+C to stop...%n", BASE_URI);

        try {
            AcknowledgementSubscriber();
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
