package com.mallya.notesapi.utils;

import com.mallya.notesapi.model.Notes;
import com.mallya.notesapi.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrashCleanUpScheduler {
    private final NotesRepository notesRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpired() {
        LocalDateTime expiry = LocalDateTime.now().minusDays(30);
        List<Notes> notes = notesRepository.findByDeletedTrueAndDeletedAtBefore(expiry);
        notesRepository.deleteAll(notes);
    }
}
