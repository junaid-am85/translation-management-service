package com.java.translation.service;

import com.java.translation.entity.Translation;
import com.java.translation.repository.TranslationRepository;
import com.java.translation.specification.TranslationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TranslationService {

	private final TranslationRepository translationRepository;

	@Autowired
	public TranslationService(TranslationRepository translationRepository) {
		this.translationRepository = translationRepository;
	}

	private static Specification<Translation> getTranslationSpecification(String locale, String key, String content, String tag) {
		Specification<Translation> spec = Specification.where(null);

		if (locale != null) {
			spec = spec.and(TranslationSpecification.hasLocale(locale));
		}
		if (key != null) {
			spec = spec.and(TranslationSpecification.hasKey(key));
		}
		if (content != null) {
			spec = spec.and(TranslationSpecification.hasContent(content));
		}
		if (tag != null) {
			spec = spec.and(TranslationSpecification.hasTag(tag));
		}
		return spec;
	}

	public Translation create(Translation translation) {
		return translationRepository.save(translation);
	}

	public Optional<Translation> getById(Long id) {
		return translationRepository.findById(id);
	}

	public List<Translation> searchByLocale(String locale) {
		return translationRepository.findByLocale(locale);
	}

	public Translation update(Long id, Translation updated) {
		return translationRepository.findById(id).map(existing -> {
			existing.setContent(updated.getContent());
			existing.setTags(updated.getTags());
			return translationRepository.save(existing);
		}).orElseThrow(() -> new RuntimeException("Translation not found"));
	}

	public void delete(Long id) {
		translationRepository.deleteById(id);
	}

	public List<Translation> getAll() {
		return translationRepository.findAll();
	}

	public List<Translation> search(String locale, String key, String content, String tag) {
		boolean noParamsProvided = (locale == null && key == null && content == null && tag == null);

		if (noParamsProvided) {
			return List.of();
		}

		Specification<Translation> spec = getTranslationSpecification(locale, key, content, tag);
		return translationRepository.findAll(spec);
	}
}
