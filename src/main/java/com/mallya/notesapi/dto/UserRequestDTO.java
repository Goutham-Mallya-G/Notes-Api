package com.mallya.notesapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRequestDTO {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_=+\\-;':></?{}]).{8,30}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be 8-30 characters long."
    )    private String password;
}
