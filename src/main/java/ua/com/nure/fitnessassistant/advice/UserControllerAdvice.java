package ua.com.nure.fitnessassistant.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.com.nure.fitnessassistant.exeption.CustomException;

@ControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException customException) {
        return new ResponseEntity<>(customException.getMessage(), customException.getHttpStatus());
    }
}
