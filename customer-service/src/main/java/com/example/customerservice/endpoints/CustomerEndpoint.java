package com.example.customerservice.endpoints;

import com.example.customerservice.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

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
    public Response PlaceOrder(Order order) throws IOException, TimeoutException {


        orderPublisher.placeOrder(order);
        customerService.storeOrder(order);
        return Response.ok().build();
    }
    @Path("/getCurrentOrders")
    @GET
    public Response getCurrentOrders(@QueryParam("customerName")String customerName) {
        if(customerName==null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Order>orders=customerService.getCurrentOrders(customerName);
        return Response.ok(orders).build();

    }
    @Path("/getPastOrders")
    @GET
    public Response getPastOrders(@QueryParam("customerName")String customerName) {
        if(customerName==null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Order>orders=customerService.getPastDishesOrders(customerName);
        return Response.ok(orders).build();
    }
    @Path("/getCustomers")
    @GET
    public Response getCustomers() {
        if(customerService.getCustomers().isEmpty())
        {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(customerService.getCustomers()).build();
    }
}
