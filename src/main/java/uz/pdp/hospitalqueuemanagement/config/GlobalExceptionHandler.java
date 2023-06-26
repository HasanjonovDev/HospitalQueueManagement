package uz.pdp.hospitalqueuemanagement.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.pdp.hospitalqueuemanagement.exception.AuthorizationFailedException;
import uz.pdp.hospitalqueuemanagement.exception.DataNotFoundException;
import uz.pdp.hospitalqueuemanagement.exception.RequestValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<String> dataNotFound(DataNotFoundException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(value = {RequestValidationException.class})
    public ResponseEntity<String> requestValidation(RequestValidationException e){
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(value = {AuthorizationFailedException.class})
    public ResponseEntity<String> authorizationFailed(AuthorizationFailedException e){
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
