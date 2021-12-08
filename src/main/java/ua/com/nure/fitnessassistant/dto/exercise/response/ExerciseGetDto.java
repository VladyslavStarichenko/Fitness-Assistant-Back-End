package ua.com.nure.fitnessassistant.dto.exercise.response;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;

import javax.persistence.Column;


@Service
@Data
public class ExerciseGetDto {


    private String name;

    private Integer repeats;


    private Integer timeToDoOneRep;

    private String img;

    private String description;

    private Integer rest;



}
