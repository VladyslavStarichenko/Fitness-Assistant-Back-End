package ua.com.nure.fitnessassistant.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.com.nure.fitnessassistant.exeption.CustomException;


import java.util.NoSuchElementException;

@ControllerAdvice
public class ProgramControllerAdvice {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleEmptyInput(){
        return new ResponseEntity<>("Input is empty" , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(){
        return new ResponseEntity<>("There is no element found" , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException customException) {
        return new ResponseEntity<>(customException.getMessage(), customException.getHttpStatus());
    }
}
