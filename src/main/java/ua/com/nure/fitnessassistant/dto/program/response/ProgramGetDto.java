package ua.com.nure.fitnessassistant.dto.program.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.dto.exercise.response.ExerciseGetDto;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Service
public class ProgramGetDto {

    @JsonIgnore
    @Autowired
    ModelMapper modelMapper;

    private String name;
    private String created_by;


    private Set<String> exercises;





    public ProgramGetDto fromProgram(Program program){
        ProgramGetDto programGetDto = modelMapper.map(program,ProgramGetDto.class);
        programGetDto.setName(program.getName());
        programGetDto.setCreated_by(program.getCreated_by().getUserName());
        programGetDto.setExercises(program.getExercises()
                .stream()
                .map(Exercise::getName)
                .collect(Collectors.toSet()));
        return programGetDto;
    }
}
