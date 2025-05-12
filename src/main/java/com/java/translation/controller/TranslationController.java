package com.java.translation.controller;

import com.java.translation.entity.Translation;
import com.java.translation.service.TranslationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/translations")
@SecurityRequirement(name = "bearerAuth")
public class TranslationController {

	private final TranslationService translationService;

	@Autowired
	public TranslationController(TranslationService translationService) {
		this.translationService = translationService;
	}

	@PostMapping
	public ResponseEntity<Translation> create(@RequestBody Translation translation) {
		return ResponseEntity.ok(translationService.create(translation));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Translation> getById(@PathVariable Long id) {
		return translationService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Translation> update(@PathVariable Long id, @RequestBody Translation updated) {
		return ResponseEntity.ok(translationService.update(id, updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		translationService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<Translation>> search(@RequestParam(required = false) String locale, @RequestParam(required = false) String key,
			@RequestParam(required = false) String content, @RequestParam(required = false) String tag) {
		List<Translation> results = translationService.search(locale, key, content, tag);
		return ResponseEntity.ok(results);
	}

	@GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> exportTranslations(@RequestParam(required = false) String locale) {
		List<Translation> translations = (locale != null) ? translationService.searchByLocale(locale) : translationService.getAll();

		Map<String, String> translationMap = translations.stream()
				.collect(Collectors.toMap(Translation::getTranslationKey, Translation::getContent, (existing, replacement) -> existing));

		return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(translationMap);
	}

}
