package ua.com.nure.fitnessassistant.dto.exercise.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;
import ua.com.nure.fitnessassistant.model.program.Program;

@Service
@Data
public class ExerciseGetDto {
    @JsonIgnore
    @Autowired
    ModelMapper modelMapper;

    private String name;

    public ExerciseGetDto fromExercise(Exercise exercise){
        ExerciseGetDto exerciseGetDto = modelMapper.map(exercise,ExerciseGetDto.class);
        exerciseGetDto.setName(exercise.getName());
        return exerciseGetDto;
    }

}
