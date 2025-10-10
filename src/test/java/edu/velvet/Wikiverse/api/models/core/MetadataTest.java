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
  @DisplayName("Should return layout settings instance")
  void shouldReturnLayoutSettingsInstance() {
    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    assertNotNull(layoutSettings);
    assertSame(layoutSettings, metadata.getLayoutSettings()); // Should return same instance
  }

  @Test
  @DisplayName("Should initialize with default values")
  void shouldInitializeWithDefaultValues() {
    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    assertNotNull(layoutSettings);
    assertEquals(0.5, layoutSettings.getAttractionMultiplier());
    assertEquals(0.5, layoutSettings.getRepulsionMultiplier());
    assertEquals(0.5, layoutSettings.getVertexDensity());
    assertEquals(250, layoutSettings.getMaxLayoutIterations());
    assertEquals(30, layoutSettings.getMaxIterationMovement());
    assertEquals(30, layoutSettings.getTemperatureCurveMultiplier());
    assertEquals(true, layoutSettings.isPrefers3D());
  }

  @Test
  @DisplayName("Should initialize force adjustment multipliers array")
  void shouldInitializeForceAdjustmentMultipliersArray() {
    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    Number[] multipliers = layoutSettings.getForceAdjustmentMultipliers();
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
    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    layoutSettings.setAttractionMultiplier(newMultiplier);
    assertEquals(newMultiplier, layoutSettings.getAttractionMultiplier());
  }

  @Test
  @DisplayName("Should set and get repulsion multiplier successfully")
  void shouldSetAndGetRepulsionMultiplierSuccessfully() {
    Number newMultiplier = 0.7;
    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    layoutSettings.setRepulsionMultiplier(newMultiplier);
    assertEquals(newMultiplier, layoutSettings.getRepulsionMultiplier());
  }

  @Test
  @DisplayName("Should handle different number types for multipliers")
  void shouldHandleDifferentNumberTypesForMultipliers() {
    LayoutSettings layoutSettings = metadata.getLayoutSettings();

    // Test with Integer
    layoutSettings.setAttractionMultiplier(1);
    assertEquals(1, layoutSettings.getAttractionMultiplier());

    // Test with Double
    layoutSettings.setRepulsionMultiplier(2.5);
    assertEquals(2.5, layoutSettings.getRepulsionMultiplier());

    // Test with Float
    layoutSettings.setAttractionMultiplier(3.14f);
    assertEquals(3.14f, layoutSettings.getAttractionMultiplier());
  }

  @Test
  @DisplayName("Should allow null multipliers")
  void shouldAllowNullMultipliers() {
    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    layoutSettings.setAttractionMultiplier(null);
    layoutSettings.setRepulsionMultiplier(null);
    assertNull(layoutSettings.getAttractionMultiplier());
    assertNull(layoutSettings.getRepulsionMultiplier());
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
    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    Number[] originalMultipliers = layoutSettings.getForceAdjustmentMultipliers();
    Number[] retrievedMultipliers = layoutSettings.getForceAdjustmentMultipliers();

    // Should be the same array reference
    assertSame(originalMultipliers, retrievedMultipliers);

    // Verify values are correct
    assertEquals(0.9, retrievedMultipliers[0]);
    assertEquals(1.1, retrievedMultipliers[1]);
  }

  @Test
  @DisplayName("Should handle edge cases for numeric values")
  void shouldHandleEdgeCasesForNumericValues() {
    LayoutSettings layoutSettings = metadata.getLayoutSettings();

    // Test with very small values
    layoutSettings.setAttractionMultiplier(0.001);
    layoutSettings.setRepulsionMultiplier(0.0001);
    assertEquals(0.001, layoutSettings.getAttractionMultiplier());
    assertEquals(0.0001, layoutSettings.getRepulsionMultiplier());

    // Test with large values
    layoutSettings.setAttractionMultiplier(1000.0);
    layoutSettings.setRepulsionMultiplier(999.99);
    assertEquals(1000.0, layoutSettings.getAttractionMultiplier());
    assertEquals(999.99, layoutSettings.getRepulsionMultiplier());

    // Test with negative values
    layoutSettings.setAttractionMultiplier(-0.5);
    layoutSettings.setRepulsionMultiplier(-1.0);
    assertEquals(-0.5, layoutSettings.getAttractionMultiplier());
    assertEquals(-1.0, layoutSettings.getRepulsionMultiplier());
  }

  @Test
  @DisplayName("Should handle complete metadata configuration")
  void shouldHandleCompleteMetadataConfiguration() {
    // Set all mutable properties
    metadata.setOriginID("Q789");
    metadata.setOrigin(testOrigin);
    metadata.setWikiLangTarget("de");

    LayoutSettings layoutSettings = metadata.getLayoutSettings();
    layoutSettings.setAttractionMultiplier(0.75);
    layoutSettings.setRepulsionMultiplier(0.25);

    // Verify all properties
    assertEquals("Q789", metadata.getOriginID());
    assertEquals(testOrigin, metadata.getOrigin());
    assertEquals("de", metadata.getWikiLangTarget());
    assertEquals(0.75, layoutSettings.getAttractionMultiplier());
    assertEquals(0.25, layoutSettings.getRepulsionMultiplier());

    // Verify immutable properties remain unchanged
    assertEquals(0.5, layoutSettings.getVertexDensity());
    assertEquals(250, layoutSettings.getMaxLayoutIterations());
    assertEquals(30, layoutSettings.getMaxIterationMovement());
    assertEquals(30, layoutSettings.getTemperatureCurveMultiplier());
  }

  @Test
  @DisplayName("Should handle multiple metadata instances independently")
  void shouldHandleMultipleMetadataInstancesIndependently() {
    Metadata metadata1 = new Metadata("Q1", "en");
    Metadata metadata2 = new Metadata("Q2", "fr");

    // Configure differently
    LayoutSettings layoutSettings1 = metadata1.getLayoutSettings();
    LayoutSettings layoutSettings2 = metadata2.getLayoutSettings();
    layoutSettings1.setAttractionMultiplier(0.8);
    layoutSettings2.setAttractionMultiplier(0.3);

    // Verify independence
    assertEquals(0.8, layoutSettings1.getAttractionMultiplier());
    assertEquals(0.3, layoutSettings2.getAttractionMultiplier());
    assertNotEquals(layoutSettings1.getAttractionMultiplier(), layoutSettings2.getAttractionMultiplier());

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
    LayoutSettings layoutSettings = metadata.getLayoutSettings();

    // Initial state
    assertEquals("Q123", metadata.getOriginID());
    assertEquals("en", metadata.getWikiLangTarget());
    assertEquals(0.5, layoutSettings.getAttractionMultiplier());

    // Multiple updates
    metadata.setOriginID("Q456");
    metadata.setWikiLangTarget("fr");
    layoutSettings.setAttractionMultiplier(0.8);

    // Verify state
    assertEquals("Q456", metadata.getOriginID());
    assertEquals("fr", metadata.getWikiLangTarget());
    assertEquals(0.8, layoutSettings.getAttractionMultiplier());

    // More updates
    metadata.setOriginID("Q789");
    metadata.setWikiLangTarget("de");
    layoutSettings.setAttractionMultiplier(0.2);

    // Verify final state
    assertEquals("Q789", metadata.getOriginID());
    assertEquals("de", metadata.getWikiLangTarget());
    assertEquals(0.2, layoutSettings.getAttractionMultiplier());
  }
}
