package ua.com.nure.fitnessassistant.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
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

    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMail(email);
        user.setAge(age);
        user.setGoal(goal);
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
        return userDto;
    }
}
