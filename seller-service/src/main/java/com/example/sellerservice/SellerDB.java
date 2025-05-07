package com.example.sellerservice;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class SellerDB {
    private static MongoDatabase db;

    static {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://rawaaan245:rawantarek2003@sellerservicedb.0m3nd6n.mongodb.net/?retryWrites=true&w=majority&appName=SellerServiceDB");
        db = mongoClient.getDatabase("sellerservicedb");
    }

    public static MongoDatabase getDb() {
        return db;
    }
}
