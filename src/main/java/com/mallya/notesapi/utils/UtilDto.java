package com.mallya.notesapi.utils;

import com.mallya.notesapi.dto.UserRegisterResponseDTO;
import com.mallya.notesapi.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UtilDto {

    public UserRegisterResponseDTO convertUserToUserResponseDTO(Users user){
        UserRegisterResponseDTO userRegisterResponseDTO = new UserRegisterResponseDTO();
        userRegisterResponseDTO.setId(user.getId());
        userRegisterResponseDTO.setEmail(user.getEmail());
        userRegisterResponseDTO.setName(user.getName());
        return userRegisterResponseDTO;
    }
}
