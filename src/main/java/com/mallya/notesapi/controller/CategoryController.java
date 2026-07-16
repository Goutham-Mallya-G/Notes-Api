package com.mallya.notesapi.controller;

import com.mallya.notesapi.dto.category.CategoryRequestDTO;
import com.mallya.notesapi.dto.category.CategoryResponseDTO;
import com.mallya.notesapi.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDTO> addCategory(@Valid @RequestBody CategoryRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails){
        CategoryResponseDTO responseDTO = categoryService.addCategory(requestDTO, userDetails.getUsername());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory(@AuthenticationPrincipal UserDetails userDetails){
        List<CategoryResponseDTO> list = categoryService.listAllCategories(userDetails.getUsername());
        return ResponseEntity.ok(list);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategoryById(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails){
        CategoryResponseDTO response = categoryService.updateCategory(id, requestDTO, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, String>> deleteCategoryById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        Map<String,String> response = categoryService.deleteCategory(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}
