package com.mallya.notesapi.dto.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
public class NotesRequestDTO {

    @NotNull(message = "Title should not be null")
    @NotBlank(message = "Title should not be blank")
    @Size(min=3, max=50, message = "Title must contain characters between 3 and 50")
    private String title;
    @NotNull(message = "Content should not be null")
    @NotBlank(message = "Content should not be blank")
    @Size(min=1, max=10000, message = "Content must contain characters between 3 and 50")
    private String content;
    @Nullable
    private Long categoryId;
}
