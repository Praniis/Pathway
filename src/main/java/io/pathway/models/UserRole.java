package io.pathway.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONPropertyIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "UserRoles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "permission", length = 1073741824)
    private String permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "OrganisationId")
    private Organisation organisation;

    @OneToMany(mappedBy = "userRole")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @JSONPropertyIgnore
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @JSONPropertyIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}