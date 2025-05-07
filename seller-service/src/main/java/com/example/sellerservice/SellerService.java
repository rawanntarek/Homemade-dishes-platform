package com.example.sellerservice;

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


}
