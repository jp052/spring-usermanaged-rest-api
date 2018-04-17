package com.plankdev.restcommons;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public class ResponseBuilder {

    public static <T extends Model> ResponseEntity<?> buildNewModelResponseEntity(Optional<T> objectOpt) {
        return buildModelResponseEntity(objectOpt, true);
    }

    public static <T extends Model> ResponseEntity<?> buildEditedModelResponseEntity(Optional<T> objectOpt) {
        return buildModelResponseEntity(objectOpt, false);
    }

    private static <T extends Model> ResponseEntity<?> buildModelResponseEntity(Optional<T> objectOpt, boolean isNew) {
        ResponseEntity<?> response;

        if (objectOpt.isPresent()) {
            T model = objectOpt.get();
            ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

            //adds the id of the current model to the URL
            if (isNew) {
                servletUriComponentsBuilder.path("/{id}");
            }

            URI location = servletUriComponentsBuilder.buildAndExpand(model.getId()).toUri();
            response = ResponseEntity.created(location).body(model);
        } else {
            response = ResponseEntity.noContent().build();
        }
        return response;
    }

/*    private static <T> ResponseEntity<?> buildModelResponseEntity(Optional<T> objectOpt, boolean isNew) {
        ResponseEntity<?> response;

        if (objectOpt.isPresent()) {
            T model = objectOpt.get();
            ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

            //adds the id of the current model to the URL
            if (isNew) {
                servletUriComponentsBuilder.path("/{id}");
            }

            URI location = servletUriComponentsBuilder.buildAndExpand(model.getId()).toUri();
            response = ResponseEntity.created(location).body(model);
        } else {
            response = ResponseEntity.noContent().build();
        }
        return response;
    }*/
}
