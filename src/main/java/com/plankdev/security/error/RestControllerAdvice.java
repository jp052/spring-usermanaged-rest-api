package com.plankdev.security.error;

import com.plankdev.security.exception.AppNotFoundException;
import com.plankdev.security.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Transforms normal Exceptions into custom error message and http status.
 */
@ControllerAdvice
public class RestControllerAdvice {

    /**
     * Translates UserNotFroundException into HTTP 404 (NOT FOUND)
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {UserNotFoundException.class, AppNotFoundException.class}) //TODO: add parent class to exceptions like ApiNotFoundExeption
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<?> userNotFoundExceptionHandler(UserNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        return response;
    }


}
