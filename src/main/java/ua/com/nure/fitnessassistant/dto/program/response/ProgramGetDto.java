package ua.com.nure.fitnessassistant.dto.program.response;

import lombok.Data;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;

import java.util.Set;


@Data
public class ProgramGetDto {

    private String name;
    private String created_by;
    private String programType;
    private boolean isPublic;
    private Set<ExerciseGetDto> exercises;
    private String description;
    private Integer fullTime;

}
