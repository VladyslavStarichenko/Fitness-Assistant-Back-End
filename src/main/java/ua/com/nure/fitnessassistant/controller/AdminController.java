package ua.com.nure.fitnessassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.user.response.UserDto;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.UserRepository;
import ua.com.nure.fitnessassistant.service.user.UserService;
import ua.com.nure.fitnessassistant.service.user.impl.UserServiceImpl;

import java.util.UUID;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {


    private final UserService userService;
    private final  UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, UserServiceImpl userServiceImpl, UserRepository userRepository) {
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
        this.userRepository = userRepository;
    }

    @GetMapping("users/id={id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id){
        User user = userServiceImpl.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = userServiceImpl.fromUser(user);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("users/userName={userName}")
    public ResponseEntity<UserDto> getUserByName(@PathVariable(name = "userName") String userName){
        User user = userRepository.findUserByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = userServiceImpl.fromUser(user);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }


    @GetMapping("users/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public Page<UserDto> getUsers(@PathVariable int pageNumber,
                                  @PathVariable int pageSize,
                                  @PathVariable String sortBy){

        Page<User> userPage = userServiceImpl.getUsersPage(pageNumber,pageSize,sortBy);
        return new PageImpl<>(userPage.stream()
        .map(userServiceImpl::fromUser)
        .collect(Collectors.toList()),userPage.getPageable(),userPage.getTotalElements());
    }
}
