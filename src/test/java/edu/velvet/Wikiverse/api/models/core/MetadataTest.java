package edu.velvet.Wikiverse.api.models.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Metadata class.
 * Tests all public methods including constructor validation, getters, setters,
 * and configuration parameter management functionality.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Metadata Tests")
class MetadataTest {

  private Metadata metadata;
  private Vertex testOrigin;

  @BeforeEach
  void setUp() {
    metadata = new Metadata("Q123", "en");
    testOrigin = new Vertex();
    testOrigin.setId("Q123");
    testOrigin.setLabel("Test Origin");
  }

  @Test
  @DisplayName("Should create metadata with constructor parameters")
  void shouldCreateMetadataWithConstructorParameters() {
    assertNotNull(metadata);
    assertEquals("Q123", metadata.getOriginID());
    assertEquals("en", metadata.getWikiLangTarget());
  }

  @Test
  @DisplayName("Should initialize with default values")
  void shouldInitializeWithDefaultValues() {
    assertEquals(0.5, metadata.getAttractionMultiplier());
    assertEquals(0.5, metadata.getRepulsionMultiplier());
    assertEquals(0.5, metadata.getVertexDensity());
    assertEquals(250, metadata.getMaxLayoutIterations());
    assertEquals(30, metadata.getMaxIterationMovement());
    assertEquals(30, metadata.getTemperatureCurveMultiplier());
  }

  @Test
  @DisplayName("Should initialize force adjustment multipliers array")
  void shouldInitializeForceAdjustmentMultipliersArray() {
    Number[] multipliers = metadata.getForceAdjustmentMultipliers();
    assertNotNull(multipliers);
    assertEquals(2, multipliers.length);
    assertEquals(0.9, multipliers[0]);
    assertEquals(1.1, multipliers[1]);
  }

  @Test
  @DisplayName("Should set and get origin ID successfully")
  void shouldSetAndGetOriginIdSuccessfully() {
    String newOriginID = "Q456";
    metadata.setOriginID(newOriginID);
    assertEquals(newOriginID, metadata.getOriginID());
  }

  @Test
  @DisplayName("Should allow null origin ID")
  void shouldAllowNullOriginId() {
    metadata.setOriginID(null);
    assertNull(metadata.getOriginID());
  }

  @Test
  @DisplayName("Should set and get origin vertex successfully")
  void shouldSetAndGetOriginVertexSuccessfully() {
    metadata.setOrigin(testOrigin);
    assertEquals(testOrigin, metadata.getOrigin());
  }

  @Test
  @DisplayName("Should allow null origin vertex")
  void shouldAllowNullOriginVertex() {
    metadata.setOrigin(null);
    assertNull(metadata.getOrigin());
  }

  @Test
  @DisplayName("Should set and get attraction multiplier successfully")
  void shouldSetAndGetAttractionMultiplierSuccessfully() {
    Number newMultiplier = 0.8;
    metadata.setAttractionMultiplier(newMultiplier);
    assertEquals(newMultiplier, metadata.getAttractionMultiplier());
  }

  @Test
  @DisplayName("Should set and get repulsion multiplier successfully")
  void shouldSetAndGetRepulsionMultiplierSuccessfully() {
    Number newMultiplier = 0.7;
    metadata.setRepulsionMultiplier(newMultiplier);
    assertEquals(newMultiplier, metadata.getRepulsionMultiplier());
  }

  @Test
  @DisplayName("Should handle different number types for multipliers")
  void shouldHandleDifferentNumberTypesForMultipliers() {
    // Test with Integer
    metadata.setAttractionMultiplier(1);
    assertEquals(1, metadata.getAttractionMultiplier());

    // Test with Double
    metadata.setRepulsionMultiplier(2.5);
    assertEquals(2.5, metadata.getRepulsionMultiplier());

    // Test with Float
    metadata.setAttractionMultiplier(3.14f);
    assertEquals(3.14f, metadata.getAttractionMultiplier());
  }

  @Test
  @DisplayName("Should allow null multipliers")
  void shouldAllowNullMultipliers() {
    metadata.setAttractionMultiplier(null);
    metadata.setRepulsionMultiplier(null);
    assertNull(metadata.getAttractionMultiplier());
    assertNull(metadata.getRepulsionMultiplier());
  }

  @Test
  @DisplayName("Should set and get wiki language target successfully")
  void shouldSetAndGetWikiLanguageTargetSuccessfully() {
    String newLang = "fr";
    metadata.setWikiLangTarget(newLang);
    assertEquals(newLang, metadata.getWikiLangTarget());
  }

  @Test
  @DisplayName("Should allow null wiki language target")
  void shouldAllowNullWikiLanguageTarget() {
    metadata.setWikiLangTarget(null);
    assertNull(metadata.getWikiLangTarget());
  }

  @Test
  @DisplayName("Should handle different language codes")
  void shouldHandleDifferentLanguageCodes() {
    String[] languages = { "en", "fr", "de", "es", "it", "pt", "ru", "ja", "ko", "zh" };

    for (String lang : languages) {
      metadata.setWikiLangTarget(lang);
      assertEquals(lang, metadata.getWikiLangTarget());
    }
  }

  @Test
  @DisplayName("Should maintain force adjustment multipliers immutability")
  void shouldMaintainForceAdjustmentMultipliersImmutability() {
    Number[] originalMultipliers = metadata.getForceAdjustmentMultipliers();
    Number[] retrievedMultipliers = metadata.getForceAdjustmentMultipliers();

    // Should be the same array reference
    assertSame(originalMultipliers, retrievedMultipliers);

    // Verify values are correct
    assertEquals(0.9, retrievedMultipliers[0]);
    assertEquals(1.1, retrievedMultipliers[1]);
  }

  @Test
  @DisplayName("Should handle edge cases for numeric values")
  void shouldHandleEdgeCasesForNumericValues() {
    // Test with very small values
    metadata.setAttractionMultiplier(0.001);
    metadata.setRepulsionMultiplier(0.0001);
    assertEquals(0.001, metadata.getAttractionMultiplier());
    assertEquals(0.0001, metadata.getRepulsionMultiplier());

    // Test with large values
    metadata.setAttractionMultiplier(1000.0);
    metadata.setRepulsionMultiplier(999.99);
    assertEquals(1000.0, metadata.getAttractionMultiplier());
    assertEquals(999.99, metadata.getRepulsionMultiplier());

    // Test with negative values
    metadata.setAttractionMultiplier(-0.5);
    metadata.setRepulsionMultiplier(-1.0);
    assertEquals(-0.5, metadata.getAttractionMultiplier());
    assertEquals(-1.0, metadata.getRepulsionMultiplier());
  }

  @Test
  @DisplayName("Should handle complete metadata configuration")
  void shouldHandleCompleteMetadataConfiguration() {
    // Set all mutable properties
    metadata.setOriginID("Q789");
    metadata.setOrigin(testOrigin);
    metadata.setAttractionMultiplier(0.75);
    metadata.setRepulsionMultiplier(0.25);
    metadata.setWikiLangTarget("de");

    // Verify all properties
    assertEquals("Q789", metadata.getOriginID());
    assertEquals(testOrigin, metadata.getOrigin());
    assertEquals(0.75, metadata.getAttractionMultiplier());
    assertEquals(0.25, metadata.getRepulsionMultiplier());
    assertEquals("de", metadata.getWikiLangTarget());

    // Verify immutable properties remain unchanged
    assertEquals(0.5, metadata.getVertexDensity());
    assertEquals(250, metadata.getMaxLayoutIterations());
    assertEquals(30, metadata.getMaxIterationMovement());
    assertEquals(30, metadata.getTemperatureCurveMultiplier());
  }

  @Test
  @DisplayName("Should handle multiple metadata instances independently")
  void shouldHandleMultipleMetadataInstancesIndependently() {
    Metadata metadata1 = new Metadata("Q1", "en");
    Metadata metadata2 = new Metadata("Q2", "fr");

    // Configure differently
    metadata1.setAttractionMultiplier(0.8);
    metadata2.setAttractionMultiplier(0.3);

    // Verify independence
    assertEquals(0.8, metadata1.getAttractionMultiplier());
    assertEquals(0.3, metadata2.getAttractionMultiplier());
    assertNotEquals(metadata1.getAttractionMultiplier(), metadata2.getAttractionMultiplier());

    assertEquals("Q1", metadata1.getOriginID());
    assertEquals("Q2", metadata2.getOriginID());
    assertNotEquals(metadata1.getOriginID(), metadata2.getOriginID());
  }

  @Test
  @DisplayName("Should handle special characters in origin ID")
  void shouldHandleSpecialCharactersInOriginId() {
    String specialOriginID = "Q123-特殊字符_@#$%";
    metadata.setOriginID(specialOriginID);
    assertEquals(specialOriginID, metadata.getOriginID());
  }

  @Test
  @DisplayName("Should handle very long origin ID")
  void shouldHandleVeryLongOriginId() {
    String longOriginID = "Q" + "1".repeat(1000);
    metadata.setOriginID(longOriginID);
    assertEquals(longOriginID, metadata.getOriginID());
  }

  @Test
  @DisplayName("Should handle empty string origin ID")
  void shouldHandleEmptyStringOriginId() {
    metadata.setOriginID("");
    assertEquals("", metadata.getOriginID());
  }

  @Test
  @DisplayName("Should handle empty string wiki language target")
  void shouldHandleEmptyStringWikiLanguageTarget() {
    metadata.setWikiLangTarget("");
    assertEquals("", metadata.getWikiLangTarget());
  }

  @Test
  @DisplayName("Should maintain state consistency after multiple operations")
  void shouldMaintainStateConsistencyAfterMultipleOperations() {
    // Initial state
    assertEquals("Q123", metadata.getOriginID());
    assertEquals("en", metadata.getWikiLangTarget());
    assertEquals(0.5, metadata.getAttractionMultiplier());

    // Multiple updates
    metadata.setOriginID("Q456");
    metadata.setWikiLangTarget("fr");
    metadata.setAttractionMultiplier(0.8);

    // Verify state
    assertEquals("Q456", metadata.getOriginID());
    assertEquals("fr", metadata.getWikiLangTarget());
    assertEquals(0.8, metadata.getAttractionMultiplier());

    // More updates
    metadata.setOriginID("Q789");
    metadata.setWikiLangTarget("de");
    metadata.setAttractionMultiplier(0.2);

    // Verify final state
    assertEquals("Q789", metadata.getOriginID());
    assertEquals("de", metadata.getWikiLangTarget());
    assertEquals(0.2, metadata.getAttractionMultiplier());
  }
}
