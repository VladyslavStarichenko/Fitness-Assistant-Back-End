package ua.com.nure.fitnessassistant.controller.exercise;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ua.com.nure.fitnessassistant.dto.exercise.request.CreateExerciseDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExercisePageResponse;
import ua.com.nure.fitnessassistant.dto.user.request.CUUserDto;
import ua.com.nure.fitnessassistant.dto.user.response.UserDto;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.ExerciseRepository;
import ua.com.nure.fitnessassistant.service.exercise.impl.ExerciseServiceImpl;

import java.awt.*;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/exercise/")
@CrossOrigin
public class ExerciseController {


    private final ExerciseServiceImpl exerciseServiceImpl;

    @Autowired
    public ExerciseController(ExerciseServiceImpl exerciseServiceImpl) {
        this.exerciseServiceImpl = exerciseServiceImpl;
    }


    @PostMapping
    public ResponseEntity<ExerciseGetDto> createExercise(@RequestBody CreateExerciseDto createExerciseDto) {
        Exercise exercise = new Exercise();
        exercise.setName(createExerciseDto.getName());
        exercise.setStatus(Status.ACTIVE);
        this.exerciseServiceImpl.createExercise(exercise);
        return ResponseEntity.ok().body(this.exerciseServiceImpl.fromExercise(exercise));
    }

    @GetMapping("exercises/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<ExercisePageResponse> getAllExercise (@PathVariable int pageNumber,
                                                                @PathVariable int pageSize,
                                                                @PathVariable String sortBy
    ) {
        Page<Exercise> exercises = this.exerciseServiceImpl.getAllExercises(pageNumber,pageSize,sortBy);
        Page<ExerciseGetDto> page = new PageImpl<>(exercises.stream()
                .map(exerciseServiceImpl::fromExercise)
                .collect(Collectors.toList()),exercises.getPageable(),exercises.getTotalElements());

        Set<String> names = exerciseServiceImpl.getNames(page);
        ExercisePageResponse result = exerciseServiceImpl.fromExercisePage(page,names,sortBy);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PutMapping("/{exerciseName}" )
    public ResponseEntity<ExerciseGetDto> updateExercise(@RequestBody CreateExerciseDto exerciseDto,
                                                  @PathVariable String exerciseName) {

        Exercise exercise = exerciseServiceImpl
                .updateExercise(exerciseServiceImpl
                        .toExercise(exerciseDto),exerciseName);
        return new ResponseEntity<>(exerciseServiceImpl.fromExercise(exercise),HttpStatus.OK);
    }



    @DeleteMapping("/{name}" )
    public ResponseEntity<String> deleteAccount(@PathVariable String name) {
        exerciseServiceImpl.delete(name);
        return new ResponseEntity<>("Exercise Successfully deleted",HttpStatus.OK);
    }


}
