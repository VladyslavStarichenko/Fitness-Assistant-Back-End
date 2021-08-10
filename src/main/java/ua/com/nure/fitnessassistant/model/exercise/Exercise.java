package ua.com.nure.fitnessassistant.model.exercise;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import ua.com.nure.fitnessassistant.model.program.Program;
import ua.com.nure.fitnessassistant.model.user.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "exercises")

public class Exercise extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;


    @Column(name = "name")
    private String name;


    @JsonIgnore
    @ManyToMany(mappedBy = "exercises")
    private Set<Program> programs = new HashSet<>();

    public Exercise() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }
}
