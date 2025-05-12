package com.java.translation.specification;

import com.java.translation.entity.Translation;
import org.springframework.data.jpa.domain.Specification;

public class TranslationSpecification {

	public static Specification<Translation> hasLocale(String locale) {
		return (root, query, cb) -> cb.equal(root.get("locale"), locale);
	}

	public static Specification<Translation> hasKey(String key) {
		return (root, query, cb) -> cb.equal(root.get("key"), key);
	}

	public static Specification<Translation> hasContent(String content) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%");
	}

	public static Specification<Translation> hasTag(String tag) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("tags")), "%" + tag.toLowerCase() + "%");
	}

}
