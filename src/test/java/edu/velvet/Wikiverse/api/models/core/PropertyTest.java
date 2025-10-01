package edu.velvet.Wikiverse.api.models.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Property class.
 * Tests all public methods including getters, setters, validation logic,
 * and state management functionality.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Property Tests")
class PropertyTest {

	private Property property;

	@BeforeEach
	void setUp() {
		property = new Property();
	}

	@Test
	@DisplayName("Should create property with default values")
	void shouldCreatePropertyWithDefaultValues() {
		assertNotNull(property);
		assertNull(property.getId());
		assertNull(property.getLabel());
		assertNull(property.getDescription());
	}

	@Test
	@DisplayName("Should set and get ID successfully")
	void shouldSetAndGetIdSuccessfully() {
		String testId = "property-id-123";
		property.setId(testId);
		assertEquals(testId, property.getId());
	}

	@Test
	@DisplayName("Should throw exception when setting null ID")
	void shouldThrowExceptionWhenSettingNullId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> property.setId(null));
		assertEquals("ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting empty ID")
	void shouldThrowExceptionWhenSettingEmptyId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> property.setId(""));
		assertEquals("ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting whitespace-only ID")
	void shouldThrowExceptionWhenSettingWhitespaceOnlyId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> property.setId("   "));
		assertEquals("ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should set and get label successfully")
	void shouldSetAndGetLabelSuccessfully() {
		String testLabel = "Test Property Label";
		property.setLabel(testLabel);
		assertEquals(testLabel, property.getLabel());
	}

	@Test
	@DisplayName("Should throw exception when setting null label")
	void shouldThrowExceptionWhenSettingNullLabel() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			property.setLabel(null)
		);
		assertEquals("Label cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting empty label")
	void shouldThrowExceptionWhenSettingEmptyLabel() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> property.setLabel(""));
		assertEquals("Label cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting whitespace-only label")
	void shouldThrowExceptionWhenSettingWhitespaceOnlyLabel() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			property.setLabel("   ")
		);
		assertEquals("Label cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should set and get description successfully")
	void shouldSetAndGetDescriptionSuccessfully() {
		String testDescription = "This is a test property description";
		property.setDescription(testDescription);
		assertEquals(testDescription, property.getDescription());
	}

	@Test
	@DisplayName("Should allow null description")
	void shouldAllowNullDescription() {
		property.setDescription(null);
		assertNull(property.getDescription());
	}

	@Test
	@DisplayName("Should allow empty description")
	void shouldAllowEmptyDescription() {
		property.setDescription("");
		assertEquals("", property.getDescription());
	}

	@Test
	@DisplayName("Should allow whitespace-only description")
	void shouldAllowWhitespaceOnlyDescription() {
		property.setDescription("   ");
		assertEquals("   ", property.getDescription());
	}

	@Test
	@DisplayName("Should handle complete property setup")
	void shouldHandleCompletePropertySetup() {
		// Set all properties
		property.setId("property-123");
		property.setLabel("Test Property");
		property.setDescription("A test property for unit testing");

		// Verify all properties
		assertEquals("property-123", property.getId());
		assertEquals("Test Property", property.getLabel());
		assertEquals("A test property for unit testing", property.getDescription());
	}

	@Test
	@DisplayName("Should handle edge cases for string properties")
	void shouldHandleEdgeCasesForStringProperties() {
		// Test with single character
		property.setId("a");
		property.setLabel("b");
		assertEquals("a", property.getId());
		assertEquals("b", property.getLabel());

		// Test with special characters
		String specialId = "id-with-dashes_and_underscores.123";
		String specialLabel = "Label with spaces & symbols!";
		String specialDescription = "Description with special chars: @#$%^&*()";

		property.setId(specialId);
		property.setLabel(specialLabel);
		property.setDescription(specialDescription);

		assertEquals(specialId, property.getId());
		assertEquals(specialLabel, property.getLabel());
		assertEquals(specialDescription, property.getDescription());
	}

	@Test
	@DisplayName("Should maintain state consistency after multiple operations")
	void shouldMaintainStateConsistencyAfterMultipleOperations() {
		// Initial state
		assertNull(property.getId());
		assertNull(property.getLabel());
		assertNull(property.getDescription());

		// Set properties
		property.setId("test-id");
		property.setLabel("Test Label");
		property.setDescription("Test Description");

		// Verify state
		assertEquals("test-id", property.getId());
		assertEquals("Test Label", property.getLabel());
		assertEquals("Test Description", property.getDescription());

		// Update properties
		property.setId("updated-id");
		property.setLabel("Updated Label");
		property.setDescription("Updated Description");

		// Verify updated state
		assertEquals("updated-id", property.getId());
		assertEquals("Updated Label", property.getLabel());
		assertEquals("Updated Description", property.getDescription());
	}

	@Test
	@DisplayName("Should handle property updates correctly")
	void shouldHandlePropertyUpdatesCorrectly() {
		// Set initial values
		property.setId("initial-id");
		property.setLabel("Initial Label");
		property.setDescription("Initial Description");

		assertEquals("initial-id", property.getId());
		assertEquals("Initial Label", property.getLabel());
		assertEquals("Initial Description", property.getDescription());

		// Update with new values
		property.setId("new-id");
		property.setLabel("New Label");
		property.setDescription("New Description");

		assertEquals("new-id", property.getId());
		assertEquals("New Label", property.getLabel());
		assertEquals("New Description", property.getDescription());
	}

	@Test
	@DisplayName("Should handle very long strings")
	void shouldHandleVeryLongStrings() {
		String longId = "a".repeat(1000);
		String longLabel = "b".repeat(1000);
		String longDescription = "c".repeat(10000);

		property.setId(longId);
		property.setLabel(longLabel);
		property.setDescription(longDescription);

		assertEquals(longId, property.getId());
		assertEquals(longLabel, property.getLabel());
		assertEquals(longDescription, property.getDescription());
	}

	@Test
	@DisplayName("Should handle unicode characters")
	void shouldHandleUnicodeCharacters() {
		String unicodeId = "属性-123-αβγ";
		String unicodeLabel = "属性标签 🏷️";
		String unicodeDescription = "属性描述：包含中文、emoji 🎉 和特殊符号";

		property.setId(unicodeId);
		property.setLabel(unicodeLabel);
		property.setDescription(unicodeDescription);

		assertEquals(unicodeId, property.getId());
		assertEquals(unicodeLabel, property.getLabel());
		assertEquals(unicodeDescription, property.getDescription());
	}

	@Test
	@DisplayName("Should handle mixed content in description")
	void shouldHandleMixedContentInDescription() {
		String mixedDescription =
			"Description with:\n- Line breaks\n- Tabs\t\there\n- Numbers: 123\n- Special chars: !@#$%^&*()";
		property.setDescription(mixedDescription);
		assertEquals(mixedDescription, property.getDescription());
	}

	@Test
	@DisplayName("Should validate ID trimming behavior")
	void shouldValidateIdTrimmingBehavior() {
		// Test that whitespace is trimmed before validation
		property.setId("  valid-id  ");
		assertEquals("  valid-id  ", property.getId());

		// Test that only whitespace fails validation
		assertThrows(IllegalArgumentException.class, () -> property.setId("  "));
	}

	@Test
	@DisplayName("Should validate label trimming behavior")
	void shouldValidateLabelTrimmingBehavior() {
		// Test that whitespace is trimmed before validation
		property.setLabel("  valid-label  ");
		assertEquals("  valid-label  ", property.getLabel());

		// Test that only whitespace fails validation
		assertThrows(IllegalArgumentException.class, () -> property.setLabel("  "));
	}

	@Test
	@DisplayName("Should handle multiple property instances")
	void shouldHandleMultiplePropertyInstances() {
		Property property1 = new Property();
		Property property2 = new Property();

		property1.setId("property-1");
		property1.setLabel("Property One");
		property1.setDescription("First property");

		property2.setId("property-2");
		property2.setLabel("Property Two");
		property2.setDescription("Second property");

		assertEquals("property-1", property1.getId());
		assertEquals("Property One", property1.getLabel());
		assertEquals("First property", property1.getDescription());

		assertEquals("property-2", property2.getId());
		assertEquals("Property Two", property2.getLabel());
		assertEquals("Second property", property2.getDescription());

		// Verify independence
		assertNotEquals(property1.getId(), property2.getId());
		assertNotEquals(property1.getLabel(), property2.getLabel());
		assertNotEquals(property1.getDescription(), property2.getDescription());
	}

	@Test
	@DisplayName("Should handle property with minimal valid values")
	void shouldHandlePropertyWithMinimalValidValues() {
		property.setId("a");
		property.setLabel("b");
		property.setDescription("");

		assertEquals("a", property.getId());
		assertEquals("b", property.getLabel());
		assertEquals("", property.getDescription());
	}

	@Test
	@DisplayName("Should handle property with maximum valid values")
	void shouldHandlePropertyWithMaximumValidValues() {
		String maxId = "a".repeat(1000);
		String maxLabel = "b".repeat(1000);
		String maxDescription = "c".repeat(10000);

		property.setId(maxId);
		property.setLabel(maxLabel);
		property.setDescription(maxDescription);

		assertEquals(maxId, property.getId());
		assertEquals(maxLabel, property.getLabel());
		assertEquals(maxDescription, property.getDescription());
	}
}
