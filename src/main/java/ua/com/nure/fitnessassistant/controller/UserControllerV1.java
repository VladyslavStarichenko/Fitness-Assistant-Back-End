package ua.com.nure.fitnessassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.nure.fitnessassistant.dto.UserDto;
import ua.com.nure.fitnessassistant.model.user.Role;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/")
public class UserControllerV1 {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasRole(ROLE_USER)")
    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") UUID id) {
        User loggedInUser = getCurrentLoggedInUser();
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (user.equals(loggedInUser)) {
            UserDto result = UserDto.fromUser(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    private User getCurrentLoggedInUser() {
        Object loggedInUserObj = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = ((UserDetails) loggedInUserObj).getUsername();
        return userService.findByUserName(username);
    }
}

