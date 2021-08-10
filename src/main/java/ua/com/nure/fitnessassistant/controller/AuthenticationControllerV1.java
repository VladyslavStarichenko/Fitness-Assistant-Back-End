package ua.com.nure.fitnessassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.request.AuthenticationRequestDto;
import ua.com.nure.fitnessassistant.dto.request.UserRegisterDTO;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;

import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationControllerV1 {



    private final UserServiceSCRT userService;

    @Autowired
    public AuthenticationControllerV1(UserServiceSCRT userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login (@RequestBody AuthenticationRequestDto requestDto){
        Map<Object, Object> response = userService.signIn(requestDto);
       return ResponseEntity.ok(response);
    }


    @PostMapping("signUp")
    public ResponseEntity signUp (@RequestBody UserRegisterDTO user){
        Map<Object, Object> response = userService.signup(user.toUser());
        return ResponseEntity.ok(response);
    }
}
