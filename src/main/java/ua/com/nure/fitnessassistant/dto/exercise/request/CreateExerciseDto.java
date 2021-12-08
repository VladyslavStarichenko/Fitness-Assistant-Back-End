package ua.com.nure.fitnessassistant.dto.exercise.request;

import lombok.Data;



@Data
public class CreateExerciseDto {

    private String name;

    private Integer repeats;

    private Integer timeToDoOneRep;

    private String img;

    private String description;

    private Integer rest;
}
