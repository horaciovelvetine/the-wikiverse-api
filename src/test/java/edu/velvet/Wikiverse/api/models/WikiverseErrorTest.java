package edu.velvet.Wikiverse.api.models;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the enhanced WikiverseError class and all its implementations.
 * Tests all error types including ServiceFault and all WikidataServiceError
 * variants,
 * ensuring proper handling of all enhanced features including timestamps, error
 * categories,
 * HTTP status codes, and stack traces.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Enhanced WikiverseError Tests")
class WikiverseErrorTest {

	// Test data constants
	private static final String TEST_MESSAGE = "Test error message";
	private static final String TEST_SOURCE = "TestSource.java";
	private static final String TEST_QUERY = "test query";
	private static final int TEST_HTTP_STATUS = 500;

	private Instant testTimestamp;
	private StackTraceElement[] testStackTrace;

	@BeforeEach
	void setUp() {
		testTimestamp = Instant.now();
		testStackTrace = Thread.currentThread().getStackTrace();
	}

	// ==================== ErrorCategory Tests ====================

	@Test
	@DisplayName("ErrorCategory enum should have all expected values")
	void errorCategoryEnumShouldHaveAllExpectedValues() {
		WikiverseError.ErrorCategory[] categories = WikiverseError.ErrorCategory.values();

		assertEquals(9, categories.length);
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.NETWORK));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.VALIDATION));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.PROCESSING));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.AUTHENTICATION));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.CONFIGURATION));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.EXTERNAL_SERVICE));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.INTERNAL_LOGIC));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.NOT_FOUND));
		assertTrue(List.of(categories).contains(WikiverseError.ErrorCategory.RATE_LIMITED));
	}

	// ==================== ServiceFault Tests ====================

	@Test
	@DisplayName("ServiceFault should create with full constructor")
	void serviceFaultShouldCreateWithFullConstructor() {
		WikiverseError.ServiceFault error = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				testTimestamp,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC,
				TEST_HTTP_STATUS,
				testStackTrace);

		assertNotNull(error);
		assertEquals(TEST_MESSAGE, error.message());
		assertEquals(TEST_SOURCE, error.source());
		assertEquals(testTimestamp, error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.INTERNAL_LOGIC, error.category());
		assertEquals(TEST_HTTP_STATUS, error.httpStatusCode());
		assertEquals(testStackTrace, error.stackTrace());
	}

	@Test
	@DisplayName("ServiceFault should create with convenience constructor")
	void serviceFaultShouldCreateWithConvenienceConstructor() {
		WikiverseError.ServiceFault error = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				WikiverseError.ErrorCategory.CONFIGURATION);

		assertNotNull(error);
		assertEquals(TEST_MESSAGE, error.message());
		assertEquals(TEST_SOURCE, error.source());
		assertNotNull(error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.CONFIGURATION, error.category());
		assertEquals(500, error.httpStatusCode());
		assertNotNull(error.stackTrace());
		assertEquals(0, error.stackTrace().length);
	}

	@Test
	@DisplayName("ServiceFault should implement WikiverseError interface")
	void serviceFaultShouldImplementWikiverseErrorInterface() {
		WikiverseError.ServiceFault error = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				WikiverseError.ErrorCategory.VALIDATION);

		assertTrue(error instanceof WikiverseError);
	}

	@Test
	@DisplayName("ServiceFault should handle null values in required fields")
	void serviceFaultShouldHandleNullValuesInRequiredFields() {
		WikiverseError.ServiceFault error = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				testTimestamp,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC,
				null,
				null);

		assertNull(error.httpStatusCode());
		assertNull(error.stackTrace());
	}

	@Test
	@DisplayName("ServiceFault should be equal when same parameters")
	void serviceFaultShouldBeEqualWhenSameParameters() {
		WikiverseError.ServiceFault error1 = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				testTimestamp,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC,
				TEST_HTTP_STATUS,
				testStackTrace);

		WikiverseError.ServiceFault error2 = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				testTimestamp,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC,
				TEST_HTTP_STATUS,
				testStackTrace);

		assertEquals(error1, error2);
		assertEquals(error1.hashCode(), error2.hashCode());
	}

	// ==================== APIOffline Tests ====================

	@Test
	@DisplayName("APIOffline should create with full constructor")
	void apiOfflineShouldCreateWithFullConstructor() {
		WikiverseError.WikidataServiceError.APIOffline error = new WikiverseError.WikidataServiceError.APIOffline(
				TEST_MESSAGE,
				TEST_SOURCE,
				testTimestamp,
				WikiverseError.ErrorCategory.EXTERNAL_SERVICE,
				TEST_HTTP_STATUS,
				testStackTrace);

		assertNotNull(error);
		assertEquals(TEST_MESSAGE, error.message());
		assertEquals(TEST_SOURCE, error.source());
		assertEquals(testTimestamp, error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.EXTERNAL_SERVICE, error.category());
		assertEquals(TEST_HTTP_STATUS, error.httpStatusCode());
		assertEquals(testStackTrace, error.stackTrace());
	}

	@Test
	@DisplayName("APIOffline should create with convenience constructor with message and source")
	void apiOfflineShouldCreateWithConvenienceConstructorWithMessageAndSource() {
		WikiverseError.WikidataServiceError.APIOffline error = new WikiverseError.WikidataServiceError.APIOffline(
				TEST_MESSAGE,
				TEST_SOURCE,
				testStackTrace);

		assertNotNull(error);
		assertEquals(TEST_MESSAGE, error.message());
		assertEquals(TEST_SOURCE, error.source());
		assertNotNull(error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.EXTERNAL_SERVICE, error.category());
		assertEquals(503, error.httpStatusCode());
		assertEquals(testStackTrace, error.stackTrace());
	}

	// ==================== NoMatchingResultsFound Tests ====================

	@Test
	@DisplayName("NoMatchingResultsFound should create with full constructor")
	void noMatchingResultsFoundShouldCreateWithFullConstructor() {
		WikiverseError.WikidataServiceError.NoMatchingResultsFound error = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				TEST_QUERY,
				testTimestamp,
				WikiverseError.ErrorCategory.NOT_FOUND,
				404,
				testStackTrace);

		assertNotNull(error);
		assertEquals(TEST_QUERY, error.queryValue());
		assertEquals("WikidataFetchBroker.java", error.source());
		assertEquals(testTimestamp, error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.NOT_FOUND, error.category());
		assertEquals(404, error.httpStatusCode());
		assertEquals(testStackTrace, error.stackTrace());
	}

	@Test
	@DisplayName("NoMatchingResultsFound should create with convenience constructor")
	void noMatchingResultsFoundShouldCreateWithConvenienceConstructor() {
		WikiverseError.WikidataServiceError.NoMatchingResultsFound error = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				TEST_QUERY);

		assertNotNull(error);
		assertEquals(TEST_QUERY, error.queryValue());
		assertEquals("WikidataFetchBroker.java", error.source());
		assertNotNull(error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.NOT_FOUND, error.category());
		assertEquals(404, error.httpStatusCode());
		assertNotNull(error.stackTrace());
		assertEquals(0, error.stackTrace().length);
	}

	@Test
	@DisplayName("NoMatchingResultsFound should return correct message")
	void noMatchingResultsFoundShouldReturnCorrectMessage() {
		WikiverseError.WikidataServiceError.NoMatchingResultsFound error = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				TEST_QUERY);

		String expectedMessage = "No results found for: " + TEST_QUERY;
		assertEquals(expectedMessage, error.message());
	}

	@Test
	@DisplayName("NoMatchingResultsFound should implement WikidataServiceError interface")
	void noMatchingResultsFoundShouldImplementWikidataServiceErrorInterface() {
		WikiverseError.WikidataServiceError.NoMatchingResultsFound error = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				TEST_QUERY);

		assertTrue(error instanceof WikiverseError.WikidataServiceError);
		assertTrue(error instanceof WikiverseError);
	}

	@Test
	@DisplayName("NoMatchingResultsFound should handle null query value")
	void noMatchingResultsFoundShouldHandleNullQueryValue() {
		WikiverseError.WikidataServiceError.NoMatchingResultsFound error = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				null);

		assertNull(error.queryValue());
		assertEquals("No results found for: null", error.message());
		assertEquals("WikidataFetchBroker.java", error.source());
	}

	// ==================== UnableToProcessWikidataEntity Tests ====================

	@Test
	@DisplayName("UnableToProcessWikidataEntity should create with full constructor")
	void unableToProcessWikidataEntityShouldCreateWithFullConstructor() {
		WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity error = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE,
				TEST_SOURCE,
				testTimestamp,
				WikiverseError.ErrorCategory.PROCESSING,
				422,
				testStackTrace);

		assertNotNull(error);
		assertEquals(TEST_MESSAGE, error.message());
		assertEquals(TEST_SOURCE, error.source());
		assertEquals(testTimestamp, error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.PROCESSING, error.category());
		assertEquals(422, error.httpStatusCode());
		assertEquals(testStackTrace, error.stackTrace());
	}

	@Test
	@DisplayName("UnableToProcessWikidataEntity should create with convenience constructor")
	void unableToProcessWikidataEntityShouldCreateWithConvenienceConstructor() {
		WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity error = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE, TEST_SOURCE);

		assertNotNull(error);
		assertEquals(TEST_MESSAGE, error.message());
		assertEquals(TEST_SOURCE, error.source());
		assertNotNull(error.timestamp());
		assertEquals(WikiverseError.ErrorCategory.PROCESSING, error.category());
		assertEquals(422, error.httpStatusCode());
		assertNotNull(error.stackTrace());
		assertEquals(0, error.stackTrace().length);
	}

	@Test
	@DisplayName("UnableToProcessWikidataEntity should implement WikidataServiceError interface")
	void unableToProcessWikidataEntityShouldImplementWikidataServiceErrorInterface() {
		WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity error = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE, TEST_SOURCE);

		assertTrue(error instanceof WikiverseError.WikidataServiceError);
		assertTrue(error instanceof WikiverseError);
	}

	// ==================== Interface Compliance Tests ====================

	@Test
	@DisplayName("All error types should implement WikiverseError interface")
	void allErrorTypesShouldImplementWikiverseErrorInterface() {
		WikiverseError serviceFault = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC);
		WikiverseError apiOffline = new WikiverseError.WikidataServiceError.APIOffline(
				TEST_MESSAGE,
				TEST_SOURCE,
				testStackTrace);
		WikiverseError noResults = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(TEST_QUERY);
		WikiverseError unableToProcess = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE,
				TEST_SOURCE);

		assertTrue(serviceFault instanceof WikiverseError);
		assertTrue(apiOffline instanceof WikiverseError);
		assertTrue(noResults instanceof WikiverseError);
		assertTrue(unableToProcess instanceof WikiverseError);
	}

	@Test
	@DisplayName("WikidataServiceError implementations should implement both interfaces")
	void wikidataServiceErrorImplementationsShouldImplementBothInterfaces() {
		WikiverseError.WikidataServiceError apiOffline = new WikiverseError.WikidataServiceError.APIOffline(
				TEST_MESSAGE,
				TEST_SOURCE,
				testStackTrace);
		WikiverseError.WikidataServiceError noResults = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				TEST_QUERY);
		WikiverseError.WikidataServiceError unableToProcess = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE, TEST_SOURCE);

		assertTrue(apiOffline instanceof WikiverseError.WikidataServiceError);
		assertTrue(apiOffline instanceof WikiverseError);
		assertTrue(noResults instanceof WikiverseError.WikidataServiceError);
		assertTrue(noResults instanceof WikiverseError);
		assertTrue(unableToProcess instanceof WikiverseError.WikidataServiceError);
		assertTrue(unableToProcess instanceof WikiverseError);
	}

	@Test
	@DisplayName("All error types should have non-null required methods")
	void allErrorTypesShouldHaveNonNullRequiredMethods() {
		WikiverseError serviceFault = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC);
		WikiverseError apiOffline = new WikiverseError.WikidataServiceError.APIOffline(
				TEST_MESSAGE,
				TEST_SOURCE,
				testStackTrace);
		WikiverseError noResults = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(TEST_QUERY);
		WikiverseError unableToProcess = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE,
				TEST_SOURCE);

		// All should have required methods
		assertNotNull(serviceFault.message());
		assertNotNull(serviceFault.source());
		assertNotNull(serviceFault.timestamp());
		assertNotNull(serviceFault.category());

		assertNotNull(apiOffline.message());
		assertNotNull(apiOffline.source());
		assertNotNull(apiOffline.timestamp());
		assertNotNull(apiOffline.category());

		assertNotNull(noResults.message());
		assertNotNull(noResults.source());
		assertNotNull(noResults.timestamp());
		assertNotNull(noResults.category());

		assertNotNull(unableToProcess.message());
		assertNotNull(unableToProcess.source());
		assertNotNull(unableToProcess.timestamp());
		assertNotNull(unableToProcess.category());
	}

	// ==================== Enhanced Feature Tests ====================

	@Test
	@DisplayName("Timestamps should be recent")
	void timestampsShouldBeRecent() {
		Instant before = Instant.now();
		WikiverseError.ServiceFault error = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC);
		Instant after = Instant.now();

		assertTrue(error.timestamp().isAfter(before.minusSeconds(1)));
		assertTrue(error.timestamp().isBefore(after.plusSeconds(1)));
	}

	@Test
	@DisplayName("HTTP status codes should be appropriate for error types")
	void httpStatusCodesShouldBeAppropriateForErrorTypes() {
		WikiverseError.WikidataServiceError.NoMatchingResultsFound notFoundError = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				TEST_QUERY);
		WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity processingError = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE, TEST_SOURCE);

		assertEquals(404, notFoundError.httpStatusCode());
		assertEquals(422, processingError.httpStatusCode());
	}

	@Test
	@DisplayName("Error categories should be appropriate for error types")
	void errorCategoriesShouldBeAppropriateForErrorTypes() {
		WikiverseError.ServiceFault serviceError = new WikiverseError.ServiceFault(
				TEST_MESSAGE,
				TEST_SOURCE,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC);
		WikiverseError.WikidataServiceError.APIOffline apiError = new WikiverseError.WikidataServiceError.APIOffline(
				TEST_MESSAGE,
				TEST_SOURCE,
				testStackTrace);
		WikiverseError.WikidataServiceError.NoMatchingResultsFound notFoundError = new WikiverseError.WikidataServiceError.NoMatchingResultsFound(
				TEST_QUERY);
		WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity processingError = new WikiverseError.WikidataServiceError.UnableToProcessWikidataEntity(
				TEST_MESSAGE, TEST_SOURCE);

		assertEquals(WikiverseError.ErrorCategory.INTERNAL_LOGIC, serviceError.category());
		assertEquals(WikiverseError.ErrorCategory.EXTERNAL_SERVICE, apiError.category());
		assertEquals(WikiverseError.ErrorCategory.NOT_FOUND, notFoundError.category());
		assertEquals(WikiverseError.ErrorCategory.PROCESSING, processingError.category());
	}

	// ==================== Edge Case Tests ====================

	@Test
	@DisplayName("Error messages should handle special characters")
	void errorMessagesShouldHandleSpecialCharacters() {
		String specialMessage = "Error with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
		WikiverseError.ServiceFault error = new WikiverseError.ServiceFault(
				specialMessage,
				TEST_SOURCE,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC);

		assertEquals(specialMessage, error.message());
	}

	@Test
	@DisplayName("Error records should handle very long strings")
	void errorRecordsShouldHandleVeryLongStrings() {
		String longMessage = "A".repeat(10000);
		String longSource = "B".repeat(10000);

		WikiverseError.ServiceFault error = new WikiverseError.ServiceFault(
				longMessage,
				longSource,
				WikiverseError.ErrorCategory.INTERNAL_LOGIC);

		assertEquals(longMessage, error.message());
		assertEquals(longSource, error.source());
	}
}
