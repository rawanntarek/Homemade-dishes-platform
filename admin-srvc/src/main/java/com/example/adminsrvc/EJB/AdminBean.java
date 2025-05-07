package com.example.adminsrvc.EJB;

import com.example.adminsrvc.entities.Customer;
import com.example.adminsrvc.entities.Seller;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AdminBean {
    @EJB
    DBConnection dbConnection;

    @EJB
    PasswordGeneratorBean passwordGeneratorBean;

    public List<String> createSellerAccount(List<String> uniqueCompanyNames) {
        List<String> response = new ArrayList<>();
        MongoCollection<Document> collection = dbConnection.getDb().getCollection("sellers");

        for (String uniqueCompanyName : uniqueCompanyNames) {
            boolean exists = collection.find(Filters.eq("companyName", uniqueCompanyName)).first() != null;
            if (exists) {
                response.add("Company with name " + uniqueCompanyName + " is already added.");
                continue;
            }

            String companyPassword = passwordGeneratorBean.generatePassword(10);

            Document doc = new Document("companyName", uniqueCompanyName)
                    .append("companyPassword", companyPassword);
            collection.insertOne(doc);

            // Send to Seller Service
            try {
                URL url = new URL("http://localhost:8081/seller-service/api/sellers");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                String json = String.format("{\"companyName\":\"%s\", \"password\":\"%s\"}",
                        uniqueCompanyName, companyPassword);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int status = con.getResponseCode();
                if (status == 200 || status == 201) {
                    response.add("Created account for company: " + uniqueCompanyName + " and password: " + companyPassword);
                } else {
                    response.add("Created account for company: " + uniqueCompanyName + " and password: " + companyPassword +
                            " (but failed to notify seller service, status " + status + ")");
                }

            } catch (Exception e) {
                response.add("Error while notifying Seller Service for company: " + uniqueCompanyName + ". Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return response;
    }

    public List<Seller> ListSellerAccounts() {
        MongoCollection<Document> collection = dbConnection.getDb().getCollection("sellers");
        List<Seller> sellers = new ArrayList<>();
        for (Document doc : collection.find()) {
            String companyName = doc.getString("companyName");
            String companyPassword = doc.getString("companyPassword");
            sellers.add(new Seller(companyName, companyPassword));
        }
        return sellers;
    }

    public List<Customer> ListCustomerAccounts() {
        MongoCollection<Document> collection = dbConnection.getDb().getCollection("customers");
        List<Customer> customers = new ArrayList<>();
        for (Document doc : collection.find()) {
            String customerName = doc.getString("customerName");
            String customerPassword = doc.getString("customerPassword");
            customers.add(new Customer(customerName, customerPassword));
        }
        return customers;
    }
}
