package com.plankdev.security.dataaccess;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//TODO: rename to App and all other related classes too
@Entity
public class Application {

    public static final String APP_NAME_KEY = "appname";

	@Id
    @GeneratedValue
    private Long id;

    //name and appUser must be unique key, as on user is only allowed to have one application with the same name.
    private String name;

    @ManyToOne
    private ApiKey apiKey;

   /* @ManyToOne
    private List<ApiKey> apiKeyBlacklist;*/

    //Parent side of relation
    @ManyToOne
    @JsonBackReference //prevents infinity loop
    private AppUser appUser;

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

    public ApiKey getApiKey() {
        return apiKey;
    }

    public void setApiKey(ApiKey apiKey) {
        this.apiKey = apiKey;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
