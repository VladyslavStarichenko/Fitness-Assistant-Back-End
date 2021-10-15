package ua.com.nure.fitnessassistant.controller.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.user.request.AuthenticationDto;
import ua.com.nure.fitnessassistant.dto.user.request.CUUserDto;
import ua.com.nure.fitnessassistant.exeption.EmptyDataException;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;

import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationController {


    private final UserServiceSCRT userService;

    @Autowired
    public AuthenticationController(UserServiceSCRT userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationDto requestDto) {
        if(requestDto == null){
            throw new EmptyDataException("Invalid or empty input");
        }
        Map<Object, Object> response = userService.signIn(requestDto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody CUUserDto user) {
        if(user == null){
            throw new EmptyDataException("Invalid or empty input");
        }
        Map<Object, Object> response = userService.signup(user.toUser());
        return ResponseEntity.ok(response);
    }
}
