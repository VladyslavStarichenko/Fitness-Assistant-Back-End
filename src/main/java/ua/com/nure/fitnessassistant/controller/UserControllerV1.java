package ua.com.nure.fitnessassistant.controller;

import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.response.UserDto;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.ExerciseRepository;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;
import ua.com.nure.fitnessassistant.repository.UserRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;
import ua.com.nure.fitnessassistant.service.user.UserService;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/user/")
@CrossOrigin
public class UserControllerV1 {

    @Autowired
    private UserService userService;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceSCRT userServiceSCRT;


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("myAccount")
    public ResponseEntity<UserDto> getCurrentUser() {
        User loggedInUser = userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
            UserDto result = UserDto.fromUser(loggedInUser);
            return  ResponseEntity.ok().body(result);

    }



//    @PutMapping("/{programId}/exercises/{userId}")
//    ResponseEntity<User> CreateProgram(
//            @PathVariable Long programId,
//            @PathVariable UUID userId
//    ){
//        //TODO add checking for Optional
//        Program program =  programRepository.findById(programId).get();
//        User user = userRepository.findById(userId).get();
//
//
//        return ResponseEntity.ok().body(userRepository.save(user));
//    }


}

