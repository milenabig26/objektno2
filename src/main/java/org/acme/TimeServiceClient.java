package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "https://timeapi.io/api")
public interface TimeServiceClient {

    @GET
    @Path("/Time/current/ip")
    @Produces(MediaType.APPLICATION_JSON)
    // Dodajemo podrazumevani odgovor ako API zakaze
    String getTimeByIp(@QueryParam("ipAddress") String ipAddress);
}