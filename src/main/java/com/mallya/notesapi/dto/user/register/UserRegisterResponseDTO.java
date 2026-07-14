package com.mallya.notesapi.dto.user.register;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterResponseDTO {
    private Long id;
    private String email;
    private String name;
}
