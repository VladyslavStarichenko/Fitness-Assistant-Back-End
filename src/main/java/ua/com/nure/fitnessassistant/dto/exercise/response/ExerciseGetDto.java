package ua.com.nure.fitnessassistant.dto.exercise.response;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nure.fitnessassistant.model.exercise.Exercise;


@Service
@Data
public class ExerciseGetDto {


    private String name;



}
