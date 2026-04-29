package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "https://api.euroratesapi.dev")
public interface CurrencyClient {

    @GET
    @Path("/api/rates")
    @Produces(MediaType.APPLICATION_JSON)
   
    String getRates(@QueryParam("from") String from, @QueryParam("to") String to);
}