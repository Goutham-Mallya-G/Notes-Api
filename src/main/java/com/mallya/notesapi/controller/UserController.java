package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.user.login.UserLoginRequestDTO;
import com.mallya.notesapi.dto.user.register.UserRegisterRequestDTO;
import com.mallya.notesapi.dto.user.register.UserRegisterResponseDTO;
import com.mallya.notesapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginRequestDTO requestDTO){
        return ResponseEntity.ok(userService.login(requestDTO));
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<UserRegisterResponseDTO>> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        return ResponseEntity.ok(userService.getAllUsers(page,size));
    }


}
