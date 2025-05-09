package com.example.customerservice.endpoints;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.Dish;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerEndpoint {
    CustomerService customerService=new CustomerService();
    @Path("/register")
    @POST
    public Response register(Customer customer) {
        boolean registerStatus=customerService.RegisterCustomer(customer);
        if(registerStatus)
        {
            return Response.ok().build();

        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    @Path("/test")
    @GET
    public Response test() {
        return Response.ok().build();
    }
    @Path("/login")
    @POST
    public Response login(Customer customer) {
        boolean loginStatus=customerService.LoginCustomer(customer);
        if(loginStatus)
        {
            return Response.ok().build();
        }
        else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
    @Path("/getAllDishes")
    @GET
    public Response getAllDishes() {
        List<Dish> dishes=customerService.getDishes();
        return Response.ok(dishes).build();
    }
}
