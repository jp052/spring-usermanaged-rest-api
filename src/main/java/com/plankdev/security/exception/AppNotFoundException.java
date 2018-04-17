package com.plankdev.security.exception;

public class AppNotFoundException extends RuntimeException {
	public AppNotFoundException(String message) {
        super("could not find application. " + message);
    }
	
	public AppNotFoundException() {
        super("could not find application.");
    }

}
