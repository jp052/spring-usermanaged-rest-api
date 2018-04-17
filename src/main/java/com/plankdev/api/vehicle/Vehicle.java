package com.plankdev.api.vehicle;

import com.plankdev.restcommons.Model;
import com.plankdev.security.dataaccess.Application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Vehicle implements Model {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Application application;

    @Override
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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
