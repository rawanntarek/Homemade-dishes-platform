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
        // Authenticate the seller
        String loginMessage = sellerService.loginSeller(seller.getCompanyName(), seller.getPassword());

        // Check if the login was successful and return the appropriate response
        if (loginMessage.equals("Login successful!")) {
            // Return the response as a valid JSON object
            return Response.ok().entity("{\"success\": true, \"message\": \"Login successful!\"}").build();
        } else {
            // Return the response as a valid JSON object for failure
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"success\": false, \"message\": \"" + loginMessage + "\"}")
                    .build();
        }
    }
    @Path("addDish")
    @POST
    public Response addDish(Dish dish) {
        sellerService.AddDishes(dish);
        return Response.ok().build();
    }

}
