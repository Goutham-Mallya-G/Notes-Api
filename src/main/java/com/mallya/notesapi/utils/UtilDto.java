package com.mallya.notesapi.utils;

import com.mallya.notesapi.dto.UserResponseDTO;
import com.mallya.notesapi.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UtilDto {

    public UserResponseDTO convertUserToUserResponseDTO(Users user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setName(user.getName());
        return userResponseDTO;
    }
}
