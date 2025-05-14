package com.example.adminsrvc.EJB;

import com.example.adminsrvc.ConfirmationOrder;
import com.example.adminsrvc.entities.Customer;
import com.example.adminsrvc.entities.Seller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                URL url = new URL("http://localhost:8082/seller-service/api/sellers");
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

    public List<Customer> ListCustomerAccounts() throws IOException {
        URL url = new URL("http://localhost:8083/customer-service/api/customers/getCustomers");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int status = connection.getResponseCode();

        List<Customer> customers = new ArrayList<>();

        if (status == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                ObjectMapper mapper = new ObjectMapper();
                customers = mapper.readValue(content.toString(), mapper.getTypeFactory().constructCollectionType(List.class, Customer.class));
            }
        } else if (status == 204) {
            System.out.println("No customers found.");
        } else {
            System.out.println("Error fetching customers. HTTP Status Code: " + status);
        }

        return customers;
    }

    public List<ConfirmationOrder> getNotifications()
    {
        MongoCollection<Document> collection = dbConnection.getDb().getCollection("notifications");
        List<ConfirmationOrder> notifications = new ArrayList<>();
        for (Document doc : collection.find()) {
            String orderId = doc.getString("orderId");
            String customerName = doc.getString("customerName");
            String message = doc.getString("message");
            String status = doc.getString("status");
            Double total = doc.getDouble("totalOrderAmount");
            notifications.add(new ConfirmationOrder(customerName,status,message,orderId,total));
        }
        return notifications;
    }
    public List<String> getLogs()
    {
        MongoCollection<Document> collection = dbConnection.getDb().getCollection("logs");
        List<String> logs = new ArrayList<>();
        for (Document doc : collection.find()) {
            String Message = doc.getString("message");
            logs.add(Message);
        }
        return logs;
    }
}
