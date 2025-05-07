package com.example.sellerservice.api;

import com.example.sellerservice.models.Seller;
import com.example.sellerservice.services.SellerService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/sellers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellerEndpoint {

    private SellerService sellerService = new SellerService();

    @POST
    public Response createSeller(Seller seller) {
        sellerService.saveSeller(seller);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listSellers() {
        List<Seller> sellers = sellerService.getAllSellers();
        if (sellers.isEmpty()) return Response.noContent().build();
        return Response.ok(sellers).build();
    }
}
