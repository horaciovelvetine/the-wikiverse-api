package edu.velvet.Wikiverse.api.services.wikidata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the IDFilterRecord class.
 * Tests all public methods including constructor validation, getter methods,
 * and record behavior functionality.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("IDFilterRecord Tests")
class IDFilterRecordTest {

  @Test
  @DisplayName("Should create record with all parameters")
  void shouldCreateRecordWithAllParameters() {
    String id = "P31";
    String label = "instance of";
    String notes = "Used for filtering entity types";

    IDFilterRecord record = new IDFilterRecord(id, label, notes);

    assertNotNull(record);
    assertEquals(id, record.id());
    assertEquals(label, record.label());
    assertEquals(notes, record.notes());
  }

  @Test
  @DisplayName("Should create record with null values")
  void shouldCreateRecordWithNullValues() {
    IDFilterRecord record = new IDFilterRecord(null, null, null);

    assertNotNull(record);
    assertNull(record.id());
    assertNull(record.label());
    assertNull(record.notes());
  }

  @Test
  @DisplayName("Should create record with empty strings")
  void shouldCreateRecordWithEmptyStrings() {
    IDFilterRecord record = new IDFilterRecord("", "", "");

    assertNotNull(record);
    assertEquals("", record.id());
    assertEquals("", record.label());
    assertEquals("", record.notes());
  }

  @Test
  @DisplayName("Should create record with mixed null and non-null values")
  void shouldCreateRecordWithMixedNullAndNonNullValues() {
    IDFilterRecord record = new IDFilterRecord("Q42", null, "Test notes");

    assertNotNull(record);
    assertEquals("Q42", record.id());
    assertNull(record.label());
    assertEquals("Test notes", record.notes());
  }

  @Test
  @DisplayName("Should handle property ID values")
  void shouldHandlePropertyIdValues() {
    String propertyId = "P31";
    String label = "instance of";
    String notes = "Property for entity classification";

    IDFilterRecord record = new IDFilterRecord(propertyId, label, notes);

    assertEquals(propertyId, record.id());
    assertEquals(label, record.label());
    assertEquals(notes, record.notes());
  }

  @Test
  @DisplayName("Should handle entity ID values")
  void shouldHandleEntityIdValues() {
    String entityId = "Q42";
    String label = "Douglas Adams";
    String notes = "Author of The Hitchhiker's Guide to the Galaxy";

    IDFilterRecord record = new IDFilterRecord(entityId, label, notes);

    assertEquals(entityId, record.id());
    assertEquals(label, record.label());
    assertEquals(notes, record.notes());
  }

  @Test
  @DisplayName("Should handle special characters in values")
  void shouldHandleSpecialCharactersInValues() {
    String id = "P31-特殊字符_@#$%";
    String label = "Label with émojis 🚀 and symbols!";
    String notes = "Notes with\nnewlines\tand\ttabs";

    IDFilterRecord record = new IDFilterRecord(id, label, notes);

    assertEquals(id, record.id());
    assertEquals(label, record.label());
    assertEquals(notes, record.notes());
  }

  @Test
  @DisplayName("Should handle very long strings")
  void shouldHandleVeryLongStrings() {
    String longId = "P" + "1".repeat(1000);
    String longLabel = "A".repeat(1000);
    String longNotes = "B".repeat(10000);

    IDFilterRecord record = new IDFilterRecord(longId, longLabel, longNotes);

    assertEquals(longId, record.id());
    assertEquals(longLabel, record.label());
    assertEquals(longNotes, record.notes());
  }

  @Test
  @DisplayName("Should support record equality")
  void shouldSupportRecordEquality() {
    IDFilterRecord record1 = new IDFilterRecord("P31", "instance of", "Test notes");
    IDFilterRecord record2 = new IDFilterRecord("P31", "instance of", "Test notes");
    IDFilterRecord record3 = new IDFilterRecord("P32", "subclass of", "Different notes");

    assertEquals(record1, record2);
    assertNotEquals(record1, record3);
    assertNotEquals(record2, record3);
  }

  @Test
  @DisplayName("Should support record hash code")
  void shouldSupportRecordHashCode() {
    IDFilterRecord record1 = new IDFilterRecord("P31", "instance of", "Test notes");
    IDFilterRecord record2 = new IDFilterRecord("P31", "instance of", "Test notes");
    IDFilterRecord record3 = new IDFilterRecord("P32", "subclass of", "Different notes");

    assertEquals(record1.hashCode(), record2.hashCode());
    assertNotEquals(record1.hashCode(), record3.hashCode());
  }

  @Test
  @DisplayName("Should support record toString")
  void shouldSupportRecordToString() {
    IDFilterRecord record = new IDFilterRecord("P31", "instance of", "Test notes");
    String toString = record.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("P31"));
    assertTrue(toString.contains("instance of"));
    assertTrue(toString.contains("Test notes"));
  }

  @Test
  @DisplayName("Should handle whitespace-only values")
  void shouldHandleWhitespaceOnlyValues() {
    IDFilterRecord record = new IDFilterRecord("   ", "\t\n", "  \t  ");

    assertEquals("   ", record.id());
    assertEquals("\t\n", record.label());
    assertEquals("  \t  ", record.notes());
  }

  @Test
  @DisplayName("Should handle numeric ID values")
  void shouldHandleNumericIdValues() {
    IDFilterRecord record = new IDFilterRecord("123456", "Numeric ID", "Test with numbers");

    assertEquals("123456", record.id());
    assertEquals("Numeric ID", record.label());
    assertEquals("Test with numbers", record.notes());
  }

  @Test
  @DisplayName("Should handle mixed case values")
  void shouldHandleMixedCaseValues() {
    IDFilterRecord record = new IDFilterRecord("p31", "Instance Of", "MiXeD cAsE tEsT");

    assertEquals("p31", record.id());
    assertEquals("Instance Of", record.label());
    assertEquals("MiXeD cAsE tEsT", record.notes());
  }

  @Test
  @DisplayName("Should handle unicode characters")
  void shouldHandleUnicodeCharacters() {
    String unicodeId = "P31\u4E2D\u6587";
    String unicodeLabel = "Étiquette en Français";
    String unicodeNotes = "Beschreibung auf Deutsch";

    IDFilterRecord record = new IDFilterRecord(unicodeId, unicodeLabel, unicodeNotes);

    assertEquals(unicodeId, record.id());
    assertEquals(unicodeLabel, record.label());
    assertEquals(unicodeNotes, record.notes());
  }

  @Test
  @DisplayName("Should handle single character values")
  void shouldHandleSingleCharacterValues() {
    IDFilterRecord record = new IDFilterRecord("P", "A", "B");

    assertEquals("P", record.id());
    assertEquals("A", record.label());
    assertEquals("B", record.notes());
  }

  @Test
  @DisplayName("Should handle record with only ID")
  void shouldHandleRecordWithOnlyId() {
    IDFilterRecord record = new IDFilterRecord("P31", null, null);

    assertEquals("P31", record.id());
    assertNull(record.label());
    assertNull(record.notes());
  }

  @Test
  @DisplayName("Should handle record with only label")
  void shouldHandleRecordWithOnlyLabel() {
    IDFilterRecord record = new IDFilterRecord(null, "instance of", null);

    assertNull(record.id());
    assertEquals("instance of", record.label());
    assertNull(record.notes());
  }

  @Test
  @DisplayName("Should handle record with only notes")
  void shouldHandleRecordWithOnlyNotes() {
    IDFilterRecord record = new IDFilterRecord(null, null, "Important notes");

    assertNull(record.id());
    assertNull(record.label());
    assertEquals("Important notes", record.notes());
  }
}
