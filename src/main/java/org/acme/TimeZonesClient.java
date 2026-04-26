package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@RegisterRestClient(configKey = "TimeZonesClient")
@Path("/api/time")
public interface TimeZonesClient {

    @GET
    @Path("/current/ip")
    TimeResponse getTimeByIp(@QueryParam("ipAddress") String ipAddress);
}