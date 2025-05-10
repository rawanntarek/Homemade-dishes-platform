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


    // Start the server
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.example.customerservice.endpoints");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        final HttpServer server = startServer();
        System.out.printf("Server started at %s%nPress Ctrl+C to stop...%n", BASE_URI);

        try {
            Thread.currentThread().join(); // keep server alive
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
