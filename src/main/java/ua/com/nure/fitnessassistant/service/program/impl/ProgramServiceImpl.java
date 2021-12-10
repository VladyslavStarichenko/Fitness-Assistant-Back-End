package ua.com.nure.fitnessassistant.service.program.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramPageResponse;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.program.ProgramRepository;
import ua.com.nure.fitnessassistant.repository.user.UserRepository;
import ua.com.nure.fitnessassistant.service.exercise.impl.ExerciseServiceImpl;
import ua.com.nure.fitnessassistant.service.program.ProgramService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ExerciseServiceImpl exerciseServiceImpl;


    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository, UserRepository userRepository, ModelMapper modelMapper, ExerciseServiceImpl exerciseServiceImpl) {
        this.programRepository = programRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;


        this.exerciseServiceImpl = exerciseServiceImpl;
    }
    @Override
    public Program createProgram(Program program, User user) {
        log.info("Program {} was created by user: {}",program,program.getCreated_by());
        Program programDb = this.programRepository.save(program);
        addProgramToList(program,user);
        return programDb ;
    }
    public User addProgramToList (Program program, User user){
        Set<Program> programs = user.getPrograms();
        programs.add(program);
        userRepository.save(user);
        log.info("IN updateUser - user's program List with id: {} successfully added", user.getId());
        return user;
    }

    public User removeProgramFromList (Program program,User user){
        Set<Program> programs = user.getPrograms();
        programs.remove(program);
        userRepository.save(user);
        log.info("IN updateUser - user's program List with id: {} successfully deleted", user.getId());
        return user;
    }


    @Override
    public Page<Program> getProgramsByUser(int pageNumber, int sizeOfPage,String sortBy, String userName) {
        if(sortBy == null){
            sortBy = "name";
        }
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage, Sort.by(Sort.Order.asc(sortBy)));
        Page<Program> result = programRepository.findProgramsByUserName(pageable,userName);
        log.info("IN getAllPages: It was found - {} program pages", result.getTotalPages());
        return result;
    }

    public Page<Program> getAllPrograms(int pageNumber, int sizeOfPage,String sortBy){
        if(sortBy == null){
            sortBy = "name";
        }
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage, Sort.by(Sort.Order.asc(sortBy)));
        return programRepository.findAll(pageable);
    }


    @Override
    public Program findByProgramByName(String name) {
        Optional<Program> programDb = this.programRepository.findProgramByName(name);
        if (programDb.isPresent()) {
            Program result = programDb.get();
            log.info("IN findByProgramName - program: {} found by name: {}", result, name);
            return result;
        } else {
            throw new CustomException("There is no Program found with requested name: " + name,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Program findById(Long id) {
        Optional<Program> programDb = this.programRepository.findById(id);
        if (programDb.isPresent()) {
            log.info("IN findById - program: {} found by id: {}", programDb,id);
            return programDb.get();
        }else{
            log.warn("IN findById - no program found by id: {}", id);
            throw new CustomException("There is no Program found with request id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(String name, User user) {

        Optional<Program> programDb = this.programRepository.findProgramByName(name);
        if (!programDb.isPresent()) {
            log.warn("Program with name: {} wasn't found", name);
            throw new CustomException("There is no program with name:" + name,
                    HttpStatus.NOT_FOUND);
        }
        if(!programDb.get().getCreated_by().equals(user.getUserName())){
            throw new CustomException("You're not allowed to change this program",
                    HttpStatus.FORBIDDEN);
        }
        programRepository.delete(programDb.get());


    }

    @Override
    public Program updateProgram(String programName, String name, User user) {
        Optional<Program> programDb = this.programRepository.findProgramByName(programName);
        if (!programDb.isPresent()) {
            log.warn("Program with name: {} wasn't found", programName);
            throw new CustomException("There is no program with name:" + programName + " to update",
                    HttpStatus.NOT_FOUND);
        }

        Program programToUpdate = programDb.get();
        if(!programToUpdate.getCreated_by().equals(user.getUserName())){
            throw new CustomException("You're not allowed to change this program",
                    HttpStatus.FORBIDDEN);
        }
        Program program = new Program();
        program.setId(programToUpdate.getId());
        program.setName(name);
        program.setStatus(programToUpdate.getStatus());
        program.setProgramType(programToUpdate.getProgramType());
        program.setCreated_by(user.getUserName());
        program.setPublic(programToUpdate.isPublic());
        program.setDescription(programToUpdate.getDescription());
        program.setCreatedAt(programToUpdate.getCreatedAt());
        program.setExercises(programToUpdate.getExercises());
        return programRepository.save(program);
    }

    public Program updatePublic(String programName, boolean isPublic, User user){
        Optional<Program> programDb = this.programRepository.findProgramByName(programName);
        if (!programDb.isPresent()) {
            log.warn("Program with name: {} wasn't found", programName);
            throw new CustomException("There is no program with name:" + programName + " to update",
                    HttpStatus.NOT_FOUND);
        }

        Program programToUpdate = programDb.get();
        if(!programToUpdate.getCreated_by().equals(user.getUserName())){
            throw new CustomException("You're not allowed to change this program",
                    HttpStatus.FORBIDDEN);
        }
        Program program = new Program();
        program.setId(programToUpdate.getId());
        program.setName(programToUpdate.getName());
        program.setStatus(programToUpdate.getStatus());
        program.setProgramType(programToUpdate.getProgramType());
        program.setCreated_by(user.getUserName());
        program.setPublic(isPublic);
        program.setDescription(program.getDescription());
        program.setCreatedAt(programToUpdate.getCreatedAt());
        program.setExercises(programToUpdate.getExercises());
        return programRepository.save(program);
    }

    @Override
    public ProgramGetDto fromProgram(Program program) {
        ProgramGetDto programGetDto = modelMapper.map(program,ProgramGetDto.class);
        programGetDto.setName(program.getName());
        programGetDto.setProgramType(String.valueOf(program.getProgramType()));
        programGetDto.setCreated_by(program.getCreated_by());
        programGetDto.setPublic(program.isPublic());
        programGetDto.setDescription(program.getDescription());
        programGetDto.setExercises(program.getExercises()
                .stream()
                .map(exerciseServiceImpl::fromExercise)
                .collect(Collectors.toSet()));

        Integer totalTime = program.getExercises().stream()
                .map(el -> el.getRepeats() * el.getRest() + el.getRepeats() * el.getTimeToDoOneRep())
                .reduce(0, Integer::sum);

        programGetDto.setFullTime(totalTime);
        return programGetDto;
    }

    @Override
    public ProgramPageResponse fromPage(Page<ProgramGetDto> programPage, List<ProgramGetDto> programs, String sortedBy) {
        ProgramPageResponse response = modelMapper.map(programPage,ProgramPageResponse.class);
        if(sortedBy == null){
            sortedBy = "name";
        }
        response.setPageSize(programPage.getSize());
        response.setPrograms(programs);
        response.setSortedBy(sortedBy);
        return response;
    }




}
