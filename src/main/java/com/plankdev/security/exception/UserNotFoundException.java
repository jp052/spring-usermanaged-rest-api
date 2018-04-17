package com.plankdev.security.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("could not find user with username: " + username);
    }
}
