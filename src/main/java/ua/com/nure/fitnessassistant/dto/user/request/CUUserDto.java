package ua.com.nure.fitnessassistant.dto.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ua.com.nure.fitnessassistant.model.user.Goal;
import ua.com.nure.fitnessassistant.model.user.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CUUserDto {


    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Goal goal;
    private String password;


    public User toUser(){
        User user = new User();
        user.setUserName(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAge(age);
        user.setGoal(goal);
        user.setPassword(password);
        return user;
    }


}
