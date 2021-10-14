package ua.com.nure.fitnessassistant.repository.exericise;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.repository.exercise.ExerciseRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @AfterEach
    void tearDown() {
        exerciseRepository.deleteAll();
    }


    @Test
    void itShouldCheckIfExerciseExistsByName() {
        //given
        String name = "squats";
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exerciseRepository.save(exercise);
        //when
        boolean expected = exerciseRepository.existsByName(name);
        //then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfExerciseIsNotExistsByName() {
        //given
        String name = "squats";
        //when
        boolean expected = exerciseRepository.existsByName(name);
        //then
        assertThat(expected).isFalse();
    }

    @Test
    void  findExercisesByName() {
        //given
        String name = "squats";
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exerciseRepository.save(exercise);
        //when
        Optional<Exercise> exerciseDb = exerciseRepository.findExercisesByName(name);
        //then
        exerciseDb.ifPresent(exercise1 -> assertThat(exercise1.getName().equals(name)).isTrue());
    }

    @Test
    void  ifNotFindExercisesByName() {
        //given
        String name = "squats";
        //when
        Optional<Exercise> exerciseDb = exerciseRepository.findExercisesByName(name);
        //then
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            exerciseDb.get();
        });

    }
}