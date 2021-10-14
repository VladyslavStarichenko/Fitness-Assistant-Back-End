package ua.com.nure.fitnessassistant.repository.program;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Role;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.exercise.ExerciseRepository;
import ua.com.nure.fitnessassistant.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;


    @AfterEach
    void tearDown() {
        programRepository.deleteAll();
    }

    @Test
    void findProgramByName() {
        //given
        String name = "squats";
        Program program = new Program();
        program.setName(name);
        programRepository.save(program);
        //when
        Optional<Program> programDb = programRepository.findProgramByName(name);
        //then
        programDb.ifPresent(exercise1 -> assertThat(exercise1.getName().equals(name)).isTrue());
    }

    @Test
    void findProgramByNameAndCreatedBy() {
        //given

        List<Role> roles = new ArrayList<>();
        Role role =  Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        User user = User.builder()
                .firstName("Vlad")
                .lastName("Starichenko")
                .userName("goldsilver")
                .email("test@nure.ua")
                .password("test")
                .roles(roles)
                .build();
        user.setStatus(Status.ACTIVE);

        String name = "squats";
        Program program = new Program();
        program.setName(name);
        programRepository.save(program);
        when(program.getCreated_by()).thenReturn(user);
        //when

    }

    @Test
    void getMyPrograms() {
    }
}