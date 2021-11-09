package ua.com.nure.fitnessassistant.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.program.request.ProgramAddDto;
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
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user/")
@Api(value = "Operations with users")
@CrossOrigin( origins = "*",
        methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class UserController {


    private final UserServiceImpl userServiceImpl;
    private final UserServiceSCRT userServiceSCRT;
    private final PasswordEncoder passwordEncoder;
    private final ProgramServiceImpl programServiceImpl;


    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserServiceSCRT userServiceSCRT, BCryptPasswordEncoder passwordEncoder, ProgramServiceImpl programServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.userServiceSCRT = userServiceSCRT;
        this.passwordEncoder = passwordEncoder;
        this.programServiceImpl = programServiceImpl;
    }


    @ApiOperation(value = "Get current logged in user account")
    @GetMapping("myAccount")
    public ResponseEntity<UserDto> getCurrentUser() {
        User loggedInUser = userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
        UserDto result = this.userServiceImpl.fromUser(loggedInUser);
        return ResponseEntity.ok().body(result);
    }


    @ApiOperation(value = "Update my account")
    @PutMapping("myAccount/update")
    public ResponseEntity<UserDto> updateAccount(@ApiParam(value = "User object to update") @RequestBody CUUserDto userDto) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
        User user = this.userServiceImpl.updateUser(loggedInUser.getId(), userDto);
        UserDto result = this.userServiceImpl.fromUser(user);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "Delete current logged in user account")
    @DeleteMapping("myAccount/delete/{password}")
    public ResponseEntity deleteAccount(@ApiParam(value = "Password to confirm deleting") @PathVariable String password) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }
        if (!passwordEncoder.encode(password).equals(loggedInUser.getPassword())) {
            throw new CustomException("Wrong Credentials, try again", HttpStatus.FORBIDDEN);
        }
        this.userServiceImpl.delete(loggedInUser.getId());
        return ResponseEntity.ok().body("Account Successfully deleted");
    }

    @ApiOperation(value = "Get current logged in user programs")
    @GetMapping("myPrograms/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<ProgramPageResponse> getMyPrograms(
            @ApiParam(value = "Page number to show") @PathVariable int pageNumber,
            @ApiParam(value = "Page size") @PathVariable int pageSize,
            @ApiParam(value = "Sort information by parameter") @PathVariable(required = false) String sortBy
    ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        if (loggedInUser == null) {
            throw new CustomException("You should authorize firstly", HttpStatus.UNAUTHORIZED);
        }


        Page<Program> programs = this.programServiceImpl.getPrograms(pageNumber, pageSize, sortBy);
        Page<ProgramGetDto> page = new PageImpl<>(programs
                .stream()
                .map(programServiceImpl::fromProgram)
                .collect(Collectors.toList()));
        List<ProgramGetDto> programsList = page.toList();

        ProgramPageResponse response = programServiceImpl.fromPage(page, programsList, sortBy);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements((int) programs.getTotalElements());
        response.setTotalPages(programs.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @ApiOperation(value = "Get user by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("users/id={id}")
    public ResponseEntity<UserDto> getUserById(@ApiParam(value = "User id to search") @PathVariable UUID id) {
        User user = userServiceImpl.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = userServiceImpl.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Get user by name")
    @GetMapping("users/userName={userName}")
    public ResponseEntity<UserDto> getUserByName(@ApiParam(value = "User name to search") @PathVariable(name = "userName") String userName) {
        User user = userServiceImpl.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = userServiceImpl.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ApiOperation(value = "Get all users")
    @GetMapping("users/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<UserPageResponse> getUsers(@ApiParam(value = "Page number to show") @PathVariable int pageNumber,
                                                     @ApiParam(value = "Page size") @PathVariable int pageSize,
                                                     @ApiParam(value = "Sort information by parameter") @PathVariable String sortBy) {
        Page<User> userPage = userServiceImpl.getUsersPage(pageNumber, pageSize, sortBy);
        Page<UserDto> page = new PageImpl<>(userPage.stream()
                .map(userServiceImpl::fromUser)
                .collect(Collectors.toList()), userPage.getPageable(), userPage.getTotalElements());
        UserPageResponse response = userServiceImpl.fromPage(page);
        response.setUsers(page.getContent());
        response.setSortedBy(sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}