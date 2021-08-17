package ua.com.nure.fitnessassistant.repository.role;


import org.springframework.data.repository.PagingAndSortingRepository;
import ua.com.nure.fitnessassistant.model.user.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role,Long> {
    Role findByName(String name);
}
