package ua.com.nure.fitnessassistant.service.user;

import org.springframework.data.domain.Page;
import ua.com.nure.fitnessassistant.dto.user.request.CUUserDto;
import ua.com.nure.fitnessassistant.dto.user.response.UserDto;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;


import java.util.UUID;

public interface UserService {


    Page<User> getUsersPage(int pageNumber, int sizeOfPage,String sortBy);

    User findByUserName(String userName);

    User findById(UUID id);

    void delete(UUID id);

    User updateUser(UUID id, CUUserDto userDto);








}
