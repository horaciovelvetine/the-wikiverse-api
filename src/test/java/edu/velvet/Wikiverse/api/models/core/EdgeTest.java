package edu.velvet.Wikiverse.api.models.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Edge class.
 * Tests all public methods including getters, setters, validation logic,
 * and edge relationship functionality.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Edge Tests")
class EdgeTest {

	private Edge edge;

	@BeforeEach
	void setUp() {
		edge = new Edge();
	}

	@Test
	@DisplayName("Should create edge with default values")
	void shouldCreateEdgeWithDefaultValues() {
		assertNotNull(edge);
		assertNull(edge.getSourceID());
		assertNull(edge.getTargetID());
		assertNull(edge.getPropertyID());
		assertNull(edge.getLabel());
	}

	@Test
	@DisplayName("Should set and get source ID successfully")
	void shouldSetAndGetSourceIdSuccessfully() {
		String testSourceID = "source-vertex-123";
		edge.setSourceID(testSourceID);
		assertEquals(testSourceID, edge.getSourceID());
	}

	@Test
	@DisplayName("Should throw exception when setting null source ID")
	void shouldThrowExceptionWhenSettingNullSourceId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> edge.setSourceID(null));
		assertEquals("Source ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting empty source ID")
	void shouldThrowExceptionWhenSettingEmptySourceId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> edge.setSourceID(""));
		assertEquals("Source ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting whitespace-only source ID")
	void shouldThrowExceptionWhenSettingWhitespaceOnlySourceId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> edge.setSourceID("   "));
		assertEquals("Source ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should set and get target ID successfully")
	void shouldSetAndGetTargetIdSuccessfully() {
		String testTargetID = "target-vertex-456";
		edge.setTargetID(testTargetID);
		assertEquals(testTargetID, edge.getTargetID());
	}

	@Test
	@DisplayName("Should throw exception when setting null target ID")
	void shouldThrowExceptionWhenSettingNullTargetId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> edge.setTargetID(null));
		assertEquals("Target ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting empty target ID")
	void shouldThrowExceptionWhenSettingEmptyTargetId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> edge.setTargetID(""));
		assertEquals("Target ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should throw exception when setting whitespace-only target ID")
	void shouldThrowExceptionWhenSettingWhitespaceOnlyTargetId() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> edge.setTargetID("   "));
		assertEquals("Target ID cannot be null or empty", exception.getMessage());
	}

	@Test
	@DisplayName("Should set and get property ID successfully")
	void shouldSetAndGetPropertyIdSuccessfully() {
		String testPropertyID = "property-789";
		edge.setPropertyID(testPropertyID);
		assertEquals(testPropertyID, edge.getPropertyID());
	}

	@Test
	@DisplayName("Should allow null property ID")
	void shouldAllowNullPropertyId() {
		edge.setPropertyID(null);
		assertNull(edge.getPropertyID());
	}

	@Test
	@DisplayName("Should allow empty property ID")
	void shouldAllowEmptyPropertyId() {
		edge.setPropertyID("");
		assertEquals("", edge.getPropertyID());
	}

	@Test
	@DisplayName("Should set and get label successfully")
	void shouldSetAndGetLabelSuccessfully() {
		String testLabel = "born in";
		edge.setLabel(testLabel);
		assertEquals(testLabel, edge.getLabel());
	}

	@Test
	@DisplayName("Should allow null label")
	void shouldAllowNullLabel() {
		edge.setLabel(null);
		assertNull(edge.getLabel());
	}

	@Test
	@DisplayName("Should allow empty label")
	void shouldAllowEmptyLabel() {
		edge.setLabel("");
		assertEquals("", edge.getLabel());
	}

	@Test
	@DisplayName("Should handle complete edge setup with property ID")
	void shouldHandleCompleteEdgeSetupWithPropertyId() {
		// Set all properties with property ID
		edge.setSourceID("vertex-123");
		edge.setTargetID("vertex-456");
		edge.setPropertyID("property-789");
		edge.setLabel("born in");

		// Verify all properties
		assertEquals("vertex-123", edge.getSourceID());
		assertEquals("vertex-456", edge.getTargetID());
		assertEquals("property-789", edge.getPropertyID());
		assertEquals("born in", edge.getLabel());
	}

	@Test
	@DisplayName("Should handle complete edge setup without property ID")
	void shouldHandleCompleteEdgeSetupWithoutPropertyId() {
		// Set all properties without property ID (using label only)
		edge.setSourceID("vertex-123");
		edge.setTargetID("vertex-456");
		edge.setPropertyID(null);
		edge.setLabel("born in 1990");

		// Verify all properties
		assertEquals("vertex-123", edge.getSourceID());
		assertEquals("vertex-456", edge.getTargetID());
		assertNull(edge.getPropertyID());
		assertEquals("born in 1990", edge.getLabel());
	}

	@Test
	@DisplayName("Should handle edge cases for string properties")
	void shouldHandleEdgeCasesForStringProperties() {
		// Test with single character
		edge.setSourceID("a");
		edge.setTargetID("b");
		edge.setPropertyID("c");
		edge.setLabel("d");
		assertEquals("a", edge.getSourceID());
		assertEquals("b", edge.getTargetID());
		assertEquals("c", edge.getPropertyID());
		assertEquals("d", edge.getLabel());

		// Test with special characters
		String specialSourceID = "source-id-with-dashes_and_underscores.123";
		String specialTargetID = "target-id-with-dashes_and_underscores.456";
		String specialPropertyID = "property-id-with-dashes_and_underscores.789";
		String specialLabel = "Label with spaces & symbols!";

		edge.setSourceID(specialSourceID);
		edge.setTargetID(specialTargetID);
		edge.setPropertyID(specialPropertyID);
		edge.setLabel(specialLabel);

		assertEquals(specialSourceID, edge.getSourceID());
		assertEquals(specialTargetID, edge.getTargetID());
		assertEquals(specialPropertyID, edge.getPropertyID());
		assertEquals(specialLabel, edge.getLabel());
	}

	@Test
	@DisplayName("Should maintain state consistency after multiple operations")
	void shouldMaintainStateConsistencyAfterMultipleOperations() {
		// Initial state
		assertNull(edge.getSourceID());
		assertNull(edge.getTargetID());
		assertNull(edge.getPropertyID());
		assertNull(edge.getLabel());

		// Set properties
		edge.setSourceID("source-1");
		edge.setTargetID("target-1");
		edge.setPropertyID("property-1");
		edge.setLabel("label-1");

		// Verify state
		assertEquals("source-1", edge.getSourceID());
		assertEquals("target-1", edge.getTargetID());
		assertEquals("property-1", edge.getPropertyID());
		assertEquals("label-1", edge.getLabel());

		// Update properties
		edge.setSourceID("source-2");
		edge.setTargetID("target-2");
		edge.setPropertyID(null);
		edge.setLabel("label-2");

		// Verify updated state
		assertEquals("source-2", edge.getSourceID());
		assertEquals("target-2", edge.getTargetID());
		assertNull(edge.getPropertyID());
		assertEquals("label-2", edge.getLabel());
	}

	@Test
	@DisplayName("Should handle property ID and label combinations")
	void shouldHandlePropertyIdAndLabelCombinations() {
		// Test with both property ID and label
		edge.setSourceID("vertex-1");
		edge.setTargetID("vertex-2");
		edge.setPropertyID("property-1");
		edge.setLabel("additional info");

		assertEquals("property-1", edge.getPropertyID());
		assertEquals("additional info", edge.getLabel());

		// Test with only property ID
		edge.setPropertyID("property-2");
		edge.setLabel(null);

		assertEquals("property-2", edge.getPropertyID());
		assertNull(edge.getLabel());

		// Test with only label
		edge.setPropertyID(null);
		edge.setLabel("date relationship");

		assertNull(edge.getPropertyID());
		assertEquals("date relationship", edge.getLabel());
	}

	@Test
	@DisplayName("Should handle whitespace trimming in validation")
	void shouldHandleWhitespaceTrimmingInValidation() {
		// Test that whitespace-only strings are rejected
		assertThrows(IllegalArgumentException.class, () -> edge.setSourceID("   "));
		assertThrows(IllegalArgumentException.class, () -> edge.setTargetID("   "));

		// Test that strings with leading/trailing whitespace are accepted
		edge.setSourceID("  source-id  ");
		edge.setTargetID("  target-id  ");
		assertEquals("  source-id  ", edge.getSourceID());
		assertEquals("  target-id  ", edge.getTargetID());
	}

	@Test
	@DisplayName("Should handle different relationship types")
	void shouldHandleDifferentRelationshipTypes() {
		// Test structured relationship with property ID
		edge.setSourceID("person-1");
		edge.setTargetID("city-1");
		edge.setPropertyID("born-in");
		edge.setLabel(null);

		assertEquals("person-1", edge.getSourceID());
		assertEquals("city-1", edge.getTargetID());
		assertEquals("born-in", edge.getPropertyID());
		assertNull(edge.getLabel());

		// Test unstructured relationship with label
		edge.setSourceID("person-1");
		edge.setTargetID("date-1");
		edge.setPropertyID(null);
		edge.setLabel("born on");

		assertEquals("person-1", edge.getSourceID());
		assertEquals("date-1", edge.getTargetID());
		assertNull(edge.getPropertyID());
		assertEquals("born on", edge.getLabel());
	}

	@Test
	@DisplayName("Should handle edge updates correctly")
	void shouldHandleEdgeUpdatesCorrectly() {
		// Initial setup
		edge.setSourceID("source-1");
		edge.setTargetID("target-1");
		edge.setPropertyID("property-1");
		edge.setLabel("label-1");

		// Update source
		edge.setSourceID("source-2");
		assertEquals("source-2", edge.getSourceID());
		assertEquals("target-1", edge.getTargetID());
		assertEquals("property-1", edge.getPropertyID());
		assertEquals("label-1", edge.getLabel());

		// Update target
		edge.setTargetID("target-2");
		assertEquals("source-2", edge.getSourceID());
		assertEquals("target-2", edge.getTargetID());
		assertEquals("property-1", edge.getPropertyID());
		assertEquals("label-1", edge.getLabel());

		// Update property
		edge.setPropertyID("property-2");
		assertEquals("source-2", edge.getSourceID());
		assertEquals("target-2", edge.getTargetID());
		assertEquals("property-2", edge.getPropertyID());
		assertEquals("label-1", edge.getLabel());

		// Update label
		edge.setLabel("label-2");
		assertEquals("source-2", edge.getSourceID());
		assertEquals("target-2", edge.getTargetID());
		assertEquals("property-2", edge.getPropertyID());
		assertEquals("label-2", edge.getLabel());
	}
}
