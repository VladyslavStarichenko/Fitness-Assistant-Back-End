package ua.com.nure.fitnessassistant.repository.program;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;

import java.util.Optional;
import java.util.Set;


@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    Optional<Program> findProgramByName(String name);


    @Query("FROM Program p WHERE p.name = ?1 AND p.created_by IN (SELECT u.id FROM User u " +
            "WHERE u.userName =?2 ) ")
    Optional<Program> findProgramByNameAndCreatedBy(String name, String userName);


    @Query("FROM Program p WHERE p.created_by IN (SELECT u.id FROM User u" +
            " WHERE u.userName =?1 )")
    Set<Program> getMyPrograms(String userName);




}
