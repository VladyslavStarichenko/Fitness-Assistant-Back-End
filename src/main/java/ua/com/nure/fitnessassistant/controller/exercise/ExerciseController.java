package ua.com.nure.fitnessassistant.controller.exercise;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.repository.ExerciseRepository;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercise/")
@CrossOrigin
public class ExerciseController {




    @Autowired
    ExerciseRepository exerciseRepository;

    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercise () {

        return ResponseEntity.ok().body(this.exerciseRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Exercise> createExercise (@RequestBody Exercise exercise){
        exercise.setStatus(Status.ACTIVE);
        return  ResponseEntity.ok().body(this.exerciseRepository.save(exercise));
    }



}
