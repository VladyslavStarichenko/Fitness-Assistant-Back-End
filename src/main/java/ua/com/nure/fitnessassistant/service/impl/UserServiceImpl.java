package ua.com.nure.fitnessassistant.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.user.Role;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.RoleRepository;
import ua.com.nure.fitnessassistant.repository.UserRepository;
import ua.com.nure.fitnessassistant.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);;
        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByUserName(String userName) {
        Optional<User> userDb = Optional.ofNullable(this.userRepository.findUserByUserName(userName));
        if(userDb.isPresent()){
            User result = userDb.get();
            log.info("IN findByUsername - user: {} found by username: {}", result, userName);
            return result;
        }else{
            throw new CustomException("There is no User found with requested name: " + userName,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public User findById(UUID id) {
//        Optional<User> userDb = this.userRepository.findById(id);
//        if (userDb.isPresent()) {
//            log.info("IN findById - user: {} found by id: {}", userDb,id);
//            return userDb.get();
//        }else{
//            log.warn("IN findById - no user found by id: {}", id);
//            throw new CustomException("There is no User found with request id: " + id,
//                    HttpStatus.NOT_FOUND);
//        }
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result);
        return result;
    }

    @Override
    public void delete(UUID id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if (userDb.isPresent()) {
            this.userRepository.delete(userDb.get());
            log.info("IN delete - user with id: {} successfully deleted",id);
        }else{
            throw new CustomException("There is no Category found with request id: " + id,
                    HttpStatus.NOT_FOUND);
        }

    }
}
