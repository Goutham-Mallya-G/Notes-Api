package com.mallya.notesapi.service;

import com.mallya.notesapi.dto.category.CategoryRequestDTO;
import com.mallya.notesapi.dto.category.CategoryResponseDTO;
import com.mallya.notesapi.exception.CategoryException;
import com.mallya.notesapi.model.Category;
import com.mallya.notesapi.model.Users;
import com.mallya.notesapi.repository.CategoryRepository;
import com.mallya.notesapi.repository.UserRepository;
import com.mallya.notesapi.utils.UtilDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UtilDto utilDto;

    public CategoryResponseDTO addCategory(@Valid CategoryRequestDTO requestDTO, String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user found"));

        if(categoryRepository.existsByUserEmailAndNameIgnoreCase(email,requestDTO.getName())){
            throw new CategoryException("Category already exists");
        }

        Category category = new Category();
        category.setUser(user);
        category.setName(requestDTO.getName());
        category.setNotes(new ArrayList<>());

        categoryRepository.save(category);

        return utilDto.convertCategoryToCategoryResponseDTO(category);
    }

    public List<CategoryResponseDTO> listAllCategories(String email) {
        List<Category> list = categoryRepository.findByUserEmail(email);
        return list.stream().map(utilDto::convertCategoryToCategoryResponseDTO).toList();
    }

    public CategoryResponseDTO updateCategory(Long id, @Valid CategoryRequestDTO requestDTO, String email) {
        Category category = categoryRepository.findByUserEmailAndId(email, id).orElseThrow(() -> new CategoryException("No Category found"));

        category.setName(requestDTO.getName());

        categoryRepository.save(category);

        return utilDto.convertCategoryToCategoryResponseDTO(category);

    }

    public Map<String,String> deleteCategory(Long id, String email) {
        Category category = categoryRepository.findByUserEmailAndId(email, id).orElseThrow(() -> new CategoryException("No Category found"));
        if(!category.getNotes().isEmpty()) {
            return Map.of("message", "Category contains notes. Move or delete them first");
        }

        categoryRepository.delete(category);
        return Map.of("message", "Category deleted successfully");

    }
}
