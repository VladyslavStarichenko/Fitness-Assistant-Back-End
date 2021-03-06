package ua.com.nure.fitnessassistant.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import ua.com.nure.fitnessassistant.model.program.Program;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "user_name", unique = true, nullable = false)
    @Size(min = 2,message = "Username should be unique and with length more then 2")
    private String userName;

    @Pattern(regexp = "\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
            message = "Mail should be in format: example (myemail@address.com)")
    @Column(name = "mail")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
    message = "Password should contain at least one capital letter, one lowercase letter, special character," +
            "length should be more or equals 8")
    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private Integer age;

    @Column(name = "weight")
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal")
    private Goal goal;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")})
    private List<Role> roles;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_program",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "program_id",referencedColumnName = "id")})
    private Set<Program> programs = new HashSet<>();
//    @JsonIgnore
//    @OneToMany(fetch = FetchType.EAGER,  cascade = CascadeType.ALL, mappedBy = "created_by")
//    private Set<Program> programs = new HashSet<>();


}
