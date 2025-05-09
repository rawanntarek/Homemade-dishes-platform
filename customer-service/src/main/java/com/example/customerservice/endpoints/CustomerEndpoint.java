package com.example.customerservice.endpoints;

import com.example.customerservice.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerEndpoint {
    CustomerService customerService=new CustomerService();
    OrderPublisher orderPublisher=new OrderPublisher();
    private List<Dish> avaialableDishes;

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
        if(dishes.isEmpty())
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            return Response.ok(dishes).build();
        }
    }
    @Path("/PlaceOrder")
    @POST
    public Response PlaceOrder(List<Order> orders) {


        orderPublisher.placeOrder(orders);
        return Response.ok().build();
    }
}
