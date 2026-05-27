package org.acme;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "app_users") 
@NamedQuery(name = Users.GET_ALL_USERS, query = "Select u from Users u")
public class Users {
    public static final String GET_ALL_USERS = "GetAllUsers";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; 

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile userProfile;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TimeZoneData> timeZoneDataList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CurrencyResponse> currencyResponses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_follows_artist",
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "artist_id") 
    )
    private List<Artist> followedArtists = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Promijenjeno na EAGER
    @JoinTable(
        name = "user_uploaded_files",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<UploadedFile> uploadedFiles = new ArrayList<>();
  

    public Users() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserProfile getUserProfile() { return userProfile; }
    public void setUserProfile(UserProfile userProfile) { this.userProfile = userProfile; }

    public List<TimeZoneData> getTimeZoneDataList() { return timeZoneDataList; }
    public void setTimeZoneDataList(List<TimeZoneData> timeZoneDataList) { this.timeZoneDataList = timeZoneDataList; }

    public List<CurrencyResponse> getCurrencyResponses() { return currencyResponses; }
    public void setCurrencyResponses(List<CurrencyResponse> currencyResponses) { this.currencyResponses = currencyResponses; }

    public List<Artist> getFollowedArtists() { return followedArtists; }
    public void setFollowedArtists(List<Artist> followedArtists) { this.followedArtists = followedArtists; }

    public List<UploadedFile> getUploadedFiles() { return uploadedFiles; }
    public void setUploadedFiles(List<UploadedFile> uploadedFiles) { this.uploadedFiles = uploadedFiles; }
}