package ua.com.nure.fitnessassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;


@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    Program findProgramByName(String name);

}
