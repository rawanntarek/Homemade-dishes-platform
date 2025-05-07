package com.example.sellerservice.endpoints;

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

    @POST
    public Response create(Seller seller) {
        sellerService.saveSeller(seller);
        return Response.status(Response.Status.CREATED).entity("Seller account added").build();
    }


}
