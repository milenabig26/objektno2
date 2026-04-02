package org.acme;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore; // Import za sprečavanje rekurzije

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stage_name", nullable = false)
    private String stageName;

    private String bio;

    // KLJUČNA IZMJENA: @JsonIgnore sprječava beskonačnu petlju (User -> Artist -> User...)
    // mappedBy mora odgovarati nazivu polja u klasi Users (followedArtists)
    @JsonIgnore
    @ManyToMany(mappedBy = "followedArtists", fetch = FetchType.LAZY)
    private List<Users> followers = new ArrayList<>();

    // Prazan konstruktor
    public Artist() {
    }

    // GETTERI I SETTERI

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Users> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Users> followers) {
        this.followers = followers;
    }
}