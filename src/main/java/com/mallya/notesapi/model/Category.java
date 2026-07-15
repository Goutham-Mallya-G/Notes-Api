package com.mallya.notesapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<Notes> notes;
    @ManyToOne
    @JoinColumn(name = "categories")
    @JsonBackReference
    private Users user;
}
