package ua.com.nure.fitnessassistant.dto.exercise.response;

import lombok.Data;

import java.util.Set;

@Data
public class ExercisePageResponse {

    private Set<String> exercises;
    private int pageNumber;
    private int pageSize;
    private String sortedBy;
    private int totalElements;
    private int totalPages;


}
