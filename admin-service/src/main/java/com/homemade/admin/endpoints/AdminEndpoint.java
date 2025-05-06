package com.homemade.admin.endpoints;

import com.homemade.admin.ejb.AdminBean;
import com.homemade.admin.entities.Customer;
import com.homemade.admin.entities.Seller;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public String test() {
        return "Test endpoint is working!";
    }

}
