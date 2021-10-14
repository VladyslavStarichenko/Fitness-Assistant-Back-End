package ua.com.nure.fitnessassistant.dto.user.response;

import lombok.Data;


import java.util.List;

@Data
public class UserPageResponse {
    private List<UserDto> users;
    private int pageNumber;
    private int pageSize;
    private String sortedBy;
    private int totalElements;
    private int totalPages;
}
