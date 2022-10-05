package io.pathway.models;

import org.json.JSONPropertyIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "Organisations")
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 150)
    private String name;

    @Lob
    @Column(name = "address")
    private String address;

    @Column(name = "type")
    private Short type;

    @Lob
    @Column(name = "logo")
    private String logo;

    @Lob
    @Column(name = "website")
    private String website;

    @Lob
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "organisation")
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "organisation")
    private Set<User> users = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JSONPropertyIgnore
    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @JSONPropertyIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}