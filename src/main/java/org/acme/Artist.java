package org.acme;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore; 

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_seq")
    @SequenceGenerator(name = "artist_seq", sequenceName = "artist_seq", allocationSize = 1)
    private Long id;

    @Column(name = "stage_name", nullable = false)
    private String stageName;

    private String bio;

    @JsonIgnore
    @ManyToMany(mappedBy = "followedArtists", fetch = FetchType.LAZY)
    private List<Users> followers = new ArrayList<>();

    public Artist() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<Users> getFollowers() { return followers; }
    public void setFollowers(List<Users> followers) { this.followers = followers; }
}