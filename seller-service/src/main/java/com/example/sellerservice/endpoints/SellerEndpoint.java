package com.example.sellerservice.endpoints;

import com.example.sellerservice.Dish;
import com.example.sellerservice.Seller;
import com.example.sellerservice.SellerService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellerEndpoint {

    SellerService sellerService = new SellerService();

    @Path("/createSeller")
    @POST
    public Response create(Seller seller) {
        sellerService.saveSeller(seller);
        return Response.status(Response.Status.CREATED).entity("Seller account added").build();
    }

    @POST
    @Path("/login")
    public Response login(Seller seller) {
        sellerService.loginSeller(seller.getCompanyName(), seller.getPassword());
        return Response.ok().entity("Login attempted for " + seller.getCompanyName()).build();
    }
    @Path("addDish")
    @POST
    public Response addDish(Dish dish) {
        sellerService.AddDishes(dish);
        return Response.ok().entity("Dish added").build();
    }

}
