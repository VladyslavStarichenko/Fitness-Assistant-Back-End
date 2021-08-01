package ua.com.nure.fitnessassistant.controller;

import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.UserDto;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.service.user.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/")
@CrossOrigin
public class UserControllerV1 {

    @Autowired
    private UserService userService;




//    @PreAuthorize("hasRole('USER_ROLE')")
    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") UUID id) {
        User loggedInUser = getCurrentLoggedInUser();
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
//        if (user.equals(loggedInUser)) {
//            UserDto result = UserDto.fromUser(user);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            UserDto result = UserDto.fromUser(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private User getCurrentLoggedInUser() {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        return userService.findByUserName(username);
    }


}

