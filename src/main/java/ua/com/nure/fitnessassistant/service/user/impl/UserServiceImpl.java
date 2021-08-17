package ua.com.nure.fitnessassistant.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.dto.user.request.CUUserDto;
import ua.com.nure.fitnessassistant.dto.user.response.UserDto;
import ua.com.nure.fitnessassistant.dto.user.response.UserPageResponse;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.program.ProgramRepository;
import ua.com.nure.fitnessassistant.repository.user.UserRepository;
import ua.com.nure.fitnessassistant.service.program.impl.ProgramServiceImpl;
import ua.com.nure.fitnessassistant.service.user.UserService;


import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProgramServiceImpl programServiceImpl;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProgramRepository programRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ProgramServiceImpl programServiceImpl, BCryptPasswordEncoder passwordEncoder, ProgramRepository programRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.programServiceImpl = programServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.programRepository = programRepository;
    }


    @Override
    public Page<User> getUsersPage(int pageNumber, int sizeOfPage,String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber,sizeOfPage, Sort.by(Sort.Order.asc(sortBy)));
        Page<User> result = userRepository.findAll(pageable);
        log.info("IN getAll: was found {}: pages, {}: users", result.getTotalPages(), result.getTotalElements());
        return result;
    }



    @Override
    public User findByUserName(String userName) {
        Optional<User> userDb = Optional.ofNullable(this.userRepository.findUserByUserName(userName));
        if (userDb.isPresent()) {
            User result = userDb.get();
            log.info("IN findByUsername - user: {} found by username: {}", result, userName);
            return (result);
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
    }

    @Override
    public void delete(UUID id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if (userDb.isPresent()) {
            this.userRepository.delete(userDb.get());
            log.info("IN delete - user with id: {} successfully deleted", id);
        } else {
            throw new CustomException("There is no User found with requested id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public User updateUser(UUID id, CUUserDto userDto) {
        Optional<User> userDb = this.userRepository.findById(id);
        if (userDb.isPresent()) {
            User userToUpdate = userDb.get();
            userToUpdate.setUserName(userDto.getUsername());
            userToUpdate.setGoal(userDto.getGoal());
            userToUpdate.setAge(userDto.getAge());
            userToUpdate.setEmail(userDto.getEmail());
            userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userToUpdate.setLastName(userDto.getLastName());
            userToUpdate.setFirstName(userDto.getFirstName());
            userRepository.save(userToUpdate);
            log.info("IN updateUser - user with id: {} successfully updated", id);
            return userToUpdate;
        } else {
            throw new CustomException("There is no user with id:" + id + " to update",
                    HttpStatus.NOT_FOUND);
        }
    }


    public UserDto fromUser(User user) {
        UserDto userDto = modelMapper.map(user,UserDto.class);
        userDto.setPrograms(
                user.getPrograms().stream()
                        .map(programServiceImpl::fromProgram)
                        .collect(Collectors.toSet())
        );
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public UserPageResponse fromPage(Page<UserDto> users){
        return modelMapper.map(users, UserPageResponse.class);
    }







}
