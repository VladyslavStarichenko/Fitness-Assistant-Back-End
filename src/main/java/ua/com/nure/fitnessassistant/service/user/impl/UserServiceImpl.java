package ua.com.nure.fitnessassistant.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.RoleRepository;
import ua.com.nure.fitnessassistant.repository.UserRepository;
import ua.com.nure.fitnessassistant.service.user.UserService;


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
    public Page<User> getUsers(Pageable pageable) {
        Page<User> result = userRepository.findAll(pageable);
        log.info("IN getAll - {} users found", result.getTotalPages());
        return result;
    }
//    @Override
//    public Page<User> getUsers(Pageable pageable) {
//        return userRepository.findAll(pageable);
//    }


    @Override
    public User findByUserName(String userName) {
        Optional<User> userDb = Optional.ofNullable(this.userRepository.findUserByUserName(userName));
        if (userDb.isPresent()) {
            User result = userDb.get();
            log.info("IN findByUsername - user: {} found by username: {}", result, userName);
            return result;
        } else {
            throw new CustomException("There is no User found with requested name: " + userName,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public User findById(UUID id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if (userDb.isPresent()) {
            log.info("IN findById - user: {} found by id: {}", userDb,id);
            return userDb.get();
        }else{
            log.warn("IN findById - no user found by id: {}", id);
            throw new CustomException("There is no User found with request id: " + id,
                    HttpStatus.NOT_FOUND);
        }
//        User result = userRepository.findById(id).orElse(null);
//
//        if (result == null) {
//            log.warn("IN findById - no user found by id: {}", id);
//            return null;
//        }
//
//        log.info("IN findById - user: {} found by id: {}", result,result.getId());
//        return result;
    }

    @Override
    public void delete(UUID id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if (userDb.isPresent()) {
            this.userRepository.delete(userDb.get());
            log.info("IN delete - user with id: {} successfully deleted", id);
        } else {
            throw new CustomException("There is no User found with request id: " + id,
                    HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public User updateUser(User user) {
        Optional<User> userDb = this.userRepository.findById(user.getId());
        if (userDb.isPresent()) {
            User userToUpdate = userDb.get();
            userToUpdate.setId(user.getId());
            userToUpdate.setUserName(user.getUserName());
            userToUpdate.setGoal(user.getGoal());
            userToUpdate.setAge(user.getAge());
            userToUpdate.setMail(user.getMail());
            userToUpdate.setStatus(user.getStatus());
            userToUpdate.setRoles(user.getRoles());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setCreatedAt(user.getCreatedAt());
            userRepository.save(userToUpdate);
            return userToUpdate;
        } else {
            throw new CustomException("There is no user with id:" + user.getId() + " to update",
                    HttpStatus.NOT_FOUND);
        }
    }


}
