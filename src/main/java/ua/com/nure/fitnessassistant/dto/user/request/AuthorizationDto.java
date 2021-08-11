package ua.com.nure.fitnessassistant.dto.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ua.com.nure.fitnessassistant.model.user.Goal;
import ua.com.nure.fitnessassistant.model.user.User;

import java.sql.Date;
import java.time.LocalDate;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationDto {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String mail;
    private Integer age;
    private Goal goal;
    private String password;
    private Date created;
    private Date updated;

    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMail(mail);
        user.setAge(age);
        user.setGoal(goal);
        user.setPassword(password);
//        user.setCreatedAt(Date.valueOf(LocalDate.now()));
//        user.setUpdatedAt(Date.valueOf(LocalDate.now()));
        return user;
    }

    public static AuthorizationDto fromUser(User user) {
        AuthorizationDto userDto = new AuthorizationDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUserName());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMail(user.getMail());
        userDto.setAge(user.getAge());
        userDto.setGoal(user.getGoal());
        userDto.setPassword(userDto.getPassword());
        userDto.setCreated((Date) user.getCreatedAt());
        userDto.setUpdated((Date) user.getUpdatedAt());
        return userDto;
    }
}
