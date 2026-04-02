package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/artist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArtistResource {

    @Inject
    ArtistService artistService;

  
    @POST
    @Path("/add")
    public Response addArtist(Artist artist) {
        Artist created = artistService.addArtist(artist);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

  
    @GET
    @Path("/all")
    public Response getAll() {
        List<Artist> artists = artistService.getAllArtists();
        return Response.ok(artists).build();
    }

    
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Artist artist = artistService.getArtistById(id);
        if (artist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(artist).build();
    }

    
    @PUT
    @Path("/follow")
    public Response followArtist(@QueryParam("userId") Long userId, @QueryParam("artistId") Long artistId) {
        if (userId == null || artistId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Potrebni su i userId i artistId").build();
        }
        
        artistService.followArtist(userId, artistId);
        return Response.ok("Korisnik (ID: " + userId + ") sada prati izvođača (ID: " + artistId + ")").build();
    }
}