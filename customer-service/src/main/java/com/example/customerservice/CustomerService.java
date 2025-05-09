package com.example.customerservice;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class CustomerService {
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
}
