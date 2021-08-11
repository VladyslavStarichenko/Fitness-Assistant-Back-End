package ua.com.nure.fitnessassistant.controller.exercise;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.repository.ExerciseRepository;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/exercise/")
@CrossOrigin
public class ExerciseController {


    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    ExerciseGetDto exerciseGetDto;

    @GetMapping
    public ResponseEntity<List<ExerciseGetDto>> getAllExercise () {
        List<Exercise> exercises = this.exerciseRepository.findAll();

        List<ExerciseGetDto> exerciseDtoList = exercises.stream()
                .map(exercise -> exerciseGetDto.fromExercise(exercise))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(exerciseDtoList);
    }

    @PostMapping
    public ResponseEntity<Exercise> createExercise (@RequestBody Exercise exercise){
        exercise.setStatus(Status.ACTIVE);
        return  ResponseEntity.ok().body(this.exerciseRepository.save(exercise));
    }



}
