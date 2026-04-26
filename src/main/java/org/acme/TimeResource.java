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
    TimeZonesClient timeZonesClient;

    @Inject
    @RestClient
    IpClient ipClient;

    @GET
    @Path("/automatski")
    @Produces(MediaType.APPLICATION_JSON)
    public TimeResponse dohvatiSveAutomatski() {
        String mojIp = ipClient.getMyIp().trim();
        return timeZonesClient.getTimeByIp(mojIp);
    }

    @GET
    @Path("/getTimezoneByIP")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional 
    public Users getTimezoneByIP(@QueryParam("userId") Long userId) {
        
        // Pronalaženje korisnika u bazi
        Users korisnik = em.find(Users.class, userId);
        
        if (korisnik == null) {
            throw new WebApplicationException("Korisnik sa ID-jem " + userId + " nije pronađen.", 404);
        }

        try {
            // 1. Dohvatanje IP adrese i vremena sa spoljnih servisa
            String mojIp = ipClient.getMyIp().trim();
            TimeResponse response = timeZonesClient.getTimeByIp(mojIp);

            // 2. Kreiranje novog zapisa
            TimeResponseRecord noviZapis = new TimeResponseRecord();
            noviZapis.dateTime = response.dateTime;
            noviZapis.timeZone = response.timeZone;
            noviZapis.year = response.year;
            noviZapis.month = response.month;
            noviZapis.day = response.day;
            noviZapis.time = response.time;
            
            // 3. Povezivanje sa korisnikom
            noviZapis.user = korisnik;
            korisnik.getTimeRecords().add(noviZapis);

            // 4. Čuvanje zapisa u bazu
            em.persist(noviZapis);

            // --- IZMJENA: Force lazy loading za SVE liste ---
            // Moramo inicijalizovati sve Lazy liste koje Users objekat ima, 
            // inače će Jackson pući pri pretvaranju u JSON.
            
            if (korisnik.getTimeRecords() != null) {
                korisnik.getTimeRecords().size();
            }
            
            // Ova linija rješava grešku sa slike (followedArtists):
            if (korisnik.getFollowedArtists() != null) {
                korisnik.getFollowedArtists().size();
            }
            
            return korisnik;

        } catch (Exception e) {
            // Ispisuje detaljnu grešku u Quarkus terminalu radi lakšeg debugging-a
            e.printStackTrace(); 
            throw new WebApplicationException("Greška: " + e.getMessage(), 500);
        }
    }
}