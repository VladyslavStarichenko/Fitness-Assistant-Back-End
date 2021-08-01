package ua.com.nure.fitnessassistant.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.nure.fitnessassistant.model.user.User;


import java.util.UUID;

public interface UserService {


    Page<User> getUsers(Pageable pageable);

    User findByUserName(String userName);

    User findById(UUID id);

    void delete(UUID id);

    User updateUser(User user);


}
