package com.homemade.admin.ejb;
import com.homemade.admin.endpoints.AdminApp;
import com.homemade.admin.entities.Customer;
import com.homemade.admin.entities.Seller;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AdminBean {
    @EJB
    DBConnection dbConnection;

    @EJB
    PasswordGeneratorBean passwordGeneratorBean;
    public void createSellerAccount(List<String> uniqueCompanyNames)
    {
        MongoCollection<Document> collection=dbConnection.getDb().getCollection("sellers");
        for(String uniqueCompanyName : uniqueCompanyNames)
        {
            String companyPassword=passwordGeneratorBean.generatePassword(10);
            Document doc = new Document("companyName", uniqueCompanyName).append("companyPassword",companyPassword);
            collection.insertOne(doc);
            System.out.println("created account for company: " + uniqueCompanyName+" and password: " + companyPassword);
        }
    }
    public List<Seller> ListSellerAccounts()
    {
        MongoCollection<Document> collection=dbConnection.getDb().getCollection("sellers");
        List<Seller> sellers=new ArrayList<>();
        for (Document doc : collection.find())
        {
            String companyName=doc.getString("companyName");
            String companyPassword=doc.getString("companyPassword");
            sellers.add(new Seller(companyName,companyPassword));


        }
        return sellers;

    }
    public List<Customer> ListCustomerAccounts()
    {
        MongoCollection<Document> collection=dbConnection.getDb().getCollection("customers");
        List<Customer> customers=new ArrayList<>();
        for (Document doc : collection.find())
        {
            String customerName=doc.getString("customerName");
            String customerPassword=doc.getString("customerPassword");
            customers.add(new Customer(customerName, customerPassword));

        }
        return customers;

    }



}