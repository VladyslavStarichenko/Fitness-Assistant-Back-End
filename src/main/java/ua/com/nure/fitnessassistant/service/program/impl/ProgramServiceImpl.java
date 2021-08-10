package ua.com.nure.fitnessassistant.service.program.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.repository.ProgramRepository;
import ua.com.nure.fitnessassistant.service.program.ProgramService;

import java.util.Optional;


@Service
@Slf4j
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    ProgramRepository programRepository;

//    @Override
//    public Program createProgram(Program program) {
//        log.info("Program {} was created by user: {}",program,program.getCreated_by());
//        return this.programRepository.save(program);
//    }

    @Override
    public Page<Program> getPrograms(Pageable pageable) {
        Page<Program> result = programRepository.findAll(pageable);
        log.info("IN getAll - {} users found", result.getTotalPages());
        return result;
    }

    @Override
    public Program findByProgramByName(String name) {
        Optional<Program> programDb = Optional.ofNullable(this.programRepository.findProgramByName(name));
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
    public void delete(Long id) {
        Optional<Program> programDb = this.programRepository.findById(id);
        if (programDb.isPresent()) {
            this.programRepository.delete(programDb.get());
            log.info("IN delete - program with id: {} successfully deleted", id);
        } else {
            throw new CustomException("There is no Program found with request id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Program updateProgram(Program program) {
        Optional<Program> programDb = this.programRepository.findById(program.getId());
        if (programDb.isPresent()) {
            Program programToUpdate = programDb.get();
            programToUpdate.setId(program.getId());
//            programToUpdate.setCreated_by(program.getCreated_by());
            programToUpdate.setName(program.getName());
            programToUpdate.setStatus(program.getStatus());
            programToUpdate.setCreatedAt(program.getCreatedAt());
            programToUpdate.setExercises(program.getExercises());
            programRepository.save(programToUpdate);
            log.info("Program with id: {} was updated",program.getId());
            return programToUpdate;
        } else {
            throw new CustomException("There is no user with id:" + program.getId() + " to update",
                    HttpStatus.NOT_FOUND);
        }
    }
}
