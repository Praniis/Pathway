package io.pathway.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONPropertyIgnore;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", length = 150)
    private String username;

    @Column(name = "password", length = 150)
    private String password;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Lob
    @Column(name = "profileImage")
    private String profileImage;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserRoleId")
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "OrganisationId")
    private Organisation organisation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JSONPropertyIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @JSONPropertyIgnore
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @JSONPropertyIgnore
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

}