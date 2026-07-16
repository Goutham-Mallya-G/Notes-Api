package com.mallya.notesapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    @Size(min=3, max=50)
    private String title;
    @NotNull
    @NotBlank
    @Size(min=1, max=10000)
    private String content;
    @ManyToOne
    @JoinColumn(name = "users_id")
    @JsonBackReference
    private Users user;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;
    @NotNull
    private boolean archived = false;
    @NotNull
    private boolean deleted = false;
    @NotNull
    private  boolean favorite = false;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private LocalDateTime updatedAt;

}
