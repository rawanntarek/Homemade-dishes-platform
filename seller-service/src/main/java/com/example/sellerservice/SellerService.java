package com.example.sellerservice;

import com.mongodb.client.MongoCollection;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public List<Dish> getDishes() {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("dishes");
        List<Dish> dishes=new ArrayList<>();
        for (Document doc : collection.find()) {
            Dish dish = new Dish();
            dish.setName(doc.getString("DishName"));
            dish.setPrice(doc.getDouble("DishPrice"));
            dish.setAmount(doc.getInteger("DishAmount"));
            dish.setCompanyName(doc.getString("CompanyName"));
            dishes.add(dish);
        }
        return dishes;
    }
    public Response updateDishes(Dish dish, String companyName, String dishName) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("dishes");
        Document filter = new Document("DishName", dishName)
                .append("CompanyName", companyName);
        Document dishExists=collection.find(filter).first();
        if(dishExists==null) {
            return Response.noContent().build();
        }
        Document updateFields=new Document();

        if (dish.getName() != null && !dish.getName().isEmpty()) {
            updateFields.append("DishName", dish.getName());  // Update the name if it's provided
        }
        if (dish.getPrice() != 0) {  // Assuming price can't be zero or handle the null case
            updateFields.append("DishPrice", dish.getPrice());  // Update the price if it's provided
        }
        if (dish.getAmount() != 0) {  // Assuming amount can't be zero or handle the null case
            updateFields.append("DishAmount", dish.getAmount());  // Update the amount if it's provided
        }
        if(updateFields.isEmpty())
        {
            System.out.println("Nothing to update");
        }
        Document update = new Document("$set", updateFields);
        collection.updateOne(filter, update);
        return Response.status(Response.Status.OK).build();


    }

    public List<Order> getSoldDishesWithCustomerInfo(String companyName) {
        MongoCollection<Document> collection = SellerDB.getDb().getCollection("orders");
        List<Order> soldOrders = new ArrayList<>();
        Document filter = new Document("status", "payment completed");
        for (Document doc : collection.find(filter)) {
            List<Document> dishesDocs = (List<Document>) doc.get("dishes");
            boolean hasCompanyDish = false;
            List<dish_order> companyDishes = new ArrayList<>();
            for (Document dishDoc : dishesDocs) {
                if (companyName.equals(dishDoc.getString("companyName"))) {
                    hasCompanyDish = true;
                    dish_order d = new dish_order();
                    d.setDishName(dishDoc.getString("dishName"));
                    d.setAmount(dishDoc.getInteger("amount"));
                    d.setPrice(dishDoc.getDouble("price"));
                    d.setCompanyName(dishDoc.getString("companyName"));
                    companyDishes.add(d);
                }
            }
            if (hasCompanyDish) {
                Order order = new Order();
                order.setCustomerName(doc.getString("customerName"));
                order.setOrderId(doc.getString("orderId"));
                order.setStatus(doc.getString("status"));
                order.setDishes(companyDishes);
                soldOrders.add(order);
            }
        }
        return soldOrders;
    }
}
