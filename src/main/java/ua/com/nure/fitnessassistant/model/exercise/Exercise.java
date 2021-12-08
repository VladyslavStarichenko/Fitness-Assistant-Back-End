package ua.com.nure.fitnessassistant.model.exercise;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercises")

public class Exercise extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(name = "name")
    private String name;

    private Integer repeats;

    @Column(name ="rep_time")
    private Integer timeToDoOneRep;

    private String img;

    private String description;

    private Integer rest;

    @JsonIgnore
    @ManyToMany(mappedBy = "exercises")
    private Set<Program> programs = new HashSet<>();

}
