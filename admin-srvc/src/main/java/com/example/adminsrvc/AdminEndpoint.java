package com.example.adminsrvc;

import com.example.adminsrvc.EJB.AdminBean;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
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
    public Response getCustomers() throws IOException {
        if(adminBean.ListCustomerAccounts().isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(adminBean.ListCustomerAccounts()).build();
    }

    @GET
    @Path("/test")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }
    @GET
    @Path("/notifications")
    public Response getNotifications() {
        if(adminBean.getNotifications().size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(adminBean.getNotifications()).build();
    }
    @GET
    @Path("/logs")
    public Response getLogs() {
        if(adminBean.getLogs().size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(adminBean.getLogs()).build();
    }

}
