package ua.com.nure.fitnessassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.dto.user.request.CUUserDto;
import ua.com.nure.fitnessassistant.dto.user.response.UserDto;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;
import ua.com.nure.fitnessassistant.service.program.impl.ProgramServiceImpl;
import ua.com.nure.fitnessassistant.service.user.impl.UserServiceImpl;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user/")
@CrossOrigin
public class UserController {


    private final UserServiceImpl userServiceImpl;
    private final ProgramRepository programRepository;
    private final UserServiceSCRT userServiceSCRT;
    private final PasswordEncoder passwordEncoder;
    private final ProgramServiceImpl programService;


    @Autowired
    public UserController(UserServiceImpl userServiceImpl, ProgramRepository programRepository, UserServiceSCRT userServiceSCRT, BCryptPasswordEncoder passwordEncoder, ProgramServiceImpl programService) {
        this.userServiceImpl = userServiceImpl;
        this.programRepository = programRepository;
        this.userServiceSCRT = userServiceSCRT;
        this.passwordEncoder = passwordEncoder;
        this.programService = programService;
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("myAccount")
    public ResponseEntity<UserDto> getCurrentUser() {
        User loggedInUser = userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
            UserDto result = this.userServiceImpl.fromUser(loggedInUser);
            return  ResponseEntity.ok().body(result);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("myAccount/myPrograms")
    public ResponseEntity<Set<ProgramGetDto>> getMyPrograms() {
        User loggedInUser = userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
        Set<Program> programs = this.programRepository.getMyPrograms(loggedInUser.getUserName());
        Set<ProgramGetDto> result = programs.
                stream()
                .map(programService::fromProgram)
                .collect(Collectors.toSet());
        return  ResponseEntity.ok().body(result);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("myAccount/update" )
    public ResponseEntity<UserDto> updateAccount(@RequestBody CUUserDto userDto) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
        User user = this.userServiceImpl.updateUser(loggedInUser.getId(),userDto);
        UserDto result = this.userServiceImpl.fromUser(user);
        return  ResponseEntity.ok().body(result);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("myAccount/delete/{password}" )
    public ResponseEntity deleteAccount(@PathVariable String password) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
        if(!passwordEncoder.encode(password).equals(loggedInUser.getPassword())){
            throw new CustomException("Wrong Credentials, try again", HttpStatus.FORBIDDEN);
        }
        this.userServiceImpl.delete(loggedInUser.getId());
        return  ResponseEntity.ok().body("Account Successfully deleted");
    }


//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PutMapping("addProgram/programName={programName}/programAuthor={programAuthor}")
//    ResponseEntity<UserDto> AddProgramToMyList(
//            @PathVariable String programName,
//            @PathVariable String programAuthor
//    ){
//        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
//        User user = this.userServiceImpl.addProgram(programName,loggedInUser, programAuthor);
//
//        return  new ResponseEntity<>(this.userServiceImpl.fromUser(user),HttpStatus.OK);
//    }


}

