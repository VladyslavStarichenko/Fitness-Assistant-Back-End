package ua.com.nure.fitnessassistant.controller;

import liquibase.pro.packaged.O;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.AuthenticationRequestDto;
import ua.com.nure.fitnessassistant.dto.UserDto;
import ua.com.nure.fitnessassistant.dto.UserRegisterDTO;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.security.jwt.JwtTokenProvider;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;
import ua.com.nure.fitnessassistant.service.user.UserService;

import java.util.HashMap;
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
