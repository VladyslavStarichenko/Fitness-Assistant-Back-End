package ua.com.nure.fitnessassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nure.fitnessassistant.model.user.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
