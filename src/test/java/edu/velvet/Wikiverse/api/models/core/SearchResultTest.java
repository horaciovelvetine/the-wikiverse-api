package edu.velvet.Wikiverse.api.models.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

/**
 * Unit tests for the SearchResult class.
 * Tests all public methods including constructor, getters, and data mapping
 * functionality from Wikidata search entities result.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("SearchResult Tests")
class SearchResultTest {

	private SearchResult searchResult;
	private WbSearchEntitiesResult mockWikiResult;

	@BeforeEach
	void setUp() {
		mockWikiResult = mock(WbSearchEntitiesResult.class);
	}

	@Test
	@DisplayName("Should create SearchResult with all properties from WbSearchEntitiesResult")
	void shouldCreateSearchResultWithAllPropertiesFromWbSearchEntitiesResult() {
		String entityId = "Q123";
		long pageId = 456789L;
		String title = "Test Article";
		String label = "Test Label";
		String description = "Test Description";
		String conceptUri = "https://www.wikidata.org/entity/Q123";

		when(mockWikiResult.getEntityId()).thenReturn(entityId);
		when(mockWikiResult.getPageId()).thenReturn(pageId);
		when(mockWikiResult.getTitle()).thenReturn(title);
		when(mockWikiResult.getLabel()).thenReturn(label);
		when(mockWikiResult.getDescription()).thenReturn(description);
		when(mockWikiResult.getConceptUri()).thenReturn(conceptUri);

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(entityId, searchResult.getEntityID());
		assertEquals(pageId, searchResult.getPageID());
		assertEquals(title, searchResult.getTitle());
		assertEquals(label, searchResult.getLabel());
		assertEquals(description, searchResult.getDescription());
		assertEquals(conceptUri, searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with null values from WbSearchEntitiesResult")
	void shouldCreateSearchResultWithNullValuesFromWbSearchEntitiesResult() {
		when(mockWikiResult.getEntityId()).thenReturn(null);
		when(mockWikiResult.getPageId()).thenReturn(0L);
		when(mockWikiResult.getTitle()).thenReturn(null);
		when(mockWikiResult.getLabel()).thenReturn(null);
		when(mockWikiResult.getDescription()).thenReturn(null);
		when(mockWikiResult.getConceptUri()).thenReturn(null);

		searchResult = new SearchResult(mockWikiResult);

		assertNull(searchResult.getEntityID());
		assertEquals(0L, searchResult.getPageID());
		assertNull(searchResult.getTitle());
		assertNull(searchResult.getLabel());
		assertNull(searchResult.getDescription());
		assertNull(searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with empty strings from WbSearchEntitiesResult")
	void shouldCreateSearchResultWithEmptyStringsFromWbSearchEntitiesResult() {
		when(mockWikiResult.getEntityId()).thenReturn("");
		when(mockWikiResult.getPageId()).thenReturn(0L);
		when(mockWikiResult.getTitle()).thenReturn("");
		when(mockWikiResult.getLabel()).thenReturn("");
		when(mockWikiResult.getDescription()).thenReturn("");
		when(mockWikiResult.getConceptUri()).thenReturn("");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals("", searchResult.getEntityID());
		assertEquals(0L, searchResult.getPageID());
		assertEquals("", searchResult.getTitle());
		assertEquals("", searchResult.getLabel());
		assertEquals("", searchResult.getDescription());
		assertEquals("", searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with special characters and symbols")
	void shouldCreateSearchResultWithSpecialCharactersAndSymbols() {
		String entityId = "Q123456";
		long pageId = 999999L;
		String title = "Article with Special Characters: & < > \" '";
		String label = "Label with émojis 🚀 and symbols!@#$%";
		String description = "Description with\nnewlines\tand\ttabs";
		String conceptUri = "https://www.wikidata.org/entity/Q123456?param=value#fragment";

		when(mockWikiResult.getEntityId()).thenReturn(entityId);
		when(mockWikiResult.getPageId()).thenReturn(pageId);
		when(mockWikiResult.getTitle()).thenReturn(title);
		when(mockWikiResult.getLabel()).thenReturn(label);
		when(mockWikiResult.getDescription()).thenReturn(description);
		when(mockWikiResult.getConceptUri()).thenReturn(conceptUri);

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(entityId, searchResult.getEntityID());
		assertEquals(pageId, searchResult.getPageID());
		assertEquals(title, searchResult.getTitle());
		assertEquals(label, searchResult.getLabel());
		assertEquals(description, searchResult.getDescription());
		assertEquals(conceptUri, searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with very long strings")
	void shouldCreateSearchResultWithVeryLongStrings() {
		String longEntityId = "Q" + "1".repeat(1000);
		long pageId = Long.MAX_VALUE;
		String longTitle = "A".repeat(10000);
		String longLabel = "B".repeat(10000);
		String longDescription = "C".repeat(10000);
		String longConceptUri = "https://www.wikidata.org/entity/" + "D".repeat(10000);

		when(mockWikiResult.getEntityId()).thenReturn(longEntityId);
		when(mockWikiResult.getPageId()).thenReturn(pageId);
		when(mockWikiResult.getTitle()).thenReturn(longTitle);
		when(mockWikiResult.getLabel()).thenReturn(longLabel);
		when(mockWikiResult.getDescription()).thenReturn(longDescription);
		when(mockWikiResult.getConceptUri()).thenReturn(longConceptUri);

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(longEntityId, searchResult.getEntityID());
		assertEquals(pageId, searchResult.getPageID());
		assertEquals(longTitle, searchResult.getTitle());
		assertEquals(longLabel, searchResult.getLabel());
		assertEquals(longDescription, searchResult.getDescription());
		assertEquals(longConceptUri, searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with negative page ID")
	void shouldCreateSearchResultWithNegativePageId() {
		when(mockWikiResult.getEntityId()).thenReturn("Q123");
		when(mockWikiResult.getPageId()).thenReturn(-1L);
		when(mockWikiResult.getTitle()).thenReturn("Test Title");
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn("Test Description");
		when(mockWikiResult.getConceptUri()).thenReturn("https://www.wikidata.org/entity/Q123");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(-1L, searchResult.getPageID());
	}

	@Test
	@DisplayName("Should create SearchResult with zero page ID")
	void shouldCreateSearchResultWithZeroPageId() {
		when(mockWikiResult.getEntityId()).thenReturn("Q123");
		when(mockWikiResult.getPageId()).thenReturn(0L);
		when(mockWikiResult.getTitle()).thenReturn("Test Title");
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn("Test Description");
		when(mockWikiResult.getConceptUri()).thenReturn("https://www.wikidata.org/entity/Q123");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(0L, searchResult.getPageID());
	}

	@Test
	@DisplayName("Should create SearchResult with maximum long page ID")
	void shouldCreateSearchResultWithMaximumLongPageId() {
		when(mockWikiResult.getEntityId()).thenReturn("Q123");
		when(mockWikiResult.getPageId()).thenReturn(Long.MAX_VALUE);
		when(mockWikiResult.getTitle()).thenReturn("Test Title");
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn("Test Description");
		when(mockWikiResult.getConceptUri()).thenReturn("https://www.wikidata.org/entity/Q123");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(Long.MAX_VALUE, searchResult.getPageID());
	}

	@Test
	@DisplayName("Should create SearchResult with minimum long page ID")
	void shouldCreateSearchResultWithMinimumLongPageId() {
		when(mockWikiResult.getEntityId()).thenReturn("Q123");
		when(mockWikiResult.getPageId()).thenReturn(Long.MIN_VALUE);
		when(mockWikiResult.getTitle()).thenReturn("Test Title");
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn("Test Description");
		when(mockWikiResult.getConceptUri()).thenReturn("https://www.wikidata.org/entity/Q123");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(Long.MIN_VALUE, searchResult.getPageID());
	}

	@Test
	@DisplayName("Should create SearchResult with whitespace-only strings")
	void shouldCreateSearchResultWithWhitespaceOnlyStrings() {
		when(mockWikiResult.getEntityId()).thenReturn("   ");
		when(mockWikiResult.getPageId()).thenReturn(123L);
		when(mockWikiResult.getTitle()).thenReturn("\t\n");
		when(mockWikiResult.getLabel()).thenReturn("  \t  ");
		when(mockWikiResult.getDescription()).thenReturn("\n\r\t");
		when(mockWikiResult.getConceptUri()).thenReturn("   ");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals("   ", searchResult.getEntityID());
		assertEquals(123L, searchResult.getPageID());
		assertEquals("\t\n", searchResult.getTitle());
		assertEquals("  \t  ", searchResult.getLabel());
		assertEquals("\n\r\t", searchResult.getDescription());
		assertEquals("   ", searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with mixed null and non-null values")
	void shouldCreateSearchResultWithMixedNullAndNonNullValues() {
		when(mockWikiResult.getEntityId()).thenReturn("Q123");
		when(mockWikiResult.getPageId()).thenReturn(456L);
		when(mockWikiResult.getTitle()).thenReturn(null);
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn(null);
		when(mockWikiResult.getConceptUri()).thenReturn("https://www.wikidata.org/entity/Q123");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals("Q123", searchResult.getEntityID());
		assertEquals(456L, searchResult.getPageID());
		assertNull(searchResult.getTitle());
		assertEquals("Test Label", searchResult.getLabel());
		assertNull(searchResult.getDescription());
		assertEquals("https://www.wikidata.org/entity/Q123", searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with single character strings")
	void shouldCreateSearchResultWithSingleCharacterStrings() {
		when(mockWikiResult.getEntityId()).thenReturn("Q");
		when(mockWikiResult.getPageId()).thenReturn(1L);
		when(mockWikiResult.getTitle()).thenReturn("A");
		when(mockWikiResult.getLabel()).thenReturn("B");
		when(mockWikiResult.getDescription()).thenReturn("C");
		when(mockWikiResult.getConceptUri()).thenReturn("D");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals("Q", searchResult.getEntityID());
		assertEquals(1L, searchResult.getPageID());
		assertEquals("A", searchResult.getTitle());
		assertEquals("B", searchResult.getLabel());
		assertEquals("C", searchResult.getDescription());
		assertEquals("D", searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with numeric strings")
	void shouldCreateSearchResultWithNumericStrings() {
		when(mockWikiResult.getEntityId()).thenReturn("123456");
		when(mockWikiResult.getPageId()).thenReturn(789L);
		when(mockWikiResult.getTitle()).thenReturn("123");
		when(mockWikiResult.getLabel()).thenReturn("456");
		when(mockWikiResult.getDescription()).thenReturn("789");
		when(mockWikiResult.getConceptUri()).thenReturn("https://example.com/123");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals("123456", searchResult.getEntityID());
		assertEquals(789L, searchResult.getPageID());
		assertEquals("123", searchResult.getTitle());
		assertEquals("456", searchResult.getLabel());
		assertEquals("789", searchResult.getDescription());
		assertEquals("https://example.com/123", searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with URL containing query parameters and fragments")
	void shouldCreateSearchResultWithUrlContainingQueryParametersAndFragments() {
		String complexUrl = "https://www.wikidata.org/entity/Q123?param1=value1&param2=value2#fragment";

		when(mockWikiResult.getEntityId()).thenReturn("Q123");
		when(mockWikiResult.getPageId()).thenReturn(456L);
		when(mockWikiResult.getTitle()).thenReturn("Test Title");
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn("Test Description");
		when(mockWikiResult.getConceptUri()).thenReturn(complexUrl);

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(complexUrl, searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with unicode characters")
	void shouldCreateSearchResultWithUnicodeCharacters() {
		String unicodeEntityId = "Q123\u4E2D\u6587";
		long pageId = 456L;
		String unicodeTitle = "Título en Español";
		String unicodeLabel = "Étiquette en Français";
		String unicodeDescription = "Beschreibung auf Deutsch";
		String unicodeUrl = "https://www.wikidata.org/entity/Q123\u4E2D\u6587";

		when(mockWikiResult.getEntityId()).thenReturn(unicodeEntityId);
		when(mockWikiResult.getPageId()).thenReturn(pageId);
		when(mockWikiResult.getTitle()).thenReturn(unicodeTitle);
		when(mockWikiResult.getLabel()).thenReturn(unicodeLabel);
		when(mockWikiResult.getDescription()).thenReturn(unicodeDescription);
		when(mockWikiResult.getConceptUri()).thenReturn(unicodeUrl);

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(unicodeEntityId, searchResult.getEntityID());
		assertEquals(pageId, searchResult.getPageID());
		assertEquals(unicodeTitle, searchResult.getTitle());
		assertEquals(unicodeLabel, searchResult.getLabel());
		assertEquals(unicodeDescription, searchResult.getDescription());
		assertEquals(unicodeUrl, searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with empty entity ID")
	void shouldCreateSearchResultWithEmptyEntityId() {
		when(mockWikiResult.getEntityId()).thenReturn("");
		when(mockWikiResult.getPageId()).thenReturn(123L);
		when(mockWikiResult.getTitle()).thenReturn("Test Title");
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn("Test Description");
		when(mockWikiResult.getConceptUri()).thenReturn("https://www.wikidata.org/entity/Q123");

		searchResult = new SearchResult(mockWikiResult);

		assertEquals("", searchResult.getEntityID());
		assertEquals(123L, searchResult.getPageID());
		assertEquals("Test Title", searchResult.getTitle());
		assertEquals("Test Label", searchResult.getLabel());
		assertEquals("Test Description", searchResult.getDescription());
		assertEquals("https://www.wikidata.org/entity/Q123", searchResult.getUrl());
	}

	@Test
	@DisplayName("Should create SearchResult with all fields set to same value")
	void shouldCreateSearchResultWithAllFieldsSetToSameValue() {
		String sameValue = "same";
		long pageId = 123L;

		when(mockWikiResult.getEntityId()).thenReturn(sameValue);
		when(mockWikiResult.getPageId()).thenReturn(pageId);
		when(mockWikiResult.getTitle()).thenReturn(sameValue);
		when(mockWikiResult.getLabel()).thenReturn(sameValue);
		when(mockWikiResult.getDescription()).thenReturn(sameValue);
		when(mockWikiResult.getConceptUri()).thenReturn(sameValue);

		searchResult = new SearchResult(mockWikiResult);

		assertEquals(sameValue, searchResult.getEntityID());
		assertEquals(pageId, searchResult.getPageID());
		assertEquals(sameValue, searchResult.getTitle());
		assertEquals(sameValue, searchResult.getLabel());
		assertEquals(sameValue, searchResult.getDescription());
		assertEquals(sameValue, searchResult.getUrl());
	}

	@Test
	@DisplayName("Should verify all getter methods return correct values")
	void shouldVerifyAllGetterMethodsReturnCorrectValues() {
		String entityId = "Q999";
		long pageId = 888L;
		String title = "Getter Test Title";
		String label = "Getter Test Label";
		String description = "Getter Test Description";
		String url = "https://www.wikidata.org/entity/Q999";

		when(mockWikiResult.getEntityId()).thenReturn(entityId);
		when(mockWikiResult.getPageId()).thenReturn(pageId);
		when(mockWikiResult.getTitle()).thenReturn(title);
		when(mockWikiResult.getLabel()).thenReturn(label);
		when(mockWikiResult.getDescription()).thenReturn(description);
		when(mockWikiResult.getConceptUri()).thenReturn(url);

		searchResult = new SearchResult(mockWikiResult);

		assertNotNull(searchResult.getEntityID());
		assertNotNull(searchResult.getPageID());
		assertNotNull(searchResult.getTitle());
		assertNotNull(searchResult.getLabel());
		assertNotNull(searchResult.getDescription());
		assertNotNull(searchResult.getUrl());

		assertEquals(entityId, searchResult.getEntityID());
		assertEquals(pageId, searchResult.getPageID());
		assertEquals(title, searchResult.getTitle());
		assertEquals(label, searchResult.getLabel());
		assertEquals(description, searchResult.getDescription());
		assertEquals(url, searchResult.getUrl());
	}

	@Test
	@DisplayName("Should handle constructor with mock verification")
	void shouldHandleConstructorWithMockVerification() {
		when(mockWikiResult.getEntityId()).thenReturn("Q123");
		when(mockWikiResult.getPageId()).thenReturn(456L);
		when(mockWikiResult.getTitle()).thenReturn("Test Title");
		when(mockWikiResult.getLabel()).thenReturn("Test Label");
		when(mockWikiResult.getDescription()).thenReturn("Test Description");
		when(mockWikiResult.getConceptUri()).thenReturn("https://www.wikidata.org/entity/Q123");

		searchResult = new SearchResult(mockWikiResult);

		verify(mockWikiResult, times(1)).getEntityId();
		verify(mockWikiResult, times(1)).getPageId();
		verify(mockWikiResult, times(1)).getTitle();
		verify(mockWikiResult, times(1)).getLabel();
		verify(mockWikiResult, times(1)).getDescription();
		verify(mockWikiResult, times(1)).getConceptUri();
	}
}
