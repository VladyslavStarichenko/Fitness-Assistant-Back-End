package ua.com.nure.fitnessassistant.dto.program.response;

import lombok.Data;
import java.util.Set;


@Data
public class ProgramGetDto {

    private String name;
    private String created_by;
    private Set<String> exercises;

}
