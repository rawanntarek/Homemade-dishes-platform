package com.example.customerservice;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private static final String seller_service_api="http://localhost:8082/seller-service/api/sellers/getAllDishes";
    public boolean RegisterCustomer(Customer customer) {
        MongoCollection<Document> collection=CustomerDB.getDb().getCollection("customers");
        Document usernameExists=collection.find(new Document("username", customer.getUsername())).first();
        if(usernameExists!=null)
        {
            return false;
        }
        Document doc = new Document("username", customer.getUsername())
                .append("password", customer.getPassword());
        collection.insertOne(doc);
        return true;

    }

    public boolean LoginCustomer(Customer customer) {
        MongoCollection<Document> collection=CustomerDB.getDb().getCollection("customers");
        Document doc = collection.find(new Document("username", customer.getUsername()).append("password", customer.getPassword())).first();
        if(doc==null) {
            return false;
        }
        else
        {
            return true;
        }
    }
    public List<Dish> getDishes() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(seller_service_api);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Dish> dishes = response.readEntity(new GenericType<List<Dish>>() {});
            response.close();
            return dishes;
        } else {
            response.close();
            return new ArrayList<>();
        }

    }
    public void storeOrder(Order order) {
        MongoCollection<Document> collection=CustomerDB.getDb().getCollection("orders");
        Document doc=new Document().append("customerName", order.getCustomerName())
                .append("status", order.getStatus())
                .append("orderId", order.getOrderId());
        List<Document> dishes=new ArrayList<>();
        for(dish_order d:order.getDishes())
        {
            Document dish=new Document().append("dishName", d.getDishName()).append("amount", d.getAmount()).append("price", d.getPrice()).append("companyName", d.getCompanyName());
            dishes.add(dish);
        }
        doc.append("dishes", dishes);
        collection.insertOne(doc);

    }
    public List<Order> getPendingOrders(String customerName)
    {
        MongoCollection<Document> collection=CustomerDB.getDb().getCollection("orders");
        FindIterable<Document>documents =collection.find(new Document("status", "pending").append("customerName", customerName));
        List<Order> orders=new ArrayList<>();
        for(Document doc:documents)
        {
            Order order=new Order();
            order.setCustomerName(doc.getString("customerName"));
            order.setOrderId(doc.getString("orderId"));
            order.setStatus(doc.getString("status"));
            List<dish_order>dishes=new ArrayList<>();

            for(Document dish:(List<Document>)doc.get("dishes"))
            {
                dish_order d=new dish_order();
                d.setDishName(dish.getString("dishName"));
                d.setAmount(dish.getInteger("amount"));
                d.setPrice(dish.getDouble("price"));
                d.setCompanyName(dish.getString("companyName"));
                dishes.add(d);
            }
            order.setDishes(dishes);
            orders.add(order);
        }
        return orders;


    }
}
