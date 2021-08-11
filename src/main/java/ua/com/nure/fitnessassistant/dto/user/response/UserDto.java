package ua.com.nure.fitnessassistant.dto.user.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.Goal;
import ua.com.nure.fitnessassistant.model.user.User;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Set<Program> programs;
    private Goal goal;

    public User toUser(){
        User user = new User();
        user.setUserName(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMail(email);
        user.setAge(age);
        user.setGoal(goal);
        user.setPrograms(programs);
        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUserName());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getMail());
        userDto.setAge(user.getAge());
        userDto.setGoal(user.getGoal());
        userDto.setPrograms(user.getPrograms());
        return userDto;
    }
}
