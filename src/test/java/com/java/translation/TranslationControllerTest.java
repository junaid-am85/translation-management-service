package com.java.translation;

import com.java.translation.controller.TranslationController;
import com.java.translation.entity.Translation;
import com.java.translation.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TranslationController.class)
@Import(TestSecurityConfig.class)
public class TranslationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TranslationService translationService;

	private Translation translation;

	@BeforeEach
	void setUp() {

		translation = new Translation();
		translation.setId(1L);
		translation.setLocale("en");
		translation.setTranslationKey("welcome");
		translation.setContent("Welcome");

	}

	@Test
	void testCreateTranslation() throws Exception {
		when(translationService.create(any(Translation.class))).thenReturn(translation);

		mockMvc.perform(post("/api/translations").contentType("application/json").content("{\"locale\":\"en\",\"translationKey\":\"welcome\",\"content\":\"Welcome\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content").value("Welcome"));
	}

	@Test
	void testGetTranslationById() throws Exception {
		when(translationService.getById(1L)).thenReturn(java.util.Optional.of(translation));

		mockMvc.perform(get("/api/translations/1")).andExpect(status().isOk()).andExpect(jsonPath("$.content").value("Welcome"));
	}

	@Test
	void testUpdateTranslation() throws Exception {
		Translation updatedTranslation = new Translation();
		updatedTranslation.setContent("Hello");
		when(translationService.update(eq(1L), any(Translation.class))).thenReturn(updatedTranslation);

		mockMvc.perform(put("/api/translations/1").contentType("application/json").content("{\"content\":\"Hello\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("Hello"));
	}

	@Test
	void testDeleteTranslation() throws Exception {
		doNothing().when(translationService).delete(1L);

		mockMvc.perform(delete("/api/translations/1")).andExpect(status().isNoContent());
	}

	@Test
	void testSearchTranslations() throws Exception {
		when(translationService.search("en", "welcome", null, null)).thenReturn(java.util.List.of(translation));

		mockMvc.perform(get("/api/translations/search?locale=en&key=welcome")).andExpect(status().isOk()).andExpect(jsonPath("$[0].content").value("Welcome"));
	}

	@Test
	void testExportTranslations() throws Exception {
		when(translationService.searchByLocale("en")).thenReturn(java.util.List.of(translation));

		mockMvc.perform(get("/api/translations/export?locale=en")).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.welcome").value("Welcome"));
	}
}
