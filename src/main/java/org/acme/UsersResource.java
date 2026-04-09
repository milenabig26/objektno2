package org.acme;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    UsersService userService; 

    @POST
    @Path("/add")
    @RolesAllowed("admin")
    public Response addUser(Users user) {
        Users created = userService.addUser(user);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/all")
    public Response getAll() {
        List<Users> users = userService.getAllUsers();
        return Response.ok().entity(users).build();
    }

    
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Users user = userService.getUserById(id); 
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(user).build();
    }

    @GET
    @Path("/search")
    public Response getByUsername(@QueryParam("username") String username) {
        if (username == null || username.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username parametar nedostaje").build();
        }
        
        Users user = userService.findByUsername(username);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(user).build();
    }
}