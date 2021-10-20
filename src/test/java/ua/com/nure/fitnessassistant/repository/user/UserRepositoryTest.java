package ua.com.nure.fitnessassistant.repository.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.nure.fitnessassistant.model.user.Role;
import ua.com.nure.fitnessassistant.model.user.Status;
import ua.com.nure.fitnessassistant.model.user.User;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@DataJpaTest
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findUserByUserName() {

        //given
        List<Role> roles = new ArrayList<>();
        Role role =  Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
        roles.add(role);
        User user = User.builder()
                .weight(80)
                .userName("goldsilver")
                .email("test@nure.ua")
                .password("test")
                .roles(roles)
                .build();
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        //when
        Optional<User> userFound = userRepository.findUserByUserName(user.getUserName());
        //then
        userFound.ifPresent(userCheck -> assertThat(userCheck.getUserName().equals(user.getUserName())).isTrue());
    }

    @Test
    void existsByUserName() {
        //given
        List<Role> roles = new ArrayList<>();
        Role role =  Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        User user = User.builder()
                .weight(80)
                .userName("goldsilver")
                .email("test@nure.ua")
                .password("test")
                .roles(roles)
                .build();
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        //when
        boolean expected = userRepository.existsByUserName(user.getUserName());
        //then
        assertThat(expected).isTrue();
    }


    @Test
    void notExistsByUserName() {
        //given
        String userName = "goldsilver";
        //when
        boolean expected = userRepository.existsByUserName(userName);
        //then
        assertThat(expected).isFalse();
    }
}