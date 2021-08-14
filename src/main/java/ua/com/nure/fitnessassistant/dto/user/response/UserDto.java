package ua.com.nure.fitnessassistant.dto.user.response;



import lombok.Data;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;

import ua.com.nure.fitnessassistant.model.user.Goal;

import java.util.Set;

@Data
public class UserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Set<ProgramGetDto> programs;
    private Goal goal;


}
