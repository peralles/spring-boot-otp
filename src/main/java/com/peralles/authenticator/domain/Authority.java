package com.peralles.authenticator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.peralles.authenticator.enumeration.AuthorityName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "authority")
public class Authority {

    @Column(name = "name", length = 50)
    @NotNull
    @Id
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private List<User> users;

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
