package ua.com.nure.fitnessassistant.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramPageResponse;
import ua.com.nure.fitnessassistant.dto.user.request.CUUserDto;
import ua.com.nure.fitnessassistant.dto.user.response.UserDto;
import ua.com.nure.fitnessassistant.dto.user.response.UserPageResponse;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.program.ProgramRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;
import ua.com.nure.fitnessassistant.service.program.impl.ProgramServiceImpl;
import ua.com.nure.fitnessassistant.service.user.impl.UserServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user/")
@CrossOrigin
public class UserController {


    private final UserServiceImpl userServiceImpl;
    private final UserServiceSCRT userServiceSCRT;
    private final PasswordEncoder passwordEncoder;
    private final ProgramServiceImpl programServiceImpl;
    private final ProgramRepository programRepository;


    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserServiceSCRT userServiceSCRT, BCryptPasswordEncoder passwordEncoder, ProgramServiceImpl programServiceImpl, ProgramRepository programRepository) {
        this.userServiceImpl = userServiceImpl;
        this.userServiceSCRT = userServiceSCRT;
        this.passwordEncoder = passwordEncoder;
        this.programServiceImpl = programServiceImpl;
        this.programRepository = programRepository;
    }



    @GetMapping("myAccount")
    public ResponseEntity<UserDto> getCurrentUser() {
        User loggedInUser = userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
            UserDto result = this.userServiceImpl.fromUser(loggedInUser);
            return  ResponseEntity.ok().body(result);

    }


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


    @GetMapping("myPrograms/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<ProgramPageResponse> getMyPrograms(
            @PathVariable int pageNumber,
            @PathVariable int pageSize,
            @PathVariable String sortBy
    ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }


        Set<Program> programsSet = this.programRepository.getMyPrograms(loggedInUser.getUserName());


        Page<ProgramGetDto> page = new PageImpl<>(programsSet
                .stream()
                .map(programServiceImpl::fromProgram)
                .collect(Collectors.toList()));

        List<ProgramGetDto> programsList = page.toList();
        ProgramPageResponse response = programServiceImpl.fromPage(page, programsList, sortBy);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements(programsSet.size());
        response.setTotalPages(programsSet.size()/pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
        User user = userServiceImpl.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = userServiceImpl.fromUser(user);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }


    @GetMapping("users/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<UserPageResponse> getUsers(@PathVariable int pageNumber,
                                                     @PathVariable int pageSize,
                                                     @PathVariable String sortBy){

        Page<User> userPage = userServiceImpl.getUsersPage(pageNumber,pageSize,sortBy);
        Page<UserDto> page = new PageImpl<>(userPage.stream()
                .map(userServiceImpl::fromUser)
                .collect(Collectors.toList()),userPage.getPageable(),userPage.getTotalElements());

        UserPageResponse response = userServiceImpl.fromPage(page);
        response.setUsers(page.getContent());
        response.setSortedBy(sortBy);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}

