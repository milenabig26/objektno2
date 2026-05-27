package org.acme;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    UsersService userService; 

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/quarkus_uploads/";

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

        if (user.getUploadedFiles() != null) {
            for (UploadedFile uf : user.getUploadedFiles()) {
                if (uf.filename != null) {
                    File diskFile = new File(uf.filename);
                    if (diskFile.exists()) {
                        uf.setFile(diskFile);
                    }
                }
            }
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

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional 
    public Response uploadFile(
            @QueryParam("id") Long userId, 
            @FormParam("fileName") String fileName, 
            @FormParam("file") org.jboss.resteasy.reactive.multipart.FileUpload file) {
        
        Users user = userService.getUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Korisnik sa ID-jem " + userId + " nije pronađen.")
                           .build();
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Popravljeno: Koristimo punu putanju paketa za nio Path da ne smeta @Path anotaciji
        java.nio.file.Path targetPath = Paths.get(UPLOAD_DIR, fileName);
        UploadedFile uploadedFileEntity = null;

        if (Files.exists(targetPath)) {
            
            for (UploadedFile f : user.getUploadedFiles()) {
                if (f.filename.equals(targetPath.toString())) {
                    uploadedFileEntity = f;
                    break;
                }
            }

            if (uploadedFileEntity == null) {
                uploadedFileEntity = new UploadedFile();
                uploadedFileEntity.filename = targetPath.toString();
                user.getUploadedFiles().add(uploadedFileEntity);
                
                userService.addUser(user); 
            }

            return Response.ok(user)
                           .header("X-Status-Message", "Fajl vec postoji. Putanja je preuzeta i veza azurirana.")
                           .build();

        } else {
            try {
                Files.copy(file.filePath(), targetPath);
            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                               .entity("Greška prilikom upisa fajla na disk: " + e.getMessage())
                               .build();
            }

            uploadedFileEntity = new UploadedFile();
            uploadedFileEntity.filename = targetPath.toString();

            user.getUploadedFiles().add(uploadedFileEntity);
          
            userService.addUser(user); 

            return Response.status(Response.Status.CREATED).entity(user).build();
        }
    }
}