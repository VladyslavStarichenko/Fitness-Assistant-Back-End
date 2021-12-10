package ua.com.nure.fitnessassistant.repository.program;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query(value = " FROM Program p WHERE p.name = ?1 AND p.created_by = ?2")
    Optional<Program> findProgramByNameAndCreatedBy(String name, String userName);


    @Query(value = "SELECT * FROM programs p WHERE created_by =?", nativeQuery = true)
    Set<Program> getMyPrograms(String userName);

    @Query(value = "Select * FROM programs p WHERE p.id IN (SELECT up.program_id FROM user_program up WHERE user_id IN(SELECT u.id FROM users u WHERE u.user_name = ?) ) ", nativeQuery = true)
    Page<Program> findProgramsByUserName(Pageable pageable, String userName);




}
