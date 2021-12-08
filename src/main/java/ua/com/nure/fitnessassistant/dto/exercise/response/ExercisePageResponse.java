package ua.com.nure.fitnessassistant.dto.exercise.response;

import lombok.Data;

import java.util.Set;

@Data
public class ExercisePageResponse {

    private Set<ExerciseGetDto> exercises;
    private int pageNumber;
    private int pageSize;
    private String sortedBy;
    private int totalElements;
    private int totalPages;


}
