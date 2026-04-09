package org.acme;

import io.quarkus.scheduler.Scheduled; 
import org.jboss.logging.Logger;      
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UsersService {

    private static final Logger LOG = Logger.getLogger(UsersService.class);

    @Inject
    EntityManager em;

    @Scheduled(every = "30s")
    void automatskiIzvjestaj() {
        List<Users> sviKorisnici = getAllUsers();
        LOG.info(">>> [SCHEDULER] Trenutno stanje u bazi: " + sviKorisnici.size() + " korisnika.");
    }
  

    @Transactional
    public Users addUser(Users user) {
        if (user.getUserProfile() != null) {
            user.getUserProfile().setUser(user);
        }
        em.persist(user);
        return user;
    }

    public List<Users> getAllUsers() {
        return em.createNamedQuery(Users.GET_ALL_USERS, Users.class).getResultList();
    }

    public Users getUserById(Long id) {
        return em.find(Users.class, id);
    }

    public Users findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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

    public List<UserProfile> getAllProfiles() {
        return em.createQuery("SELECT p FROM UserProfile p", UserProfile.class).getResultList();
    }

    public UserProfile getProfileById(Long id) {
        return em.find(UserProfile.class, id);
    }

    @Transactional
    public UserProfile updateProfile(Long id, UserProfile updatedData) {
        UserProfile existingProfile = em.find(UserProfile.class, id);
        if (existingProfile != null) {
            existingProfile.setFirstName(updatedData.getFirstName());
            existingProfile.setLastName(updatedData.getLastName());
            existingProfile.setBio(updatedData.getBio());
            return existingProfile;
        }
        return null;
    }
}