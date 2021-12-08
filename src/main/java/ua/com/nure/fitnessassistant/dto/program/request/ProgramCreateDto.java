package ua.com.nure.fitnessassistant.dto.program.request;
import lombok.Data;
import ua.com.nure.fitnessassistant.model.program.ProgramType;


@Data
public class ProgramCreateDto {

    private String name;
    public ProgramType programType;
    private boolean isPublic;
    public String description;


}
