package com.example.sellerservice;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class SellerService {

    public void saveSeller(Seller seller) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("accounts");

        Document doc = new Document("companyName", seller.getCompanyName())
                .append("companyPassword", seller.getPassword());
        collection.insertOne(doc);
    }
    public void loginSeller(String companyName, String password) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("accounts");
        Document doc = collection.find(new Document("companyName", companyName)).first();
        if (doc == null) {
            System.out.println("Company with name " + companyName + " does not exist.");
            return;
        }
        String companyPassword = doc.getString("companyPassword");
        if (!companyPassword.equals(password)) {
            System.out.println("Company with name " + companyName + " has wrong password.");
            return;
        }
    }


}
