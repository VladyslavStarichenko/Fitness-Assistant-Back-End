package ua.com.nure.fitnessassistant.service.program;

import org.springframework.data.domain.Page;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramGetDto;
import ua.com.nure.fitnessassistant.dto.program.response.ProgramPageResponse;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.User;

import java.util.List;


public interface ProgramService {

    Program createProgram (Program program, User user);


    Page<Program> getPrograms(int pageNumber, int sizeOfPage,String sortBy);

    Program findByProgramByName(String name);

    Program findById(Long id);

    void delete(String name, User user);

    Program updateProgram(String programName, String name, User user);

    ProgramGetDto fromProgram(Program program);

    ProgramPageResponse fromPage(Page<ProgramGetDto> programPage, List<ProgramGetDto> names, String sortedBy);


}
