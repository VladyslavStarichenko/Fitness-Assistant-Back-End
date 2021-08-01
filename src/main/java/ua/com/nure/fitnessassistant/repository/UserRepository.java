package ua.com.nure.fitnessassistant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.nure.fitnessassistant.model.user.User;

import java.awt.print.Pageable;
import java.util.UUID;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    User findUserByUserName(String name);

    boolean existsByUserName(String userName);

}
