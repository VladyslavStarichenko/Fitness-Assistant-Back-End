package ua.com.nure.fitnessassistant.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import ua.com.nure.fitnessassistant.model.user.Goal;
import ua.com.nure.fitnessassistant.model.user.User;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Goal goal;
    private String password;

    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMail(email);
        user.setAge(age);
        user.setGoal(goal);
        user.setPassword(password);
        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUserName());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getMail());
        userDto.setAge(user.getAge());
        userDto.setGoal(user.getGoal());
        userDto.setPassword(userDto.password);
        return userDto;
    }
}
