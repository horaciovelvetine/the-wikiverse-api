package edu.velvet.Wikiverse.api.models.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Vertex class.
 * Tests all public methods including getters, setters, validation logic,
 * and state management functionality.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Vertex Tests")
class VertexTest {

	private Vertex vertex;
	private Point3D testPosition;

	@BeforeEach
	void setUp() {
		vertex = new Vertex();
		testPosition = new Point3D(1.0, 2.0, 3.0);
	}

	@Test
	@DisplayName("Should create vertex with default values")
	void shouldCreateVertexWithDefaultValues() {
		assertNotNull(vertex);
		assertNull(vertex.getId());
		assertNull(vertex.getLabel());
		assertNull(vertex.getDescription());
		assertNull(vertex.getUrl());
		assertNotNull(vertex.getPosition());
		assertFalse(vertex.isLocked());
	}

	@Test
	@DisplayName("Should set and get ID successfully")
	void shouldSetAndGetIdSuccessfully() {
		String testId = "test-id-123";
		vertex.setId(testId);
		assertEquals(testId, vertex.getId());
	}

	@Test
	@DisplayName("Should throw exception when setting null ID")
	void shouldThrowExceptionWhenSettingNullId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertex.setId(null));
		assertEquals("ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting empty ID")
	void shouldThrowExceptionWhenSettingEmptyId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertex.setId(""));
		assertEquals("ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting whitespace-only ID")
	void shouldThrowExceptionWhenSettingWhitespaceOnlyId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertex.setId("   "));
		assertEquals("ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should set and get label successfully")
	void shouldSetAndGetLabelSuccessfully() {
		String testLabel = "Test Label";
		vertex.setLabel(testLabel);
		assertEquals(testLabel, vertex.getLabel());
	}

	@Test
	@DisplayName("Should throw exception when setting null label")
	void shouldThrowExceptionWhenSettingNullLabel() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertex.setLabel(null));
		assertEquals("Label cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting empty label")
	void shouldThrowExceptionWhenSettingEmptyLabel() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertex.setLabel(""));
		assertEquals("Label cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should set and get description successfully")
	void shouldSetAndGetDescriptionSuccessfully() {
		String testDescription = "Test Description";
		vertex.setDescription(testDescription);
		assertEquals(testDescription, vertex.getDescription());
	}

	@Test
	@DisplayName("Should allow null description")
	void shouldAllowNullDescription() {
		vertex.setDescription(null);
		assertNull(vertex.getDescription());
	}

	@Test
	@DisplayName("Should set and get URL successfully")
	void shouldSetAndGetUrlSuccessfully() {
		String testUrl = "https://example.com";
		vertex.setUrl(testUrl);
		assertEquals(testUrl, vertex.getUrl());
	}

	@Test
	@DisplayName("Should throw exception when setting null URL")
	void shouldThrowExceptionWhenSettingNullUrl() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertex.setUrl(null));
		assertEquals("URL cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting empty URL")
	void shouldThrowExceptionWhenSettingEmptyUrl() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertex.setUrl(""));
		assertEquals("URL cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should set and get position successfully")
	void shouldSetAndGetPositionSuccessfully() {
		vertex.setPosition(testPosition);
		assertEquals(testPosition, vertex.getPosition());
	}

	@Test
	@DisplayName("Should throw exception when setting null position")
	void shouldThrowExceptionWhenSettingNullPosition() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			vertex.setPosition(null)
		);
		assertEquals("Position cannot be null", exception.getMessage());
	}

	@Test
	@DisplayName("Should have default position initialized")
	void shouldHaveDefaultPositionInitialized() {
		assertNotNull(vertex.getPosition());
		assertTrue(vertex.getPosition() instanceof Point3D);
	}

	@Test
	@DisplayName("Should lock and unlock vertex successfully")
	void shouldLockAndUnlockVertexSuccessfully() {
		assertFalse(vertex.isLocked());

		vertex.lock();
		assertTrue(vertex.isLocked());

		vertex.unlock();
		assertFalse(vertex.isLocked());
	}

	@Test
	@DisplayName("Should toggle lock state multiple times")
	void shouldToggleLockStateMultipleTimes() {
		assertFalse(vertex.isLocked());

		vertex.lock();
		assertTrue(vertex.isLocked());

		vertex.lock();
		assertTrue(vertex.isLocked());

		vertex.unlock();
		assertFalse(vertex.isLocked());

		vertex.unlock();
		assertFalse(vertex.isLocked());
	}

	@Test
	@DisplayName("Should handle complete vertex setup")
	void shouldHandleCompleteVertexSetup() {
		// Set all properties
		vertex.setId("vertex-123");
		vertex.setLabel("Test Vertex");
		vertex.setDescription("A test vertex for unit testing");
		vertex.setUrl("https://en.wikipedia.org/wiki/Test");
		vertex.setPosition(testPosition);

		// Verify all properties
		assertEquals("vertex-123", vertex.getId());
		assertEquals("Test Vertex", vertex.getLabel());
		assertEquals("A test vertex for unit testing", vertex.getDescription());
		assertEquals("https://en.wikipedia.org/wiki/Test", vertex.getUrl());
		assertEquals(testPosition, vertex.getPosition());
		assertFalse(vertex.isLocked());
	}

	@Test
	@DisplayName("Should handle edge cases for string properties")
	void shouldHandleEdgeCasesForStringProperties() {
		// Test with single character
		vertex.setId("a");
		vertex.setLabel("b");
		vertex.setUrl("c");
		assertEquals("a", vertex.getId());
		assertEquals("b", vertex.getLabel());
		assertEquals("c", vertex.getUrl());

		// Test with special characters
		String specialId = "id-with-dashes_and_underscores.123";
		String specialLabel = "Label with spaces & symbols!";
		String specialUrl = "https://example.com/path?param=value#fragment";

		vertex.setId(specialId);
		vertex.setLabel(specialLabel);
		vertex.setUrl(specialUrl);

		assertEquals(specialId, vertex.getId());
		assertEquals(specialLabel, vertex.getLabel());
		assertEquals(specialUrl, vertex.getUrl());
	}

	@Test
	@DisplayName("Should maintain state consistency after multiple operations")
	void shouldMaintainStateConsistencyAfterMultipleOperations() {
		// Initial state
		assertFalse(vertex.isLocked());

		// Set properties
		vertex.setId("test-id");
		vertex.setLabel("Test Label");

		// Lock and verify state
		vertex.lock();
		assertTrue(vertex.isLocked());
		assertEquals("test-id", vertex.getId());
		assertEquals("Test Label", vertex.getLabel());

		// Unlock and verify state
		vertex.unlock();
		assertFalse(vertex.isLocked());
		assertEquals("test-id", vertex.getId());
		assertEquals("Test Label", vertex.getLabel());
	}

	@Test
	@DisplayName("Should handle position updates correctly")
	void shouldHandlePositionUpdatesCorrectly() {
		Point3D originalPosition = vertex.getPosition();
		assertNotNull(originalPosition);

		// Set new position
		vertex.setPosition(testPosition);
		assertEquals(testPosition, vertex.getPosition());
		assertNotEquals(originalPosition, vertex.getPosition());

		// Set another position
		Point3D anotherPosition = new Point3D(4.0, 5.0, 6.0);
		vertex.setPosition(anotherPosition);
		assertEquals(anotherPosition, vertex.getPosition());
		assertNotEquals(testPosition, vertex.getPosition());
	}

	@Test
	@DisplayName("Should return false when vertex is not fetched (default state)")
	void shouldReturnFalseWhenVertexIsNotFetched() {
		// Default vertex should not be fetched
		assertFalse(vertex.fetched());
	}

	@Test
	@DisplayName("Should return false when label is null")
	void shouldReturnFalseWhenLabelIsNull() {
		vertex.setDescription("Test description");
		vertex.setUrl("https://example.com");
		vertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		// Label is null by default
		assertFalse(vertex.fetched());
	}

	@Test
	@DisplayName("Should return false when description is null")
	void shouldReturnFalseWhenDescriptionIsNull() {
		vertex.setLabel("Test Label");
		vertex.setUrl("https://example.com");
		vertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		// Description is null by default
		assertFalse(vertex.fetched());
	}

	@Test
	@DisplayName("Should return false when URL is null")
	void shouldReturnFalseWhenUrlIsNull() {
		vertex.setLabel("Test Label");
		vertex.setDescription("Test description");
		vertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		// URL is null by default
		assertFalse(vertex.fetched());
	}

	@Test
	@DisplayName("Should return false when position is at origin (0,0,0)")
	void shouldReturnFalseWhenPositionIsAtOrigin() {
		vertex.setLabel("Test Label");
		vertex.setDescription("Test description");
		vertex.setUrl("https://example.com");
		// Position is already at origin (0,0,0) by default

		assertFalse(vertex.fetched());
	}

	@Test
	@DisplayName("Should return true when all fields are provided and position is not at origin")
	void shouldReturnTrueWhenAllFieldsAreProvidedAndPositionIsNotAtOrigin() {
		vertex.setLabel("Test Label");
		vertex.setDescription("Test description");
		vertex.setUrl("https://example.com");
		vertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		assertTrue(vertex.fetched());
	}

	@Test
	@DisplayName("Should return true with minimal valid strings for label, description, and URL")
	void shouldReturnTrueWithMinimalValidStringsForLabelDescriptionAndUrl() {
		vertex.setLabel("a");
		vertex.setDescription("b");
		vertex.setUrl("c");
		vertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		assertTrue(vertex.fetched());
	}

	@Test
	@DisplayName("Should return true with single character strings for label, description, and URL")
	void shouldReturnTrueWithSingleCharacterStringsForLabelDescriptionAndUrl() {
		vertex.setLabel("x");
		vertex.setDescription("y");
		vertex.setUrl("z");
		vertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		assertTrue(vertex.fetched());
	}

	@Test
	@DisplayName("Should return false when position is very close to origin but not exactly (0,0,0)")
	void shouldReturnFalseWhenPositionIsVeryCloseToOrigin() {
		vertex.setLabel("Test Label");
		vertex.setDescription("Test description");
		vertex.setUrl("https://example.com");
		vertex.setPosition(new Point3D(1e-10, 1e-10, 1e-10));

		// Should return false because it's within epsilon of origin
		assertFalse(vertex.fetched());
	}

	@Test
	@DisplayName("Should return true when position is just outside epsilon of origin")
	void shouldReturnTrueWhenPositionIsJustOutsideEpsilonOfOrigin() {
		vertex.setLabel("Test Label");
		vertex.setDescription("Test description");
		vertex.setUrl("https://example.com");
		vertex.setPosition(new Point3D(1e-8, 1e-8, 1e-8));

		// Should return true because it's outside epsilon of origin
		assertTrue(vertex.fetched());
	}

	@Test
	@DisplayName("Should handle partial field completion correctly")
	void shouldHandlePartialFieldCompletionCorrectly() {
		// Only label set
		vertex.setLabel("Test Label");
		assertFalse(vertex.fetched());

		// Label and description set
		vertex.setDescription("Test description");
		assertFalse(vertex.fetched());

		// Label, description, and URL set
		vertex.setUrl("https://example.com");
		assertFalse(vertex.fetched());

		// All fields set but position at origin
		assertFalse(vertex.fetched());

		// All fields set and position not at origin
		vertex.setPosition(new Point3D(1.0, 2.0, 3.0));
		assertTrue(vertex.fetched());
	}

	@Test
	@DisplayName("Should handle edge cases for position coordinates")
	void shouldHandleEdgeCasesForPositionCoordinates() {
		vertex.setLabel("Test Label");
		vertex.setDescription("Test description");
		vertex.setUrl("https://example.com");

		// Test with negative coordinates
		vertex.setPosition(new Point3D(-1.0, -2.0, -3.0));
		assertTrue(vertex.fetched());

		// Test with mixed positive and negative coordinates
		vertex.setPosition(new Point3D(1.0, -2.0, 3.0));
		assertTrue(vertex.fetched());

		// Test with very small non-zero coordinates
		vertex.setPosition(new Point3D(0.000001, 0.000001, 0.000001));
		assertTrue(vertex.fetched());
	}
}
