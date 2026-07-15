package com.mallya.notesapi.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryRequestDTO {
    @NotNull(message = "Name should not be null")
    @NotBlank(message = "Name should not be blank")
    @Size(min=3, max=30, message = "Name should be between 3 to 30 characters")
    private String name;
}
