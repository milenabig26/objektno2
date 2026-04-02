package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ArtistService {

    @Inject
    EntityManager em;

    
    @Transactional
    public Artist addArtist(Artist artist) {
        em.persist(artist);
        return artist;
    }

    
    public List<Artist> getAllArtists() {
        return em.createQuery("SELECT a FROM Artist a", Artist.class).getResultList();
    }

   
    public Artist getArtistById(Long id) {
        return em.find(Artist.class, id);
    }

    
    @Transactional
    public void followArtist(Long userId, Long artistId) {
        Users user = em.find(Users.class, userId);
        Artist artist = em.find(Artist.class, artistId);
        
        if (user != null && artist != null) {
            
            if (!user.getFollowedArtists().contains(artist)) {
                user.getFollowedArtists().add(artist);
                artist.getFollowers().add(user);
            }
        }
    }
}