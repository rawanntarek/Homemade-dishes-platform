package com.example.sellerservice.services;

import com.example.sellerservice.db.SellerDB;
import com.example.sellerservice.models.Seller;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class SellerService {

    public void saveSeller(Seller seller) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("sellers");
        Document doc = new Document("companyName", seller.getCompanyName())
                .append("password", seller.getPassword());
        collection.insertOne(doc);
    }

    public List<Seller> getAllSellers() {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("sellers");
        List<Seller> sellers = new ArrayList<>();
        for (Document doc : collection.find()) {
            sellers.add(new Seller(doc.getString("companyName"), doc.getString("password")));
        }
        return sellers;
    }
}
