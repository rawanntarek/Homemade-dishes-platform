package com.example.sellerservice;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class SellerService {

    public void saveSeller(Seller seller) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("accounts");

        Document doc = new Document("companyName", seller.getCompanyName())
                .append("companyPassword", seller.getPassword());
        collection.insertOne(doc);
    }

    public String loginSeller(String companyName, String password, HttpSession session) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("accounts");
        Document doc = collection.find(new Document("companyName", companyName)).first();

        if (doc == null) {
            return "Company with name " + companyName + " does not exist.";
        }

        String companyPassword = doc.getString("companyPassword");
        if (!companyPassword.equals(password)) {
            return "Company with name " + companyName + " has the wrong password.";
        }
        session.setAttribute("companyName", companyName);

        return "Login successful!";
    }

    public void AddDishes(Dish dish,HttpSession session) {
        String companyName = (String) session.getAttribute("companyName");
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("dishes");
        Document doc = new Document("DishName", dish.getName())
                .append("DishPrice", dish.getPrice())
                .append("DishAmount", dish.getAmount())
                .append("CompanyName", companyName);  // Store company name with the dish

        collection.insertOne(doc);
    }
    public List<Dish> getDishes(String companyName) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("dishes");
        List<Dish> dishes = new ArrayList<Dish>();
        for (Document doc : collection.find()) {
            Dish dish = new Dish();
            dish.setName(doc.getString("DishName"));
            dish.setPrice(doc.getDouble("DishPrice"));
            dish.setAmount(doc.getInteger("DishAmount"));
            dishes.add(dish);

        }
        return dishes;
    }
}
