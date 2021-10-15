package ua.com.nure.fitnessassistant.controller.program;


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

import ua.com.nure.fitnessassistant.dto.program.request.ProgramCreateDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramPageResponse;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.program.ProgramRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;
import ua.com.nure.fitnessassistant.service.exercise.impl.ExerciseServiceImpl;
import ua.com.nure.fitnessassistant.service.program.impl.ProgramServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/program/")
@Api(value = "Operations with programs")
@CrossOrigin
public class ProgramController {


    private final ProgramRepository programRepository;
    private final ProgramServiceImpl programServiceImpl;
    private final UserServiceSCRT userServiceSCRT;
    private final ExerciseServiceImpl exerciseServiceImpl;


    @Autowired
    public ProgramController(ProgramRepository programRepository, ProgramServiceImpl programServiceImpl, UserServiceSCRT userServiceSCRT, ExerciseServiceImpl exerciseServiceImpl) {
        this.programRepository = programRepository;
        this.programServiceImpl = programServiceImpl;
        this.userServiceSCRT = userServiceSCRT;
        this.exerciseServiceImpl = exerciseServiceImpl;
    }

    @ApiOperation(value = "Get program by name")
    @GetMapping("name/{name}")
    public ResponseEntity<ProgramGetDto> getProgramByName(@ApiParam(value = "Program name to search") @PathVariable String name) {
        return new ResponseEntity<>(programServiceImpl.fromProgram(programServiceImpl.findByProgramByName(name)), HttpStatus.OK);
    }

    @ApiOperation(value = "Get program by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("id/{id}")
    public ResponseEntity<ProgramGetDto> getProgramById(@ApiParam(value = "Program id to search") @PathVariable Long id) {
        return new ResponseEntity<>(programServiceImpl.fromProgram(programServiceImpl.findById(id)), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all programs")
    @GetMapping("programs/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<ProgramPageResponse> getAllPrograms(
            @ApiParam(value = "Page number to show") @PathVariable int pageNumber,
            @ApiParam(value = "Page size") @PathVariable int pageSize,
            @ApiParam(value = "Sort information by parameter") @PathVariable(required = false) String sortBy
    ) {
        Page<Program> programs = this.programServiceImpl.getPrograms(pageNumber, pageSize, sortBy);
        Page<ProgramGetDto> page = new PageImpl<>(programs
                .stream()
                .map(programServiceImpl::fromProgram)
                .collect(Collectors.toList()));

        List<ProgramGetDto> programsList = page.toList();
        ProgramPageResponse response = programServiceImpl.fromPage(page, programsList, sortBy);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements((int) programs.getTotalElements());
        response.setTotalPages(programs.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all programs by user name")
    @GetMapping("programs/user={userName}/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<ProgramPageResponse> getUserPrograms(
            @ApiParam(value = "User name to search") @PathVariable String userName,
            @ApiParam(value = "Page number to show") @PathVariable int pageNumber,
            @ApiParam(value = "Page size") @PathVariable int pageSize,
            @ApiParam(value = "Sort information by parameter") @PathVariable String sortBy
    ) {
        Set<Program> programsSet = this.programRepository.getMyPrograms(userName);
        Page<ProgramGetDto> page = getProgramGetDtoPage(programsSet);
        List<ProgramGetDto> programsList = page.toList();
        ProgramPageResponse response = programServiceImpl.fromPage(page, programsList, sortBy);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements(programsSet.size());
        response.setTotalPages(programsSet.size() / pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Page<ProgramGetDto> getProgramGetDtoPage(Set<Program> programsSet) {
        return new PageImpl<>(programsSet
                .stream()
                .map(programServiceImpl::fromProgram)
                .collect(Collectors.toList()));
    }

    @ApiOperation(value = "Create new program")
    @PostMapping
    public ResponseEntity<ProgramGetDto> createProgram(@ApiParam(value = "Program object to create") @RequestBody ProgramCreateDto programCreateDto) {
        Program program = new Program();
        program.setName(programCreateDto.getName());
        program.setCreated_by(this.userServiceSCRT.getCurrentLoggedInUser());
        program.setStatus(Status.ACTIVE);
        this.programServiceImpl.createProgram(program);
        ProgramGetDto programGetDto = programServiceImpl.fromProgram(program);
        return new ResponseEntity<>(programGetDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add exercise to the program")
    @PutMapping("add/{programName}/exercises/{exerciseName}")
    ResponseEntity<ProgramGetDto> AddExercise(
            @ApiParam(value = "Program name to add exercise") @PathVariable String programName,
            @ApiParam(value = "Exercise to add") @PathVariable String exerciseName
    ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        Optional<Program> programDb = programRepository.findProgramByNameAndCreatedBy(programName, loggedInUser.getUserName());
        if (!programDb.isPresent()) {
            throw new CustomException("There is no program found with requested data", HttpStatus.NOT_FOUND);
        }

        Program program = programDb.get();
        if (!program.getCreated_by().getUserName().equals(loggedInUser.getUserName())) {
            throw new CustomException("You're not allowed to change this program", HttpStatus.FORBIDDEN);
        }
        Exercise exerciseDb = exerciseServiceImpl.findExerciseByName(exerciseName);

        program.addExercise(exerciseDb);
        programRepository.save(program);
        return ResponseEntity.ok().body(programServiceImpl.fromProgram(program));
    }

    @ApiOperation(value = "Remove exercise from the program")
    @PutMapping("remove/{programName}/exercises/{exerciseName}")
    ResponseEntity<ProgramGetDto> removeExercise(
            @ApiParam(value = "Program name to remove exercise from") @PathVariable String programName,
            @ApiParam(value = "Exercise to remove") @PathVariable String exerciseName
    ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        Optional<Program> programDb = programRepository.findProgramByNameAndCreatedBy(programName, loggedInUser.getUserName());
        if (!programDb.isPresent()) {
            throw new CustomException("There is no program found with requested data", HttpStatus.NOT_FOUND);
        }
        Program program = programDb.get();
        if (!program.getCreated_by().getUserName().equals(loggedInUser.getUserName())) {
            throw new CustomException("You're not allowed to change this program", HttpStatus.FORBIDDEN);
        }
        Exercise exerciseDb = exerciseServiceImpl.findExerciseByName(exerciseName);

        program.removeExercise(exerciseDb);
        programRepository.save(program);
        return ResponseEntity.ok().body(programServiceImpl.fromProgram(program));
    }

    @ApiOperation(value = "Update program name")
    @PutMapping("update/{programName}/{newProgramName}")
    ResponseEntity<ProgramGetDto> updateName(
            @ApiParam(value = "Program name to update") @PathVariable String programName,
            @ApiParam(value = "New program name") @PathVariable String newProgramName
    ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        Program program = programServiceImpl.updateProgram(programName, newProgramName, loggedInUser);
        return new ResponseEntity<>(programServiceImpl.fromProgram(program), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete program by name")
    @DeleteMapping("/{programName}")
    ResponseEntity<String> deleteProgram(
            @ApiParam(value = "Program name to delete") @PathVariable String programName
    ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        programServiceImpl.delete(programName, loggedInUser);
        return new ResponseEntity<>("Program: " + programName + "was successfully deleted", HttpStatus.OK);
    }

}
