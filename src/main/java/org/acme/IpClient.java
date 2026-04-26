package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "IpClient")
public interface IpClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String getMyIp(); 
}