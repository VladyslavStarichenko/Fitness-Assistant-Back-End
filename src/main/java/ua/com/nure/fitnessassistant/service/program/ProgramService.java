package ua.com.nure.fitnessassistant.service.program;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.model.program.Program;

import java.util.Optional;


public interface ProgramService {

    Program createProgram (Program program);

    Page<Program> getPrograms(Pageable pageable);

    Program findByProgramByName(String name);

    Program findById(Long id);

    void delete(Long id);

    Program updateProgram(Program user);

    public ProgramGetDto fromProgram(Program program);

}
