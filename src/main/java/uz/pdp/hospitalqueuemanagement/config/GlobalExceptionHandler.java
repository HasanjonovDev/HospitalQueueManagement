package uz.pdp.hospitalqueuemanagement.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.pdp.hospitalqueuemanagement.exception.BadRequestException;
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

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<String> authorizationFailed(BadRequestException e){
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
