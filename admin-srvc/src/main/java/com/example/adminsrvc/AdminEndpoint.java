package com.example.adminsrvc;

import com.example.adminsrvc.EJB.AdminBean;
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
        List<String> result = adminBean.createSellerAccount(names);
        return Response.status(Response.Status.OK).entity(result).build();
    }
    @GET
    @Path("/getSellers")
    public Response getSellers() {
        if(adminBean.ListSellerAccounts().size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(adminBean.ListSellerAccounts()).build();
    }

    @GET
    @Path("/getCustomers")
    public Response getCustomers() {
        if(adminBean.ListCustomerAccounts().size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(adminBean.ListCustomerAccounts()).build();
    }

    @GET
    @Path("/test")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }
}
