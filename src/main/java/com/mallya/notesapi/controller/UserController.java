package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.UserLoginRequestDTO;
import com.mallya.notesapi.dto.UserLoginResponseDTO;
import com.mallya.notesapi.dto.UserRegisterRequestDTO;
import com.mallya.notesapi.dto.UserRegisterResponseDTO;
import com.mallya.notesapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO requestDTO){
        UserRegisterResponseDTO responseDTO = userService.registerUser(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@Valid @RequestBody UserLoginRequestDTO requestDTO){
        return ResponseEntity.ok(userService.login(requestDTO));
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<UserRegisterResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }


}
