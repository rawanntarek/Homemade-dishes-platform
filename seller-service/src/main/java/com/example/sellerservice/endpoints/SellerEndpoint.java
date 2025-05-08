package com.example.sellerservice.endpoints;

import com.example.sellerservice.Dish;
import com.example.sellerservice.Seller;
import com.example.sellerservice.SellerService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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
            return Response.ok().entity("{\"success\": true, \"message\": \"Login successful!\"}").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"success\": false, \"message\": \"" + loginMessage + "\"}")
                    .build();
        }
    }

    @Path("/addDish")
    @POST
    public Response addDish(Dish dish, @QueryParam("companyName") String companyName) {
        // Check if company name is provided
        if (companyName == null || companyName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"success\": false, \"message\": \"Company name is required.\"}")
                    .build();
        }

        // Add dish with the provided company name
        dish.setCompanyName(companyName);
        sellerService.AddDishes(dish);

        return Response.ok().entity("{\"success\": true, \"message\": \"Dish added successfully.\"}").build();
    }

    @Path("/getDishes")
    @GET
    public Response getDishes(@QueryParam("companyName") String companyName) {
        // Check if company name is provided
        if (companyName == null || companyName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"success\": false, \"message\": \"Company name is required.\"}")
                    .build();
        }

        // Get dishes for the provided company name
        List<Dish> dishes = sellerService.getDishesByCompany(companyName);
        return Response.ok().entity(dishes).build();
    }
    @PUT
    @Path("/updateDish")
    public Response updateDish(Dish dish, @QueryParam("companyName") String companyName,@QueryParam("dishName")String dishName) {
        SellerService sellerService = new SellerService();
        try{
            Response response=sellerService.updateDishes(dish, companyName,dishName);
            return response;

        }
        catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"success\": false, \"message\": \"Failed to update dish. Please try again.\"}")
                    .build();
        }


    }
}
