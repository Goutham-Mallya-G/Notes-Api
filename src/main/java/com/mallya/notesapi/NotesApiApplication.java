package com.mallya.notesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesApiApplication.class, args);
	}

}
