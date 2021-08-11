package ua.com.nure.fitnessassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.user.response.UserDto;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.service.user.UserService;
import ua.com.nure.fitnessassistant.service.user.impl.UserServiceImpl;

import java.util.UUID;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/")
public class AdminControllerV1 {


    private final UserService userService;
    private final  UserServiceImpl userServiceImpl;

    @Autowired
    public AdminControllerV1(UserService userService, UserServiceImpl userServiceImpl) {
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") UUID id){
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return  new ResponseEntity<>(result,HttpStatus.OK);

    }
    @GetMapping("users")
    public Page<User> getUsers(Pageable pageable){
        return userServiceImpl.getUsers(pageable);
    }
}
