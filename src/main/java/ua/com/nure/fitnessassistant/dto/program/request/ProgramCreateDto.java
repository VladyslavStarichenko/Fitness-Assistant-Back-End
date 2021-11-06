package ua.com.nure.fitnessassistant.dto.program.request;
import lombok.Data;



@Data
public class ProgramCreateDto {

    private String name;
    public String  programType;
    private boolean isPublic;


}
