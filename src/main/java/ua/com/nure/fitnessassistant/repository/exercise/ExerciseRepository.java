package ua.com.nure.fitnessassistant.repository.exercise;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;

import java.util.Optional;


@Repository
public interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {

    Optional<Exercise> findExercisesByName(String name);

    boolean existsByName(String name);
}
