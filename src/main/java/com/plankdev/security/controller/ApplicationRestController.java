package com.plankdev.security.controller;

import com.plankdev.security.dataaccess.AppUser;
import com.plankdev.security.dataaccess.Application;
import com.plankdev.security.dataaccess.ApplicationService;
import com.plankdev.security.dataaccess.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Optional;

//TODO: remove optionals as method params.
@RestController
@RequestMapping( value = "/api/applications", produces = MediaType.APPLICATION_JSON_VALUE )
public class ApplicationRestController {
    private ApplicationService applicationService;
    private UserService userService;

    @Autowired
    public ApplicationRestController(ApplicationService applicationService, UserService userService) {
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createApplication(Principal principal, @RequestBody Application application) {
    	AppUser appUser = userService.findByUsername(principal.getName());
        Optional<Application> createdApplicationOpt = applicationService.createApplication(application, appUser);
        ResponseEntity<?> response = buildApplicationResponseEntity(createdApplicationOpt);
        return response;
    }

    private ResponseEntity<?> buildApplicationResponseEntity(Optional<Application> createApplicationOpt) {
        ResponseEntity<?> response;

        if(createApplicationOpt.isPresent()) {
            Application createApplication = createApplicationOpt.get();
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}") //FIXME: adds double id for update, needs fix.
                    .buildAndExpand(createApplication.getId()).toUri();
            response = ResponseEntity.created(location).body(createApplication);
        } else {
            response = ResponseEntity.noContent().build();
        }
        return response;
    }

}
