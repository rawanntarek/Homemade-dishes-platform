package com.example.adminsrvc.ejb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;


@Singleton
public class DBConnection {
    private MongoClient mongo;
    private MongoDatabase db;

    @PostConstruct //method called immediately after bean initialized
    public void init() {
        mongo = MongoClients.create("mongodb+srv://rawaaan245:123456@adminservicedb.8frdbwk.mongodb.net/?retryWrites=true&w=majority&appName=AdminServiceDB");
        db = mongo.getDatabase("AdminServiceDB");
    }

    public MongoDatabase getDb() {
        return db;
    }
}
