package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "https://api.ipify.org")
public interface IpClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String getMyIp();
}