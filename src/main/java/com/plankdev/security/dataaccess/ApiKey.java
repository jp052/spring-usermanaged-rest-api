package com.plankdev.security.dataaccess;

import javax.persistence.*;

@Entity
public class ApiKey {

    @Id
    @GeneratedValue
    private Long id;

    @Lob //jwtToken can get larger than standard string size of 255 char.
    @Column(length=10000)
    private String jwtToken;

    private boolean active;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
