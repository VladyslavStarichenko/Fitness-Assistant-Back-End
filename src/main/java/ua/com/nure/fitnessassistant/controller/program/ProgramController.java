package ua.com.nure.fitnessassistant.controller.program;


import liquibase.pro.packaged.C;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;
import ua.com.nure.fitnessassistant.dto.program.request.ProgramCreateDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.repository.ExerciseRepository;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


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

    @Autowired
    ProgramGetDto programGetDtoInstance;

    @Autowired
    ExerciseGetDto exerciseGetDto;

    @GetMapping
    public ResponseEntity<List<ProgramGetDto>> getAllPrograms() {
        List<Program> programs = this.programRepository.findAll();
        List<ProgramGetDto> programGetDtoList = programs
                .stream()
                .map(program -> programGetDtoInstance.fromProgram(program))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(programGetDtoList);
    }

    @PostMapping
    public ResponseEntity<ProgramGetDto> createProgram (@RequestBody ProgramCreateDto programCreateDto){
        Program program = new Program();
        program.setName(programCreateDto.getName());
        program.setCreated_by(userServiceSCRT.getCurrentLoggedInUser());
        program.setStatus(Status.ACTIVE);
        this.programRepository.save(program);
        ProgramGetDto programGetDto = programGetDtoInstance.fromProgram(program);

        return new  ResponseEntity<>(programGetDto, HttpStatus.CREATED);
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
