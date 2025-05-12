package com.java.translation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranslationApplication {

	private static final Logger log = LoggerFactory.getLogger(TranslationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TranslationApplication.class, args);
		log.info("TestApplication has started successfully!");
	}

}
