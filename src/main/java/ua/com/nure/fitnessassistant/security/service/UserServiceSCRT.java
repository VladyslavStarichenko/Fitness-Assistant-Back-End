package ua.com.nure.fitnessassistant.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.dto.user.request.AuthenticationDto;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.user.Role;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.RoleRepository;
import ua.com.nure.fitnessassistant.repository.UserRepository;
import ua.com.nure.fitnessassistant.security.jwt.JwtTokenProvider;
import ua.com.nure.fitnessassistant.service.user.UserService;

import java.util.*;

@Service
@Slf4j
public class UserServiceSCRT {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public UserServiceSCRT(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }


    public Map<Object,Object> signup(User user) {
        if (!userRepository.existsByUserName(user.getUserName())) {
            Role roleUser = roleRepository.findByName("ROLE_USER");
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(roleUser);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(userRoles);
            user.setStatus(Status.ACTIVE);
            User registeredUser = userRepository.save(user);
            log.info("IN register - user: {} successfully registered", registeredUser);

            String token =  jwtTokenProvider.createToken(user.getUserName(), user.getRoles());
            String userNameSignedIn =  user.getUserName();
            Map<Object, Object> response = new HashMap<>();
            response.put("username", userNameSignedIn);
            response.put("token", token);
            return response;
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public Map<Object, Object> signIn(AuthenticationDto requestDto) throws AuthenticationException {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,requestDto.getPassword()));
            User user = userRepository.findUserByUserName(username);

            if(user == null){
                throw new UsernameNotFoundException("User with username: " + username + "wasn't found");
            }
            log.info("IN signIn - user: {} successfully signedIN", userRepository.findUserByUserName(username));
            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return response;
        }catch (AuthenticationException exception){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User getCurrentLoggedInUser() {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByUserName(username);
    }



}
