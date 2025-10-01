package edu.velvet.Wikiverse.api.services.wikidata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.GlobeCoordinatesValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.QuantityValue;
import org.wikidata.wdtk.datamodel.interfaces.StringValue;
import org.wikidata.wdtk.datamodel.interfaces.TimeValue;
import org.wikidata.wdtk.datamodel.interfaces.UnsupportedValue;

/**
 * Unit tests for the WikidataValue class.
 * Tests all public methods including visitor pattern implementation,
 * value type handling, and null value detection functionality.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("WikidataValue Tests")
class WikidataValueTest {

  private WikidataValue wikidataValue;

  @BeforeEach
  void setUp() {
    wikidataValue = new WikidataValue();
  }

  @Test
  @DisplayName("Should create WikidataValue with default values")
  void shouldCreateWikidataValueWithDefaultValues() {
    assertNotNull(wikidataValue);
    assertNull(wikidataValue.getValue());
    assertNull(wikidataValue.getContext());
    assertNull(wikidataValue.getType());
    assertFalse(wikidataValue.isNull());
  }

  @Test
  @DisplayName("Should handle EntityIdValue correctly")
  void shouldHandleEntityIdValueCorrectly() {
    EntityIdValue mockEntityId = mock(EntityIdValue.class);
    when(mockEntityId.getId()).thenReturn("Q42");
    when(mockEntityId.getSiteIri()).thenReturn("http://www.wikidata.org/entity/");

    WikidataValue result = wikidataValue.visit(mockEntityId);

    assertNotNull(result);
    assertEquals("Q42", result.getValue());
    assertEquals("http://www.wikidata.org/entity/", result.getContext());
    assertEquals(WikidataValue.ValueType.ENTITY_ID, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle EntityIdValue with null values")
  void shouldHandleEntityIdValueWithNullValues() {
    EntityIdValue mockEntityId = mock(EntityIdValue.class);
    when(mockEntityId.getId()).thenReturn(null);
    when(mockEntityId.getSiteIri()).thenReturn(null);

    WikidataValue result = wikidataValue.visit(mockEntityId);

    assertNotNull(result);
    assertEquals("", result.getValue());
    assertEquals("", result.getContext());
    assertEquals(WikidataValue.ValueType.ENTITY_ID, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle null EntityIdValue")
  void shouldHandleNullEntityIdValue() {
    WikidataValue result = wikidataValue.visit((EntityIdValue) null);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle TimeValue correctly")
  void shouldHandleTimeValueCorrectly() {
    TimeValue mockTime = mock(TimeValue.class);
    when(mockTime.getYear()).thenReturn(2023L);
    when(mockTime.getMonth()).thenReturn((byte) 12);
    when(mockTime.getDay()).thenReturn((byte) 25);
    when(mockTime.getHour()).thenReturn((byte) 14);
    when(mockTime.getMinute()).thenReturn((byte) 30);
    when(mockTime.getSecond()).thenReturn((byte) 45);
    when(mockTime.getPreferredCalendarModel()).thenReturn("http://www.wikidata.org/entity/Q1985727");

    WikidataValue result = wikidataValue.visit(mockTime);

    assertNotNull(result);
    assertEquals("2023-12-25 (14:30:45)", result.getValue());
    assertEquals("http://www.wikidata.org/entity/Q1985727", result.getContext());
    assertEquals(WikidataValue.ValueType.DATE_TIME, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle TimeValue with null calendar model")
  void shouldHandleTimeValueWithNullCalendarModel() {
    TimeValue mockTime = mock(TimeValue.class);
    when(mockTime.getYear()).thenReturn(2023L);
    when(mockTime.getMonth()).thenReturn((byte) 12);
    when(mockTime.getDay()).thenReturn((byte) 25);
    when(mockTime.getHour()).thenReturn((byte) 14);
    when(mockTime.getMinute()).thenReturn((byte) 30);
    when(mockTime.getSecond()).thenReturn((byte) 45);
    when(mockTime.getPreferredCalendarModel()).thenReturn(null);

    WikidataValue result = wikidataValue.visit(mockTime);

    assertNotNull(result);
    assertEquals("2023-12-25 (14:30:45)", result.getValue());
    assertEquals("", result.getContext());
    assertEquals(WikidataValue.ValueType.DATE_TIME, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle null TimeValue")
  void shouldHandleNullTimeValue() {
    WikidataValue result = wikidataValue.visit((TimeValue) null);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle StringValue correctly")
  void shouldHandleStringValueCorrectly() {
    StringValue mockString = mock(StringValue.class);
    when(mockString.getString()).thenReturn("Test string value");

    WikidataValue result = wikidataValue.visit(mockString);

    assertNotNull(result);
    assertEquals("Test string value", result.getValue());
    assertEquals("", result.getContext());
    assertEquals(WikidataValue.ValueType.STRING, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle StringValue with null string")
  void shouldHandleStringValueWithNullString() {
    StringValue mockString = mock(StringValue.class);
    when(mockString.getString()).thenReturn(null);

    WikidataValue result = wikidataValue.visit(mockString);

    assertNotNull(result);
    assertEquals("", result.getValue());
    assertEquals("", result.getContext());
    assertEquals(WikidataValue.ValueType.STRING, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle null StringValue")
  void shouldHandleNullStringValue() {
    WikidataValue result = wikidataValue.visit((StringValue) null);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle QuantityValue correctly")
  void shouldHandleQuantityValueCorrectly() {
    QuantityValue mockQuantity = mock(QuantityValue.class);
    ItemIdValue mockUnit = mock(ItemIdValue.class);
    when(mockQuantity.toString()).thenReturn("42.0");
    when(mockQuantity.getUnitItemId()).thenReturn(mockUnit);
    when(mockUnit.getIri()).thenReturn("http://www.wikidata.org/entity/Q11573");

    WikidataValue result = wikidataValue.visit(mockQuantity);

    assertNotNull(result);
    assertEquals("42.0", result.getValue());
    assertEquals("http://www.wikidata.org/entity/Q11573", result.getContext());
    assertEquals(WikidataValue.ValueType.QUANTITY, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle QuantityValue with null unit")
  void shouldHandleQuantityValueWithNullUnit() {
    QuantityValue mockQuantity = mock(QuantityValue.class);
    when(mockQuantity.toString()).thenReturn("42.0");
    when(mockQuantity.getUnitItemId()).thenReturn(null);

    WikidataValue result = wikidataValue.visit(mockQuantity);

    assertNotNull(result);
    assertEquals("42.0", result.getValue());
    assertEquals("", result.getContext());
    assertEquals(WikidataValue.ValueType.QUANTITY, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle null QuantityValue")
  void shouldHandleNullQuantityValue() {
    WikidataValue result = wikidataValue.visit((QuantityValue) null);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle MonolingualTextValue correctly")
  void shouldHandleMonolingualTextValueCorrectly() {
    MonolingualTextValue mockMonoText = mock(MonolingualTextValue.class);
    when(mockMonoText.getText()).thenReturn("Hello World");
    when(mockMonoText.getLanguageCode()).thenReturn("en");

    WikidataValue result = wikidataValue.visit(mockMonoText);

    assertNotNull(result);
    assertEquals("Hello World", result.getValue());
    assertEquals("en", result.getContext());
    assertEquals(WikidataValue.ValueType.MONOLANG, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle MonolingualTextValue with null values")
  void shouldHandleMonolingualTextValueWithNullValues() {
    MonolingualTextValue mockMonoText = mock(MonolingualTextValue.class);
    when(mockMonoText.toString()).thenReturn(null);
    when(mockMonoText.getLanguageCode()).thenReturn(null);

    WikidataValue result = wikidataValue.visit(mockMonoText);

    assertNotNull(result);
    assertEquals("", result.getValue());
    assertEquals("", result.getContext());
    assertEquals(WikidataValue.ValueType.MONOLANG, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle null MonolingualTextValue")
  void shouldHandleNullMonolingualTextValue() {
    WikidataValue result = wikidataValue.visit((MonolingualTextValue) null);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle GlobeCoordinatesValue as unsupported")
  void shouldHandleGlobeCoordinatesValueAsUnsupported() {
    GlobeCoordinatesValue mockCoords = mock(GlobeCoordinatesValue.class);

    WikidataValue result = wikidataValue.visit(mockCoords);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle null GlobeCoordinatesValue")
  void shouldHandleNullGlobeCoordinatesValue() {
    WikidataValue result = wikidataValue.visit((GlobeCoordinatesValue) null);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle UnsupportedValue as unsupported")
  void shouldHandleUnsupportedValueAsUnsupported() {
    UnsupportedValue mockUnsupported = mock(UnsupportedValue.class);

    WikidataValue result = wikidataValue.visit(mockUnsupported);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should handle null UnsupportedValue")
  void shouldHandleNullUnsupportedValue() {
    WikidataValue result = wikidataValue.visit((UnsupportedValue) null);

    assertNotNull(result);
    assertNull(result.getValue());
    assertNull(result.getContext());
    assertEquals(WikidataValue.ValueType.NULL, result.getType());
    assertTrue(result.isNull());
  }

  @Test
  @DisplayName("Should support toString method")
  void shouldSupportToStringMethod() {
    EntityIdValue mockEntityId = mock(EntityIdValue.class);
    when(mockEntityId.getId()).thenReturn("Q42");
    when(mockEntityId.getSiteIri()).thenReturn("http://www.wikidata.org/entity/");

    WikidataValue result = wikidataValue.visit(mockEntityId);
    String toString = result.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("Q42"));
    assertTrue(toString.contains("http://www.wikidata.org/entity/"));
    assertTrue(toString.contains("ENTITY_ID"));
  }

  @Test
  @DisplayName("Should handle all ValueType enum values")
  void shouldHandleAllValueTypeEnumValues() {
    WikidataValue.ValueType[] types = WikidataValue.ValueType.values();

    assertEquals(6, types.length);
    assertTrue(java.util.Arrays.asList(types).contains(WikidataValue.ValueType.STRING));
    assertTrue(java.util.Arrays.asList(types).contains(WikidataValue.ValueType.DATE_TIME));
    assertTrue(java.util.Arrays.asList(types).contains(WikidataValue.ValueType.ENTITY_ID));
    assertTrue(java.util.Arrays.asList(types).contains(WikidataValue.ValueType.QUANTITY));
    assertTrue(java.util.Arrays.asList(types).contains(WikidataValue.ValueType.NULL));
    assertTrue(java.util.Arrays.asList(types).contains(WikidataValue.ValueType.MONOLANG));
  }

  @Test
  @DisplayName("Should handle special characters in string values")
  void shouldHandleSpecialCharactersInStringValues() {
    StringValue mockString = mock(StringValue.class);
    when(mockString.getString()).thenReturn("Special chars: émojis 🚀 and symbols!@#$%");

    WikidataValue result = wikidataValue.visit(mockString);

    assertNotNull(result);
    assertEquals("Special chars: émojis 🚀 and symbols!@#$%", result.getValue());
    assertEquals(WikidataValue.ValueType.STRING, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle very long string values")
  void shouldHandleVeryLongStringValues() {
    StringValue mockString = mock(StringValue.class);
    String longString = "A".repeat(10000);
    when(mockString.getString()).thenReturn(longString);

    WikidataValue result = wikidataValue.visit(mockString);

    assertNotNull(result);
    assertEquals(longString, result.getValue());
    assertEquals(WikidataValue.ValueType.STRING, result.getType());
    assertFalse(result.isNull());
  }

  @Test
  @DisplayName("Should handle empty string values")
  void shouldHandleEmptyStringValues() {
    StringValue mockString = mock(StringValue.class);
    when(mockString.getString()).thenReturn("");

    WikidataValue result = wikidataValue.visit(mockString);

    assertNotNull(result);
    assertEquals("", result.getValue());
    assertEquals("", result.getContext());
    assertEquals(WikidataValue.ValueType.STRING, result.getType());
    assertFalse(result.isNull());
  }
}
