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
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.ExerciseRepository;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;
import ua.com.nure.fitnessassistant.service.program.impl.ProgramServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/program/")
@CrossOrigin
public class ProgramController {



    @Autowired
    ProgramRepository programRepository;

    @Autowired
    ProgramServiceImpl programService;

    @Autowired
    UserServiceSCRT userServiceSCRT;

    //TODO use Service to make a connection

    @Autowired
    ExerciseRepository exerciseRepository;





    @GetMapping
    public ResponseEntity<List<ProgramGetDto>> getAllPrograms() {
        List<Program> programs = this.programRepository.findAll();
        List<ProgramGetDto> programGetDtoList = programs
                .stream()
                .map(programService::fromProgram)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(programGetDtoList);
    }


    @PostMapping
    public ResponseEntity<ProgramGetDto> createProgram (@RequestBody ProgramCreateDto programCreateDto){
        Program program = new Program();
        program.setName(programCreateDto.getName());
        program.setCreated_by(userServiceSCRT.getCurrentLoggedInUser());
        program.setStatus(Status.ACTIVE);
        this.programService.createProgram(program);
        ProgramGetDto programGetDto = programService.fromProgram(program);
        return new  ResponseEntity<>(programGetDto, HttpStatus.CREATED);
    }


    @PutMapping("{programName}/{programAuthor}/exercises/{exerciseName}")
    ResponseEntity<ProgramGetDto> AddExeToProgram(
            @PathVariable String programName,
            @PathVariable String programAuthor,
            @PathVariable String exerciseName
    ){
        User loggedInUser = userServiceSCRT.getCurrentLoggedInUser();
        Optional<Program> programDb =  programRepository.findProgramByNameAndCreatedBy(programName,programAuthor);
        if (programDb.isEmpty()){
            throw new CustomException("There is no program found with requested data", HttpStatus.NOT_FOUND);
        }

        Program program = programDb.get();
        if(!program.getCreated_by().getUserName().equals(loggedInUser.getUserName())){
            throw new CustomException("You're not allowed to change this program", HttpStatus.FORBIDDEN);
        }
        Optional<Exercise> exerciseDb = exerciseRepository.findExercisesByName(exerciseName);
        if(exerciseDb.isEmpty()){
            throw new CustomException("There is no exercise found with requested data", HttpStatus.NOT_FOUND);
        }
        program.addExercise(exerciseDb.get());
        programRepository.save(program);
        return ResponseEntity.ok().body(programService.fromProgram(program));
    }

}
