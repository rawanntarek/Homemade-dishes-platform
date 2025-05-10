package com.example.customerservice;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;

public class Main {
    public static final String BASE_URI = "http://localhost:8083/customer-service/api/";

    public static AcknowledgmentSubscriber as=new AcknowledgmentSubscriber();
    // Start the server
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.example.customerservice.endpoints");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
           try {
               as.recieveConfirmation();
               final HttpServer server = startServer();
               System.out.printf("Server started at %s%nPress Ctrl+C to stop...%n", BASE_URI);
               // Keep server running

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
