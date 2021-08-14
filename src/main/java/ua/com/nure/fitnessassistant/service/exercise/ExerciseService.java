package ua.com.nure.fitnessassistant.service.exercise;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.nure.fitnessassistant.dto.exercise.request.CreateExerciseDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExercisePageResponse;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;

import java.util.Set;


public interface ExerciseService {

    Exercise createExercise (Exercise exercise);

    Page<Exercise> getAllExercises(int pageNumber, int sizeOfPage,String sortBy);

    Exercise findById(Long id);

    Exercise findByExerciseByName(String name);

    void delete(String name);

    Exercise updateExercise(Exercise exerciseUpdate, String name);

    ExerciseGetDto fromExercise(Exercise exercise);

    Exercise toExercise(CreateExerciseDto exerciseGetDto);

    ExercisePageResponse fromExercisePage (Page<ExerciseGetDto> page, Set<String> names, String sortedBy);

    Set<String> getNames(Page<ExerciseGetDto> names);



}
