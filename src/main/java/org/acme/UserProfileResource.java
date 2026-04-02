package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserProfileResource {

    @Inject
    UserProfileService profileService;

    @POST
    @Path("/add")
    public Response addProfile(UserProfile profile) {
        // Ovde koristimo updateProfile metodu iz servisa koju smo dodali
        UserProfile created = profileService.updateProfile(profile);
        return Response.ok().entity(created).build();
    }

    @GET
    @Path("/all")
    public Response getAll() {
        List<UserProfile> profiles = profileService.getAllProfiles();
        return Response.ok().entity(profiles).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        UserProfile profile = profileService.getProfileById(id);
        if (profile == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(profile).build();
    }
}