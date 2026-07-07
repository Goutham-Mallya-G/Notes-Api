package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.UserRequestDTO;
import com.mallya.notesapi.dto.UserResponseDTO;
import com.mallya.notesapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO requestDTO){
        UserResponseDTO responseDTO = userService.registerUser(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/test")
    public String test(){
        return "Test";
    }

}
