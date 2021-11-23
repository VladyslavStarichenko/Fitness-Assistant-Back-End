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

import ua.com.nure.fitnessassistant.dto.exercise.request.AssignExerciseDto;
import ua.com.nure.fitnessassistant.dto.program.request.ProgramAddDto;
import ua.com.nure.fitnessassistant.dto.program.request.ProgramCreateDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramPageResponse;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.program.ProgramType;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.program.ProgramRepository;
import ua.com.nure.fitnessassistant.security.service.UserServiceSCRT;
import ua.com.nure.fitnessassistant.service.exercise.impl.ExerciseServiceImpl;
import ua.com.nure.fitnessassistant.service.program.impl.ProgramServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/program/")
@Api(value = "Operations with programs")
@CrossOrigin(origins = "*",
        methods = {RequestMethod.GET,RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true",maxAge = 3600)
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
            @ApiParam(value = "Sort information by parameter") @RequestParam(required = false) @PathVariable(required = false) String sortBy
    ) {
        Page<Program> programs = this.programServiceImpl.getPrograms(pageNumber, pageSize, sortBy);
        Page<ProgramGetDto> page = new PageImpl<>(programs
                .stream()
                .map(programServiceImpl::fromProgram)
                .collect(Collectors.toList()));
        List<ProgramGetDto> programsList = page.toList();

        List<ProgramGetDto> resultList = programsList.stream()
                .filter(ProgramGetDto::isPublic)
                .collect(Collectors.toList());
        ProgramPageResponse response = programServiceImpl.fromPage(page, resultList, sortBy);
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
        Page<Program> programs = this.programServiceImpl.getPrograms(pageNumber, pageSize, sortBy);
        Page<ProgramGetDto> page = new PageImpl<>(programs
                .stream()
                .map(programServiceImpl::fromProgram)
                .collect(Collectors.toList()));
        List<ProgramGetDto> programsList = page.toList();
        List<ProgramGetDto> resultList = programsList.stream()
                .filter(p -> p.getCreated_by().equals(userName))
                .filter(ProgramGetDto::isPublic)
        .collect(Collectors.toList());
        ProgramPageResponse response = programServiceImpl.fromPage(page, resultList, sortBy);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements(resultList.size());
        response.setTotalPages(resultList.size()/pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @ApiOperation(value = "Create new program")
    @PostMapping
    public ResponseEntity<ProgramGetDto> createProgram(@ApiParam(value = "Program object to create") @RequestBody ProgramCreateDto programCreateDto) {

        Program program = new Program();
        program.setName(programCreateDto.getName());
        program.setCreated_by(this.userServiceSCRT.getCurrentLoggedInUser().getUserName());
        program.setProgramType(ProgramType.valueOf(programCreateDto.getProgramType()));
        program.setStatus(Status.ACTIVE);
        program.setPublic(programCreateDto.isPublic());
        this.programServiceImpl.createProgram(program, userServiceSCRT.getCurrentLoggedInUser());
        ProgramGetDto programGetDto = programServiceImpl.fromProgram(program);
        return new ResponseEntity<>(programGetDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add program")
    @PostMapping ("addProgramToList")
    public ResponseEntity<ProgramGetDto> addProgram(@ApiParam(value = "Program object to create") @RequestBody ProgramAddDto programAddDto) {

        Optional<Program> program = programRepository.findProgramByNameAndCreatedBy(programAddDto.getName(), programAddDto.getAuthor());

        if(program.isPresent()){
            Program program1 = program.get();
            programServiceImpl.addProgramToList(program1,userServiceSCRT.getCurrentLoggedInUser());
            ProgramGetDto programGetDto = programServiceImpl.fromProgram(program.get());
            return new ResponseEntity<>(programGetDto, HttpStatus.CREATED);
        }else {
            throw new CustomException("Faild to add Programm", HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value = "Remove program")
    @PostMapping ("removeProgramFromList")
    public ResponseEntity<ProgramGetDto> removeProgram(@ApiParam(value = "Program object to create") @RequestBody ProgramAddDto programAddDto) {

        Optional<Program> program = programRepository.findProgramByNameAndCreatedBy(programAddDto.getName(), programAddDto.getAuthor());

        if(program.isPresent()){
            Program program1 = program.get();
            programServiceImpl.removeProgramFromList(program1,userServiceSCRT.getCurrentLoggedInUser());
            ProgramGetDto programGetDto = programServiceImpl.fromProgram(program.get());
            return new ResponseEntity<>(programGetDto, HttpStatus.CREATED);
        }else {
            throw new CustomException("Faild to add Programm", HttpStatus.BAD_REQUEST);
        }

    }



    @ApiOperation(value = "Add exercise to the program")
    @PutMapping("add/{programName}/exercises/")
    ResponseEntity<ProgramGetDto> AddExercise(
            @PathVariable String programName,
            @RequestBody AssignExerciseDto exerciseDto
            ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        Optional<Program> programDb = programRepository.findProgramByNameAndCreatedBy(programName, loggedInUser.getUserName());
        if (!programDb.isPresent()) {
            throw new CustomException("There is no program found with requested data", HttpStatus.NOT_FOUND);
        }

        Program program = programDb.get();
        if (!program.getCreated_by().equals(loggedInUser.getUserName())) {
            throw new CustomException("You're not allowed to change this program", HttpStatus.FORBIDDEN);
        }

        exerciseDto.getExercises()
                .forEach(exercise -> {
                    Exercise exerciseDb = exerciseServiceImpl.findExerciseByName(exercise);
                    program.addExercise(exerciseDb);
                });
        return ResponseEntity.ok().body(programServiceImpl.fromProgram(programRepository.save(program)));
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
        if (!program.getCreated_by().equals(loggedInUser.getUserName())) {
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

    @ApiOperation(value = "Make program private")
    @PutMapping("update/{programName}/public/{isPublic}")
    ResponseEntity<ProgramGetDto> updatePublic(
            @ApiParam(value = "Program name to update") @PathVariable String programName,
            @ApiParam(value = "New program name") @PathVariable Boolean isPublic
    ) {
        User loggedInUser = this.userServiceSCRT.getCurrentLoggedInUser();
        Program program = programServiceImpl.updatePublic(programName, isPublic, loggedInUser);
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