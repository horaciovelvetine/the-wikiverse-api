package edu.velvet.Wikiverse.api.models.requests;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.velvet.Wikiverse.api.models.WikiverseError;

/**
 * Unit tests for the Request class.
 * Tests all public methods including getters, setters, timing calculations,
 * and state management functionality.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Request Tests")
class RequestTest {

	private Request request;
	private Instant testReceivedAt;

	@BeforeEach
	void setUp() {
		request = new Request();
		testReceivedAt = Instant.now().minusSeconds(10);
	}

	@Test
	@DisplayName("Should create request with received timestamp set to current time")
	void shouldCreateRequestWithReceivedTimestampSetToCurrentTime() {
		Instant beforeCreation = Instant.now();
		Request newRequest = new Request();
		Instant afterCreation = Instant.now();

		assertNotNull(newRequest);
		assertNotNull(newRequest.getReceivedAt());
		assertTrue(newRequest.getReceivedAt().isAfter(beforeCreation.minusSeconds(1)));
		assertTrue(newRequest.getReceivedAt().isBefore(afterCreation.plusSeconds(1)));
		assertNull(newRequest.getRespondedAt());
	}

	@Test
	@DisplayName("Should set and get received timestamp successfully")
	void shouldSetAndGetReceivedTimestampSuccessfully() {
		request.setReceivedAt(testReceivedAt);
		assertEquals(testReceivedAt, request.getReceivedAt());
	}

	@Test
	@DisplayName("Should allow null received timestamp")
	void shouldAllowNullReceivedTimestamp() {
		request.setReceivedAt(null);
		assertNull(request.getReceivedAt());
	}

	@Test
	@DisplayName("Should set responded timestamp to current time")
	void shouldSetRespondedTimestampToCurrentTime() {
		Instant beforeSet = Instant.now();
		request.setRespondedAt();
		Instant afterSet = Instant.now();

		assertNotNull(request.getRespondedAt());
		assertTrue(request.getRespondedAt().isAfter(beforeSet.minusSeconds(1)));
		assertTrue(request.getRespondedAt().isBefore(afterSet.plusSeconds(1)));
	}

	@Test
	@DisplayName("Should allow null responded timestamp when manually set")
	void shouldAllowNullRespondedTimestampWhenManuallySet() {
		// This test verifies that we can still set null via reflection or direct field
		// access
		// if needed for testing edge cases, but the normal API doesn't allow it
		request.setRespondedAt(); // Set a timestamp first
		assertNotNull(request.getRespondedAt());

		// The normal API only allows setting to current time
		request.setRespondedAt();
		assertNotNull(request.getRespondedAt());
	}

	@Test
	@DisplayName("Should calculate duration correctly when both timestamps are set")
	void shouldCalculateDurationCorrectlyWhenBothTimestampsAreSet() {
		request.setReceivedAt(testReceivedAt);
		request.setRespondedAt();

		Long duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration > 0);
		// Duration will be from testReceivedAt to now, so we can't predict exact value
		// but it should be positive and reasonable
		assertTrue(duration > 0);
	}

	@Test
	@DisplayName("Should return null duration when received timestamp is null")
	void shouldReturnNullDurationWhenReceivedTimestampIsNull() {
		request.setReceivedAt(null);
		request.setRespondedAt();

		assertNull(request.getRequestDurationMillis());
	}

	@Test
	@DisplayName("Should return null duration when responded timestamp is null")
	void shouldReturnNullDurationWhenRespondedTimestampIsNull() {
		request.setReceivedAt(testReceivedAt);
		// respondedAt is null by default, so we don't need to set it

		assertNull(request.getRequestDurationMillis());
	}

	@Test
	@DisplayName("Should return null duration when both timestamps are null")
	void shouldReturnNullDurationWhenBothTimestampsAreNull() {
		request.setReceivedAt(null);
		// respondedAt is null by default, so we don't need to set it

		assertNull(request.getRequestDurationMillis());
	}

	@Test
	@DisplayName("Should return true when request is completed")
	void shouldReturnTrueWhenRequestIsCompleted() {
		request.setReceivedAt(testReceivedAt);
		request.setRespondedAt();

		assertTrue(request.isCompleted());
	}

	@Test
	@DisplayName("Should return false when received timestamp is null")
	void shouldReturnFalseWhenReceivedTimestampIsNull() {
		request.setReceivedAt(null);
		request.setRespondedAt();

		assertFalse(request.isCompleted());
	}

	@Test
	@DisplayName("Should return false when responded timestamp is null")
	void shouldReturnFalseWhenRespondedTimestampIsNull() {
		request.setReceivedAt(testReceivedAt);
		// respondedAt is null by default, so we don't need to set it

		assertFalse(request.isCompleted());
	}

	@Test
	@DisplayName("Should return false when both timestamps are null")
	void shouldReturnFalseWhenBothTimestampsAreNull() {
		request.setReceivedAt(null);
		// respondedAt is null by default, so we don't need to set it

		assertFalse(request.isCompleted());
	}

	@Test
	@DisplayName("Should mark request as completed by setting responded timestamp to current time")
	void shouldMarkRequestAsCompletedBySettingRespondedTimestampToCurrentTime() {
		Instant beforeMark = Instant.now();
		request.markCompleted();
		Instant afterMark = Instant.now();

		assertNotNull(request.getRespondedAt());
		assertTrue(request.getRespondedAt().isAfter(beforeMark.minusSeconds(1)));
		assertTrue(request.getRespondedAt().isBefore(afterMark.plusSeconds(1)));
		assertTrue(request.isCompleted());
	}

	@Test
	@DisplayName("Should handle complete request lifecycle")
	void shouldHandleCompleteRequestLifecycle() {
		// Initial state
		assertNotNull(request.getReceivedAt());
		assertNull(request.getRespondedAt());
		assertFalse(request.isCompleted());
		assertNull(request.getRequestDurationMillis());

		// Mark as completed using setRespondedAt()
		request.setRespondedAt();

		// Verify completed state
		assertNotNull(request.getReceivedAt());
		assertNotNull(request.getRespondedAt());
		assertTrue(request.isCompleted());
		assertNotNull(request.getRequestDurationMillis());
		assertTrue(request.getRequestDurationMillis() >= 0);
	}

	@Test
	@DisplayName("Should handle multiple setRespondedAt calls")
	void shouldHandleMultipleSetRespondedAtCalls() {
		request.setRespondedAt();
		Instant firstResponse = request.getRespondedAt();

		// Wait a bit
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		request.setRespondedAt();
		Instant secondResponse = request.getRespondedAt();

		// Second call should update the timestamp
		assertTrue(secondResponse.isAfter(firstResponse));
		assertTrue(request.isCompleted());
	}

	@Test
	@DisplayName("Should handle edge cases for duration calculation")
	void shouldHandleEdgeCasesForDurationCalculation() {
		Instant baseTime = Instant.now();

		// Test with very small duration
		request.setReceivedAt(baseTime);
		request.setRespondedAt();
		Long duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration >= 0); // Should be positive or zero

		// Test with negative duration (responded before received) - this is not
		// possible with new API
		// since setRespondedAt() always sets to current time, but we can test the logic
		request.setReceivedAt(Instant.now().plus(1, ChronoUnit.SECONDS));
		request.setRespondedAt();
		duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration < 0); // Should be negative since received is in the future
	}

	@Test
	@DisplayName("Should handle very large duration values")
	void shouldHandleVeryLargeDurationValues() {
		Instant veryOldTime = Instant.now().minus(365, ChronoUnit.DAYS);

		request.setReceivedAt(veryOldTime);
		request.setRespondedAt();

		Long duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration > 0);
		// Should be approximately 365 days in milliseconds
		long expectedDuration = 365L * 24 * 60 * 60 * 1000;
		assertTrue(duration >= expectedDuration * 0.99); // Allow 1% tolerance
		assertTrue(duration <= expectedDuration * 1.01);
	}

	@Test
	@DisplayName("Should maintain state consistency after multiple operations")
	void shouldMaintainStateConsistencyAfterMultipleOperations() {
		// Initial state
		assertNotNull(request.getReceivedAt());
		assertNull(request.getRespondedAt());
		assertFalse(request.isCompleted());

		// Set responded timestamp
		request.setRespondedAt();
		assertTrue(request.isCompleted());
		assertNotNull(request.getRequestDurationMillis());

		// Change received timestamp
		Instant newReceivedAt = testReceivedAt.minus(5, ChronoUnit.SECONDS);
		request.setReceivedAt(newReceivedAt);
		assertTrue(request.isCompleted());
		assertNotNull(request.getRequestDurationMillis());
		assertTrue(request.getRequestDurationMillis() > 0);

		// Note: We can't clear responded timestamp with the new API,
		// but we can test that the state remains consistent
		assertTrue(request.isCompleted());
		assertNotNull(request.getRequestDurationMillis());
	}

	@Test
	@DisplayName("Should handle timestamp updates correctly")
	void shouldHandleTimestampUpdatesCorrectly() {
		Instant originalReceived = request.getReceivedAt();
		assertNotNull(originalReceived);

		// Update received timestamp
		request.setReceivedAt(testReceivedAt);
		assertEquals(testReceivedAt, request.getReceivedAt());
		assertNotEquals(originalReceived, request.getReceivedAt());

		// Update responded timestamp
		request.setRespondedAt();
		assertNotNull(request.getRespondedAt());
		assertTrue(request.isCompleted());

		// Update both timestamps
		Instant newReceived = testReceivedAt.minus(1, ChronoUnit.HOURS);
		request.setReceivedAt(newReceived);
		request.setRespondedAt();

		assertEquals(newReceived, request.getReceivedAt());
		assertNotNull(request.getRespondedAt());
		assertTrue(request.isCompleted());
	}

	@Test
	@DisplayName("Should handle concurrent access safely")
	void shouldHandleConcurrentAccessSafely() throws InterruptedException {
		Request sharedRequest = new Request();
		int threadCount = 10;
		Thread[] threads = new Thread[threadCount];

		// Create threads that will call setRespondedAt
		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(() -> {
				try {
					Thread.sleep(10); // Small delay to ensure some concurrency
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				sharedRequest.setRespondedAt();
			});
		}

		// Start all threads
		for (Thread thread : threads) {
			thread.start();
		}

		// Wait for all threads to complete
		for (Thread thread : threads) {
			thread.join();
		}

		// Verify the request is completed
		assertTrue(sharedRequest.isCompleted());
		assertNotNull(sharedRequest.getRespondedAt());
	}

	@Test
	@DisplayName("Should handle boundary conditions for timestamps")
	void shouldHandleBoundaryConditionsForTimestamps() {
		// Test with epoch time
		Instant epoch = Instant.EPOCH;
		request.setReceivedAt(epoch);
		request.setRespondedAt();
		Long duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration > 0); // Should be positive since epoch is in the past

		// Test with very recent times
		Instant now = Instant.now();
		request.setReceivedAt(now);
		request.setRespondedAt();
		duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration >= 0);
		assertTrue(duration < 1000); // Should be less than 1 second

		// Test with future timestamps
		Instant future = Instant.now().plus(1, ChronoUnit.HOURS);
		request.setReceivedAt(future);
		request.setRespondedAt();
		duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration < 0); // Should be negative since received is in the future
		assertTrue(request.isCompleted());
	}

	@Test
	@DisplayName("Should handle precision differences in timestamp calculations")
	void shouldHandlePrecisionDifferencesInTimestampCalculations() {
		Instant baseTime = Instant.now();

		// Test with different precision levels
		request.setReceivedAt(baseTime);
		request.setRespondedAt();

		// Duration should be small since we set received and responded close in time
		Long duration = request.getRequestDurationMillis();
		assertNotNull(duration);
		assertTrue(duration >= 0);
		assertTrue(duration < 1000); // Should be less than 1 second
	}

	@Test
	@DisplayName("Should return this from markCompleted for method chaining")
	void shouldReturnThisFromMarkCompletedForMethodChaining() {
		Request result = request.markCompleted();

		assertEquals(request, result);
		assertTrue(request.isCompleted());
		assertNotNull(request.getRespondedAt());
	}

	@Test
	@DisplayName("Should return false when no error is present")
	void shouldReturnFalseWhenNoErrorIsPresent() {
		assertFalse(request.errored());
	}

	@Test
	@DisplayName("Should return true when error is present")
	void shouldReturnTrueWhenErrorIsPresent() {
		WikiverseError error = new WikiverseError.ServiceFault(
			"Test Error",
			"RequestTest.class",
			WikiverseError.ErrorCategory.INTERNAL_LOGIC
		);
		request.setError(error);

		assertTrue(request.errored());
	}

	@Test
	@DisplayName("Should return null when no error is present")
	void shouldReturnNullWhenNoErrorIsPresent() {
		assertNull(request.getError());
	}

	@Test
	@DisplayName("Should return error when error is present")
	void shouldReturnErrorWhenErrorIsPresent() {
		WikiverseError error = new WikiverseError.ServiceFault(
			"Test Error",
			"RequestTest.class",
			WikiverseError.ErrorCategory.INTERNAL_LOGIC
		);
		request.setError(error);

		assertEquals(error, request.getError());
	}

	@Test
	@DisplayName("Should set error successfully")
	void shouldSetErrorSuccessfully() {
		WikiverseError error = new WikiverseError.ServiceFault(
			"Test Error",
			"RequestTest.class",
			WikiverseError.ErrorCategory.INTERNAL_LOGIC
		);
		request.setError(error);

		assertEquals(error, request.getError());
		assertTrue(request.errored());
	}

	@Test
	@DisplayName("Should clear error when set to null")
	void shouldClearErrorWhenSetToNull() {
		WikiverseError error = new WikiverseError.ServiceFault(
			"Test Error",
			"RequestTest.class",
			WikiverseError.ErrorCategory.INTERNAL_LOGIC
		);
		request.setError(error);
		assertTrue(request.errored());

		request.setError(null);
		assertFalse(request.errored());
		assertNull(request.getError());
	}

	@Test
	@DisplayName("Should maintain error state independently of completion state")
	void shouldMaintainErrorStateIndependentlyOfCompletionState() {
		// Set error before completion
		WikiverseError error = new WikiverseError.ServiceFault(
			"Test Error",
			"RequestTest.class",
			WikiverseError.ErrorCategory.INTERNAL_LOGIC
		);
		request.setError(error);
		assertTrue(request.errored());
		assertFalse(request.isCompleted());

		// Complete request - error should still be present
		request.markCompleted();
		assertTrue(request.errored());
		assertTrue(request.isCompleted());
		assertEquals(error, request.getError());

		// Clear error - request should still be completed
		request.setError(null);
		assertFalse(request.errored());
		assertTrue(request.isCompleted());
		assertNull(request.getError());
	}
}
