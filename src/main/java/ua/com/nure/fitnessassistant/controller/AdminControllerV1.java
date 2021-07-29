package ua.com.nure.fitnessassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.nure.fitnessassistant.dto.UserDto;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminControllerV1 {

    @Autowired
    private UserService userService;

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") UUID id){
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return  new ResponseEntity<>(result,HttpStatus.OK);

    }
}
