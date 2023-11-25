package ksp7331.practice.libraryAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibraryAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryAPIApplication.class, args);
	}

}
