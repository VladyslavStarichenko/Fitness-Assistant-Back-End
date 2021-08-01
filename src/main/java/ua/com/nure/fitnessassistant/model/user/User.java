package ua.com.nure.fitnessassistant.model.user;

import lombok.Data;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "user_name", unique = true, nullable = false)
    @Size(min = 2,message = "Username should be unique and with length more then 2")
    private String userName;


    @Column(name = "first_name")
    @Size(min = 2,message = "Use your full version of Name please")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2,message = "Use your full version of Name please")
    private String lastName;


    @Pattern(regexp = "\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
            message = "Mail should be in format: example (myemail@address.com)")
    @Column(name = "mail")
    private String mail;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
    message = "Password should contain at least one capital letter, one lowercase letter, special character," +
            "length should be more or equals 8")
    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal")
    private Goal goal;


    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")})
    private List<Role> roles;

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        return this == ((User) obj);
    }


}
