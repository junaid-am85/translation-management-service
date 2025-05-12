package com.java.translation;

import com.java.translation.entity.Translation;
import com.java.translation.repository.TranslationRepository;
import com.java.translation.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TranslationServiceTest {

	@InjectMocks
	private TranslationService translationService;

	@Mock
	private TranslationRepository translationRepository;

	private Translation translation;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		translation = new Translation();
		translation.setId(1L);
		translation.setLocale("en");
		translation.setTranslationKey("welcome");
		translation.setContent("Welcome");
	}

	@Test
	void testCreate() {
		when(translationRepository.save(any(Translation.class))).thenReturn(translation);

		Translation createdTranslation = translationService.create(translation);

		assertNotNull(createdTranslation);
		assertEquals("welcome", createdTranslation.getTranslationKey());
		verify(translationRepository, times(1)).save(translation);
	}

	@Test
	void testGetById() {
		when(translationRepository.findById(1L)).thenReturn(Optional.of(translation));

		Optional<Translation> result = translationService.getById(1L);

		assertTrue(result.isPresent());
		assertEquals("Welcome", result.get().getContent());
		verify(translationRepository, times(1)).findById(1L);
	}

	@Test
	void testUpdate() {
		Translation updatedTranslation = new Translation();
		updatedTranslation.setContent("Hello");

		when(translationRepository.findById(1L)).thenReturn(Optional.of(translation));
		when(translationRepository.save(any(Translation.class))).thenReturn(translation);

		Translation result = translationService.update(1L, updatedTranslation);

		assertNotNull(result);
		assertEquals("Hello", result.getContent());
		verify(translationRepository, times(1)).findById(1L);
		verify(translationRepository, times(1)).save(translation);
	}

	@Test
	void testDelete() {
		when(translationRepository.findById(1L)).thenReturn(Optional.of(translation));
		doNothing().when(translationRepository).deleteById(1L);

		translationService.delete(1L);

		verify(translationRepository, times(1)).deleteById(1L);
	}

	@Test
	void testSearchByLocale() {
		when(translationRepository.findByLocale("en")).thenReturn(List.of(translation));

		List<Translation> result = translationService.searchByLocale("en");

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		verify(translationRepository, times(1)).findByLocale("en");
	}

	@Test
	void testSearchWithNoParams() {
		List<Translation> result = translationService.search(null, null, null, null);

		assertTrue(result.isEmpty());
	}

	@Test
	void testSearchWithParams() {
		when(translationRepository.findAll(any(Specification.class))).thenReturn(List.of(translation));

		List<Translation> result = translationService.search("en", "welcome", null, null);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		verify(translationRepository, times(1)).findAll(any(Specification.class));
	}

}
