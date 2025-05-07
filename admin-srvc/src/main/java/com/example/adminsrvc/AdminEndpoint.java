package com.example.adminsrvc;

import com.example.adminsrvc.ejb.AdminBean;
import com.example.adminsrvc.entities.Customer;
import com.example.adminsrvc.entities.Seller;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminEndpoint {
    @EJB
    AdminBean adminBean;

    @POST
    @Path("/createSellers")
    public Response createSellerAccounts(List<String> names) {
        adminBean.createSellerAccount(names);
        return Response.status(Response.Status.CREATED).entity("Seller accounts created").build();
    }

    @GET
    @Path("/getSellers")
    public List<Seller> getSellers() {
        return adminBean.ListSellerAccounts();
    }

    @GET
    @Path("/getCustomers")
    public List<Customer> getCustomers() {
        return adminBean.ListCustomerAccounts();
    }

    @GET
    @Path("/test")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }
}
