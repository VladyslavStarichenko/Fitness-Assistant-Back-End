package ua.com.nure.fitnessassistant.controller.authentication;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.user.request.AuthenticationDto;
import ua.com.nure.fitnessassistant.dto.user.request.CUUserDto;
import ua.com.nure.fitnessassistant.exeption.EmptyDataException;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;

import java.util.Map;


@CrossOrigin(origins = "*",
methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RestController
@Api(value = "Authentication operations (login, sign up)")
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationController {


    private final UserServiceSCRT userService;

    @Autowired
    public AuthenticationController(UserServiceSCRT userService) {
        this.userService = userService;
    }


    @PostMapping("login")
    @ApiOperation(value = "Login to the system")
    public ResponseEntity login(@ApiParam(value = "Registered User object") @RequestBody AuthenticationDto requestDto) {
        if (requestDto == null) {
            throw new EmptyDataException("Invalid or empty input");
        }
        Map<Object, Object> response = userService.signIn(requestDto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("signUp")
    @ApiOperation(value = "Sign up to the system")
    public ResponseEntity signUp(@ApiParam(value = "User object to sign up to the system") @RequestBody CUUserDto user) {
        if (user == null) {
            throw new EmptyDataException("Invalid or empty input");
        }
        Map<Object, Object> response = userService.signup(user.toUser());
        return ResponseEntity.ok(response);
    }
}