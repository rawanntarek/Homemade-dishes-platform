package com.example.customerservice;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class CustomerService {
    public void RegisterCustomer(Customer customer) {
        MongoCollection<Document> collection=CustomerDB.getDb().getCollection("customers");
        Document doc = new Document("username", customer.getUsername())
                .append("password", customer.getPassword());
        collection.insertOne(doc);

    }
}
