package com.example.customerservice;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class CustomerDB {
    private static MongoDatabase db;

    static {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://rawaaan245:rawantarek@customerservicedb.dcqjy4p.mongodb.net/?retryWrites=true&w=majority&appName=CustomerServiceDB");
        db = mongoClient.getDatabase("customerservicedb");
    }

    public static MongoDatabase getDb() {
        return db;
    }

}
