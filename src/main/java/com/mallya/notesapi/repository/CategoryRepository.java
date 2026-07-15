package com.mallya.notesapi.repository;

import com.mallya.notesapi.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByUserEmailAndId(String email, Long id);

    boolean existsByUserEmailAndNameIgnoreCase(String email, @NotNull(message = "Name should not be null") @NotBlank(message = "Name should not be blank") @Size(min=3, max=30, message = "Name should be between 3 to 30 characters") String name);
}
