package ua.com.nure.fitnessassistant.dto.program.response;

import lombok.Data;

import java.util.List;

@Data
public class ProgramPageResponse {

    private List<ProgramGetDto> programs;
    private int pageNumber;
    private int pageSize;
    private String sortedBy;
    private int totalElements;
    private int totalPages;
}
