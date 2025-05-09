package com.example.customerservice.endpoints;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cutsomers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerEndpoint {
    CustomerService customerService=new CustomerService();
    @Path("/register")
    @POST
    public Response register(Customer customer) {
        customerService.RegisterCustomer(customer);
        return Response.ok().build();
    }
    @Path("/test")
    @GET
    public Response test() {
        return Response.ok().build();
    }
}
