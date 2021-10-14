package ua.com.nure.fitnessassistant.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.com.nure.fitnessassistant.exeption.EmptyDataException;

@ControllerAdvice
public class AuthenticationControllerAdvice {

    @ExceptionHandler(EmptyDataException.class)
    public ResponseEntity<String> handleEmptyInput(){
        return new ResponseEntity<>("Input is empty" , HttpStatus.BAD_REQUEST);
    }

}
