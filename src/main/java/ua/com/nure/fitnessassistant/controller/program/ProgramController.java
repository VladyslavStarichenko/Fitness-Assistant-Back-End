package ua.com.nure.fitnessassistant.controller.program;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.repository.ExerciseRepository;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;

import java.util.List;


@RestController
@RequestMapping("/api/v1/program/")
@CrossOrigin
public class ProgramController {

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    UserServiceSCRT userServiceSCRT;

    //TODO use Service to make a connection

    @Autowired
    ExerciseRepository exerciseRepository;

    @GetMapping
    public ResponseEntity<List<Program>> getAllPrograms() {
        return ResponseEntity.ok().body(this.programRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Program> createProgram (@RequestBody Program program){
        program.setCreated_by(userServiceSCRT.getCurrentLoggedInUser());
        program.setStatus(Status.ACTIVE);
        return  ResponseEntity.ok().body(this.programRepository.save(program));
    }

    @PutMapping("/{programId}/exercises/{exerciseId}")
    ResponseEntity<Program> AddExeToProgram(
            @PathVariable Long programId,
            @PathVariable Long exerciseId
    ){
        //TODO add checking for Optional
        Program program =  programRepository.findById(programId).get();
        Exercise exercise = exerciseRepository.findById(exerciseId).get();
        program.addExercise(exercise);

        return ResponseEntity.ok().body(programRepository.save(program));
    }

}
