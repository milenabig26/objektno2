package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserProfileService {

    @Inject
    EntityManager em;

    @Transactional
    public UserProfile addProfile(UserProfile profile) {
        return em.merge(profile);
    }

    public List<UserProfile> getAllProfiles() {
        return em.createQuery("SELECT p FROM UserProfile p", UserProfile.class)
                 .getResultList();
    }

    public UserProfile getProfileById(Long id) {
        return em.find(UserProfile.class, id);
    }

    @Transactional
    public UserProfile updateProfile(UserProfile profile) {
        return em.merge(profile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        UserProfile profile = getProfileById(id);
        if (profile != null) {
            em.remove(profile);
        }
    }
}