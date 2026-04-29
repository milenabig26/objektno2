package org.acme;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.transaction.Transactional;
import java.util.ArrayList;

@Path("/currencyConversion")
public class CurrencyResource {

    @Inject
    EntityManager em;

    @Inject
    @RestClient
    CurrencyClient currencyClient;

    @Inject
    ObjectMapper objectMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Users convertCurrency(
            @QueryParam("from") String from,
            @QueryParam("to") String to,
            @QueryParam("value") double value,
            @QueryParam("userId") Long userId) {

        Users korisnik = em.find(Users.class, userId);
        if (korisnik == null) {
            throw new WebApplicationException("Korisnik nije pronađen", 404);
        }

        try {
            String response = currencyClient.getRates(from, to);
            JsonNode root = objectMapper.readTree(response);
            double currentRate = root.path("rates").path(to).asDouble();

            if (currentRate == 0) {
                currentRate = 1.0;
            }

            CurrencyResponse cr = new CurrencyResponse();
            cr.setFromCurrency(from);
            cr.setToCurrency(to);
            cr.setRate(currentRate);
            cr.calculateAndSetConversion(value);
            cr.setUser(korisnik);

            if (korisnik.getCurrencyResponses() == null) {
                korisnik.setCurrencyResponses(new ArrayList<>());
            }
            korisnik.getCurrencyResponses().add(cr);
            
            em.persist(cr);

            if (korisnik.getCurrencyResponses() != null) {
                korisnik.getCurrencyResponses().size();
            }
            if (korisnik.getFollowedArtists() != null) {
                korisnik.getFollowedArtists().size();
            }
            if (korisnik.getTimeZoneDataList() != null) {
                korisnik.getTimeZoneDataList().size();
            }

            return korisnik;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException("Greška pri konverziji: " + e.getMessage(), 500);
        }
    }
}