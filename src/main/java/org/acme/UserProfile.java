package org.acme;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_seq")
    @SequenceGenerator(name = "user_profile_seq", sequenceName = "user_profile_seq", allocationSize = 1)
    private Long id;

    private String firstName;
    private String lastName;
    private String bio;

    @OneToOne(mappedBy = "userProfile", fetch = FetchType.LAZY)
    @JsonIgnore
    private Users user;

    public UserProfile() {}

   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
}