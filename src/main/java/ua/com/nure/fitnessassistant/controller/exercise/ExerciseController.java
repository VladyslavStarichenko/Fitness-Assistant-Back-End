package ua.com.nure.fitnessassistant.controller.exercise;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ua.com.nure.fitnessassistant.dto.exercise.request.CreateExerciseDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExercisePageResponse;

import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.service.exercise.impl.ExerciseServiceImpl;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/exercise/")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = "http://localhost:3000",
        methods = {RequestMethod.GET,RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true",maxAge = 3600, allowedHeaders = "*")
public class ExerciseController {

    private final ExerciseServiceImpl exerciseServiceImpl;


    @Autowired
    public ExerciseController(ExerciseServiceImpl exerciseServiceImpl) {
        this.exerciseServiceImpl = exerciseServiceImpl;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Create new exercise")
    @PostMapping
    public ResponseEntity<ExerciseGetDto> createExercise(@ApiParam(value = "Exercise object") @RequestBody CreateExerciseDto createExerciseDto) {

        Exercise exercise = new Exercise();
        exercise.setName(createExerciseDto.getName());
        exercise.setStatus(Status.ACTIVE);
        this.exerciseServiceImpl.createExercise(exercise);
        return ResponseEntity.ok().body(this.exerciseServiceImpl.fromExercise(exercise));
    }

    @ApiOperation(value = "Get all exercises")
    @GetMapping("exercises/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<ExercisePageResponse> getAllExercise(@ApiParam(value = "Page number to show") @PathVariable int pageNumber,
                                                               @ApiParam(value = "Page size") @PathVariable int pageSize,
                                                               @ApiParam(value = "Sort information by parameter") @RequestParam(required = false) @PathVariable() String sortBy
    ) {
        Page<Exercise> exercises = this.exerciseServiceImpl.getAllExercises(pageNumber, pageSize, sortBy);
        Page<ExerciseGetDto> page = new PageImpl<>(exercises.stream()
                .map(exerciseServiceImpl::fromExercise)
                .collect(Collectors.toList()), exercises.getPageable(), exercises.getTotalElements());

        Set<String> names = exerciseServiceImpl.getNames(page);
        ExercisePageResponse result = exerciseServiceImpl.fromExercisePage(page, names, sortBy);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Update exercise by name")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{exerciseName}")
    public ResponseEntity<ExerciseGetDto> updateExercise(@ApiParam(value = "New exercise object") @RequestBody CreateExerciseDto exerciseDto,
                                                         @ApiParam(value = "Exercise name to update ") @PathVariable String exerciseName) {
        Exercise exercise = exerciseServiceImpl
                .updateExercise(exerciseServiceImpl
                        .toExercise(exerciseDto), exerciseName);
        return new ResponseEntity<>(exerciseServiceImpl.fromExercise(exercise), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete exercise by name")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteExercise(@ApiParam(value = "Exercise name to delete") @PathVariable String name) {
        exerciseServiceImpl.delete(name);
        return new ResponseEntity<>("Exercise Successfully deleted", HttpStatus.OK);
    }

    @ApiOperation(value = "Get exercise by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<ExerciseGetDto> getExerciseById(@ApiParam(value = "Exercise id") @PathVariable Long id) {
        return new ResponseEntity<>(exerciseServiceImpl.fromExercise(exerciseServiceImpl.findById(id)), HttpStatus.OK);
    }

    @ApiOperation(value = "Get exercise by name")
    @GetMapping("/name/{name}")
    public ResponseEntity<ExerciseGetDto> getExerciseByName(@ApiParam(value = "Exercise name search") @PathVariable String name) {
        return new ResponseEntity<>(exerciseServiceImpl.fromExercise(exerciseServiceImpl.findExerciseByName(name)), HttpStatus.OK);
    }


}