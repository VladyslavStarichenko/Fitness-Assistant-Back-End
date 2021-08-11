package ua.com.nure.fitnessassistant.dto.user.request;

import lombok.Data;

@Data
public class AuthenticationDto {

    private String username;
    private String password;
}
