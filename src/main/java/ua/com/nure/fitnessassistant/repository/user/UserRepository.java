package ua.com.nure.fitnessassistant.repository.user;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.nure.fitnessassistant.model.user.User;
import java.util.UUID;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    User findUserByUserName(String name);

    boolean existsByUserName(String userName);



}
