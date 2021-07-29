package ua.com.nure.fitnessassistant.service;

import ua.com.nure.fitnessassistant.model.user.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUserName(String userName);

    User findById(UUID id);

    void delete(UUID id);
}
