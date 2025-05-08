package com.example.sellerservice.endpoints;

import com.example.sellerservice.Dish;
import com.example.sellerservice.Seller;
import com.example.sellerservice.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
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
    public Response login(Seller seller, @Context HttpServletRequest request) {
        // Ensure request is not null before getting session
        if (request == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"success\": false, \"message\": \"Request is null.\"}").build();
        }
        System.out.println("HttpServletRequest is injected: " + request); // Add this log for debugging

        // Authenticate the seller
        String loginMessage = sellerService.loginSeller(seller.getCompanyName(), seller.getPassword(), request.getSession());

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
    public Response addDish(Dish dish, @Context HttpServletRequest request) {
        if (request == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"success\": false, \"message\": \"Request is null.\"}").build();
        }

        HttpSession session = request.getSession();
        sellerService.AddDishes(dish, session);
        return Response.ok().build();
    }

    @Path("getDishes")
    @GET
    public Response getDishes(@Context HttpServletRequest request) {
        if (request == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"success\": false, \"message\": \"Request is null.\"}").build();
        }

        HttpSession session = request.getSession();
        String companyName = (String) session.getAttribute("companyName");
        List<Dish> dishes = sellerService.getDishes(companyName);
        return Response.ok().entity(dishes).build();
    }
}
