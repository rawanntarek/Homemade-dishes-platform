package com.example.sellerservice;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8081/seller-service/api/";

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
            Thread.currentThread().join(); // keep server alive
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
