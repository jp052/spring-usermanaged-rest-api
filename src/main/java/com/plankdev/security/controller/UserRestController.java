package com.plankdev.security.controller;

import com.plankdev.security.dataaccess.AppUser;
import com.plankdev.security.dataaccess.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE )
public class UserRestController {

    @Autowired
    private UserService userService;

    //# create appUser
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody AppUser appUser) {
        Optional<AppUser> createUserOpt = userService.createUser(appUser);
        ResponseEntity<?> response = buildUserResponseEntity(createUserOpt);

        return response;
    }

    //# read user
    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public AppUser readUser(@PathVariable Long userId ) {
        return this.userService.findById( userId );
    }


    //# update appUser
    @PutMapping(value = "/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody AppUser appUser) {
        if(appUser.getId() == null || appUser.getId() != userId) {
            throw new IllegalStateException("PathVariable and RequestBody id needs to be set and same value");
        }
        Optional<AppUser> updatedUserOpt = userService.updateUser(appUser);
        ResponseEntity<?> response = buildUserResponseEntity(updatedUserOpt);

        return response;
    }

    //# delete user
    @DeleteMapping(value = "/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

    //# list users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUser> listUsers() {
        return this.userService.findAll();
    }

    //# whoami
    //how to implemten this properly using Resful routes? Separate resource calls StatisticsController?
    @GetMapping(value= "/whoami")
    @PreAuthorize("hasRole('USER')")
    public AppUser whoami(Principal principal) {
        AppUser appUser = userService.findByUsername(principal.getName());
        return appUser;
    }

    private ResponseEntity<?> buildUserResponseEntity(Optional<AppUser> createUserOpt) {
        ResponseEntity<?> response;

        if(createUserOpt.isPresent()) {
            AppUser createAppUser = createUserOpt.get();
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(createAppUser.getId()).toUri();
            response = ResponseEntity.created(location).body(createAppUser);
        } else {
            response = ResponseEntity.noContent().build();
        }
        return response;
    }
}
