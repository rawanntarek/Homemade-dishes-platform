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
        String url="mongodb://rawaaan245:rawantarek2003@ac-fxluyud-shard-00-00.8frdbwk.mongodb.net:27017,ac-fxluyud-shard-00-01.8frdbwk.mongodb.net:27017,ac-fxluyud-shard-00-02.8frdbwk.mongodb.net:27017/?ssl=true&replicaSet=atlas-k3ni61-shard-0&authSource=admin&retryWrites=true&w=majority&appName=AdminServiceDB";
        mongo = MongoClients.create(url);
        db = mongo.getDatabase("adminservicedb");
    }

    public MongoDatabase getDb() {
        return db;
    }
}
