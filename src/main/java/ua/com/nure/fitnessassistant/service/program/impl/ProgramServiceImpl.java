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
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;
import ua.com.nure.fitnessassistant.repository.program.ProgramRepository;
import ua.com.nure.fitnessassistant.service.program.ProgramService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProgramServiceImpl implements ProgramService {


    private final ProgramRepository programRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository, ModelMapper modelMapper) {
        this.programRepository = programRepository;
        this.modelMapper = modelMapper;

    }


    @Override
    public Program createProgram(Program program) {
        log.info("Program {} was created by user: {}",program,program.getCreated_by());
        return this.programRepository.save(program);
    }

    @Override
    public Page<Program> getPrograms(int pageNumber, int sizeOfPage,String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage, Sort.by(Sort.Order.asc(sortBy)));
        Page<Program> result = programRepository.findAll(pageable);
        log.info("IN getAllPages: It was found - {} program pages", result.getTotalPages());
        return result;
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
        if (programDb.isEmpty()) {
            log.warn("Program with name: {} wasn't found", name);
            throw new CustomException("There is no program with name:" + name,
                    HttpStatus.NOT_FOUND);
        }
        if(!programDb.get().getCreated_by().getUserName().equals(user.getUserName())){
            throw new CustomException("You're not allowed to change this program",
                    HttpStatus.FORBIDDEN);
        }
        programRepository.delete(programDb.get());


    }

    @Override
    public Program updateProgram(String programName, String name, User user) {
        Optional<Program> programDb = this.programRepository.findProgramByName(programName);
        if (programDb.isEmpty()) {
            log.warn("Program with name: {} wasn't found", programName);
            throw new CustomException("There is no program with name:" + programName + " to update",
                    HttpStatus.NOT_FOUND);
        }

        Program programToUpdate = programDb.get();
        if(!programToUpdate.getCreated_by().getUserName().equals(user.getUserName())){
            throw new CustomException("You're not allowed to change this program",
                    HttpStatus.FORBIDDEN);
        }
        Program program = new Program();
        program.setId(programToUpdate.getId());
        program.setName(name);
        program.setStatus(programToUpdate.getStatus());
        program.setCreated_by(user);
        program.setCreatedAt(programToUpdate.getCreatedAt());
        program.setExercises(programToUpdate.getExercises());
        return programRepository.save(program);
    }

    @Override
    public ProgramGetDto fromProgram(Program program) {
        ProgramGetDto programGetDto = modelMapper.map(program,ProgramGetDto.class);
        programGetDto.setName(program.getName());
        programGetDto.setCreated_by(program.getCreated_by().getUserName());
        programGetDto.setExercises(program.getExercises()
                .stream()
                .map(Exercise::getName)
                .collect(Collectors.toSet()));
        return programGetDto;
    }

    @Override
    public ProgramPageResponse fromPage(Page<ProgramGetDto> programPage, List<ProgramGetDto> programs, String sortedBy) {
        ProgramPageResponse response = modelMapper.map(programPage,ProgramPageResponse.class);
        response.setPrograms(programs);
        response.setSortedBy(sortedBy);
        return response;
    }




}
