package com.example.customerservice;

import com.mongodb.client.MongoCollection;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private static final String seller_service_api="http://localhost:8082/seller-service/api/sellers/getAllDishes";
    public boolean RegisterCustomer(Customer customer) {
        MongoCollection<Document> collection=CustomerDB.getDb().getCollection("customers");
        Document usernameExists=collection.find(new Document("username", customer.getUsername())).first();
        if(usernameExists!=null)
        {
            return false;
        }
        Document doc = new Document("username", customer.getUsername())
                .append("password", customer.getPassword());
        collection.insertOne(doc);
        return true;

    }

    public boolean LoginCustomer(Customer customer) {
        MongoCollection<Document> collection=CustomerDB.getDb().getCollection("customers");
        Document doc = collection.find(new Document("username", customer.getUsername()).append("password", customer.getPassword())).first();
        if(doc==null) {
            return false;
        }
        else
        {
            return true;
        }
    }
    public List<Dish> getDishes() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(seller_service_api);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Dish> dishes = response.readEntity(new GenericType<List<Dish>>() {});
            response.close();
            return dishes;
        } else {
            response.close();
            return new ArrayList<>();
        }

    }
}
