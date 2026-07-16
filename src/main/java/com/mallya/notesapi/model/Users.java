package com.mallya.notesapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String password;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Notes> notesList;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Category> categories;
}
