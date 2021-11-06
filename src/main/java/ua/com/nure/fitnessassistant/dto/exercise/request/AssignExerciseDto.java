package ua.com.nure.fitnessassistant.dto.exercise.request;


import lombok.Data;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;

import java.util.List;

@Data
public class AssignExerciseDto {
    private List<String> exercises;

}
