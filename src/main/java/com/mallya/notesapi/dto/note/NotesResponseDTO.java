package com.mallya.notesapi.dto.note;

import com.mallya.notesapi.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotesResponseDTO {
    private Long id;
    private Long userId;
    private String title;
    private String context;
    private Category category;
}
