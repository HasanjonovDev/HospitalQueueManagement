package uz.pdp.hospitalqueuemanagement.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

public class RequestValidationException extends RuntimeException{
    private final String message;


    public RequestValidationException(List<ObjectError> errorList){
        StringBuilder sb=new StringBuilder();

        for (ObjectError error:errorList){
            sb.append(error.getDefaultMessage()).append("\n");
        }
        this.message=sb.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
