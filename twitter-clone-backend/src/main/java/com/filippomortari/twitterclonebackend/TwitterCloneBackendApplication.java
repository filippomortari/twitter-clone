package com.filippomortari.twitterclonebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TwitterCloneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterCloneBackendApplication.class, args);
	}

}
