package ua.com.nure.fitnessassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;


@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
