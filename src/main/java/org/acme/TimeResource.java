package org.acme;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.transaction.Transactional;

@Path("/vreme")
public class TimeResource {

    @Inject
    EntityManager em;
    
    @Inject
    @RestClient
    TimeServiceClient timeServiceClient; 

    @Inject
    @RestClient
    IpClient ipClient;

    @GET
    @Path("/getTimezoneByIP")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional 
    public Users getTimezoneByIP(@QueryParam("userId") Long userId) {
        
        Users korisnik = em.find(Users.class, userId);
        
        if (korisnik == null) {
            throw new WebApplicationException("Korisnik nije pronadjen", 404);
        }

        try {
            // 1. IP adresa
            String mojIp = ipClient.getMyIp().trim();
            
            // 2. Vreme sa fallback-om
            String jsonOdgovor;
            try {
                jsonOdgovor = timeServiceClient.getTimeByIp(mojIp);
            } catch (Exception e) {
                jsonOdgovor = "{\"dateTime\":\"" + java.time.LocalDateTime.now() + "\", \"status\":\"manual\"}";
            }

            // 3. Novi zapis
            TimeZoneData noviZapis = new TimeZoneData();
            noviZapis.setDateTime(jsonOdgovor);
            noviZapis.setTimeZone("Sistem"); 
            noviZapis.setUser(korisnik);
            
            // 4. Inicijalizacija liste ako je null i dodavanje
            if (korisnik.getTimeZoneDataList() == null) {
                korisnik.setTimeZoneDataList(new java.util.ArrayList<>());
            }
            korisnik.getTimeZoneDataList().add(noviZapis);
            em.persist(noviZapis);

            // KLJUČNO ZA RJEŠAVANJE GREŠKE:
            // Moramo "dotaknuti" svaku lazy listu da bi je Hibernate učitao
            if (korisnik.getTimeZoneDataList() != null) {
                korisnik.getTimeZoneDataList().size();
            }
            
            // OVO JE DODATA LINIJA ZA TVOJU NOVU GREŠKU:
            if (korisnik.getFollowedArtists() != null) {
                korisnik.getFollowedArtists().size();
            }

            return korisnik;

        } catch (Exception e) {
            e.printStackTrace(); 
            throw new WebApplicationException("Greska: " + e.getMessage(), 500);
        }
    }
}