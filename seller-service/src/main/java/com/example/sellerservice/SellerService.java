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

    public String loginSeller(String companyName, String password) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("accounts");
        Document doc = collection.find(new Document("companyName", companyName)).first();

        if (doc == null) {
            return "Company with name " + companyName + " does not exist.";
        }

        String companyPassword = doc.getString("companyPassword");
        if (!companyPassword.equals(password)) {
            return "Company with name " + companyName + " has the wrong password.";
        }

        return "Login successful!";
    }

    public void AddDishes(Dish dish) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("dishes");
        Document doc = new Document("DishName", dish.getName())
                .append("DishPrice", dish.getPrice())
                .append("DishAmount", dish.getAmount())
                .append("CompanyName", dish.getCompanyName());  // Save company name with the dish
        collection.insertOne(doc);
    }

    public List<Dish> getDishesByCompany(String companyName) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("dishes");
        List<Dish> dishes = new ArrayList<Dish>();
        Document filter = new Document("CompanyName", companyName);

        for (Document doc : collection.find(filter)) {
            Dish dish = new Dish();
            dish.setName(doc.getString("DishName"));
            dish.setPrice(doc.getDouble("DishPrice"));
            dish.setAmount(doc.getInteger("DishAmount"));
            dishes.add(dish);

        }
        return dishes;
    }
}
