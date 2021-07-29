package ua.com.nure.fitnessassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nure.fitnessassistant.model.user.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByUserName(String name);
}
