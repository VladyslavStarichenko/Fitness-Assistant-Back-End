package ua.com.nure.fitnessassistant.service.exercise.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.dto.exercise.request.CreateExerciseDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExercisePageResponse;
import ua.com.nure.fitnessassistant.exeption.CustomException;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.repository.exercise.ExerciseRepository;

import ua.com.nure.fitnessassistant.service.exercise.ExerciseService;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ModelMapper modelMapper) {
        this.exerciseRepository = exerciseRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Exercise createExercise(Exercise exercise) {
        log.info("Exercise {} was successfully created ", exercise);
        if (exerciseRepository.existsByName(exercise.getName())) {
            log.warn("Exercise with name {} is already exists", exercise.getName());
            throw new CustomException("Exercise with name is already exists!", HttpStatus.CONFLICT);
        }
        return this.exerciseRepository.save(exercise);
    }

    @Override
    public Page<Exercise> getAllExercises(int pageNumber, int sizeOfPage, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage, Sort.by(Sort.Order.asc(sortBy)));
        Page<Exercise> result = exerciseRepository.findAll(pageable);
        log.info("IN getAllExercises: It was found - {} exercise pages", result.getTotalPages());
        return result;
    }

    @Override
    public Exercise findExerciseByName(String name) {
        Optional<Exercise> exerciseDb = this.exerciseRepository.findExercisesByName(name);
        if (exerciseDb.isPresent()) {
            Exercise result = exerciseDb.get();
            log.info("IN findByExerciseName - exercise: {} found by name: {}", result, name);
            return result;
        } else {
            log.warn("IN findById - no exercise found by name: {}", name);
            throw new CustomException("There is no exercise found with requested name: " + name,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Exercise findById(Long id) {
        Optional<Exercise> exerciseDb = this.exerciseRepository.findById(id);
        if (exerciseDb.isPresent()) {
            log.info("IN findById - exercise: {} found by id: {}", exerciseDb, id);
            return exerciseDb.get();
        } else {
            log.warn("IN findById - no exercise found by id: {}", id);
            throw new CustomException("There is no exercise found with request id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(String name) {
        Optional<Exercise> exerciseDb = this.exerciseRepository.findExercisesByName(name);
        if (exerciseDb.isEmpty()){
            throw new CustomException("There is no exercise with name:" + name,HttpStatus.NOT_FOUND);
        }
        log.info("IN deleteExercise: exercise with name {} was successfully deleted",name);
        this.exerciseRepository.delete(exerciseDb.get());
    }


    @Override
    public Exercise updateExercise(Exercise exerciseUpdate, String name) {
        Optional<Exercise> exerciseDb = this.exerciseRepository.findExercisesByName(name);
        if(exerciseDb.isEmpty()){
            throw new CustomException("There is no exercise found with name:" + name + " to update",
                    HttpStatus.NOT_FOUND);
        }
        Exercise exerciseToUpdate = exerciseDb.get();
        exerciseUpdate.setId(exerciseToUpdate.getId());
        exerciseUpdate.setStatus(exerciseToUpdate.getStatus());
        exerciseUpdate.setPrograms(exerciseToUpdate.getPrograms());
        exerciseUpdate.setCreatedAt(exerciseToUpdate.getCreatedAt());
        exerciseUpdate.setName(exerciseUpdate.getName());
        return exerciseRepository.save(exerciseUpdate);
    }

    @Override
    public ExerciseGetDto fromExercise(Exercise exercise) {
        ExerciseGetDto exerciseGetDto = modelMapper.map(exercise, ExerciseGetDto.class);
        exerciseGetDto.setName(exercise.getName());
        return exerciseGetDto;
    }

    @Override
    public Exercise toExercise(CreateExerciseDto exerciseGetDto) {
        return modelMapper.map(exerciseGetDto, Exercise.class);
    }

    @Override
    public ExercisePageResponse fromExercisePage(Page<ExerciseGetDto> page, Set<String> names, String sortedBy) {
        ExercisePageResponse exercisePageResponse = modelMapper.map(page, ExercisePageResponse.class);
        exercisePageResponse.setExercises(names);
        exercisePageResponse.setSortedBy(sortedBy);
        return exercisePageResponse;
    }

    @Override
    public Set<String> getNames(Page<ExerciseGetDto> names) {
        List<ExerciseGetDto> dtoList = names.getContent();
        return dtoList
                .stream()
                .map(ExerciseGetDto::getName)
                .collect(Collectors.toSet());
    }


}
