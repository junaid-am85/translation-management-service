package com.java.translation.service;

import com.java.translation.entity.Translation;
import com.java.translation.repository.TranslationRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Configuration
public class DataSeeder {

	private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

	@Bean
	public CommandLineRunner seedDatabase(TranslationRepository repository) {
		return args -> {
			if (repository.count() > 0) return; // prevent reseeding

			Faker faker = new Faker();
			Random random = new Random();
			String[] locales = { "en", "fr", "es", "de", "it" };
			String[] tags = { "mobile", "web", "desktop", "login", "signup" };

			int batchSize = 1000;
			for (int i = 0; i < 100_000; i += batchSize) {
				int finalI = i;
				List<Translation> batch = IntStream.range(0, batchSize).mapToObj(j -> {
					Translation t = new Translation();
					t.setLocale(locales[random.nextInt(locales.length)]);
					t.setTranslationKey("key_" + (finalI + j));
					t.setContent(faker.lorem().sentence(3));
					t.setTags(List.of(tags[random.nextInt(tags.length)]));
					return t;
				}).toList();
				repository.saveAll(batch);
			}

			log.info("Seeded 100,000+ translations.");
		};
	}
}
