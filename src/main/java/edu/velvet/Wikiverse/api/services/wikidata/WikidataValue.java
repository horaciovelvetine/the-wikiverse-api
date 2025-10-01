package edu.velvet.Wikiverse.api.services.wikidata;

import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.GlobeCoordinatesValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.QuantityValue;
import org.wikidata.wdtk.datamodel.interfaces.StringValue;
import org.wikidata.wdtk.datamodel.interfaces.TimeValue;
import org.wikidata.wdtk.datamodel.interfaces.UnsupportedValue;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.datamodel.interfaces.ValueVisitor;

/**
 * Represents a value from Wikidata with its associated context and type
 * information.
 * This class implements the ValueVisitor pattern to handle different types of
 * Wikidata values,
 * converting them into a unified format for easier processing and
 * serialization.
 *
 * <p>
 * This class provides methods to manage Wikidata value properties such as:
 * <ul>
 * <li>Value content as a string representation</li>
 * <li>Contextual information (units, language codes, etc.)</li>
 * <li>Value type classification for proper handling</li>
 * <li>Null value detection and handling</li>
 * </ul>
 *
 * <p>
 * The class implements ValueVisitor to process different Wikidata value types:
 * <ul>
 * <li>EntityIdValue - for entity references</li>
 * <li>TimeValue - for date/time information</li>
 * <li>StringValue - for plain text values</li>
 * <li>QuantityValue - for numeric values with units</li>
 * <li>MonolingualTextValue - for language-specific text</li>
 * <li>GlobeCoordinatesValue - for geographic coordinates (unsupported)</li>
 * <li>UnsupportedValue - for unknown value types</li>
 * </ul>
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see ValueVisitor
 * @see Value
 */
public class WikidataValue implements ValueVisitor<WikidataValue> {

	/**
	 * The value from Wikidata as a string in whichever format most makes sense
	 * based on the parent type.
	 */
	private String value;

	/**
	 * Provides additional context to the string value stored in value.
	 * For ValueType.Quantity this is the IRI to the unit {@link ItemDocument},
	 * for ValueType.DATE_TIME this is the IRI for the ItemID correlated to this
	 * time inside the Wikidata API.
	 */
	private String context;

	/**
	 * The type of data this value was sourced from. All data is from one of the
	 * {@link Value} sub-interfaces.
	 */
	private ValueType type;

	/**
	 * All the possible (known) value subtypes from Wikidata.
	 * These correspond to the different types of values that can be stored in
	 * Wikidata properties.
	 */
	public enum ValueType {
		STRING,
		DATE_TIME,
		ENTITY_ID,
		QUANTITY,
		NULL,
		MONOLANG
	}

	/**
	 * Gets the string value from Wikidata.
	 *
	 * @return the value as a string, or null if not set
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Gets the contextual information for this value.
	 *
	 * @return the context string (unit IRI, language code, etc.), or null if not
	 *         set
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Gets the type of this Wikidata value.
	 *
	 * @return the ValueType enum indicating the data type
	 */
	public ValueType getType() {
		return type;
	}

	/**
	 * Checks if this value represents a null or empty value from Wikidata.
	 *
	 * @return true if the value type is NULL, false otherwise
	 */
	public boolean isNull() {
		return this.type == ValueType.NULL;
	}

	// ==> Begin Value Visitor Overrides

	/**
	 * Processes an EntityIdValue from Wikidata.
	 *
	 * @param value the EntityIdValue to process, can be null
	 * @return this WikidataValue instance with entity ID data, or null values if
	 *         input is null
	 */
	@Override
	public WikidataValue visit(EntityIdValue value) {
		if (value == null) return nullWikidataValue();

		this.value = value.getId() != null ? value.getId() : "";
		this.context = value.getSiteIri() != null ? value.getSiteIri() : "";
		this.type = ValueType.ENTITY_ID;

		return this;
	}

	/**
	 * Processes a TimeValue from Wikidata.
	 *
	 * @param value the TimeValue to process, can be null
	 * @return this WikidataValue instance with formatted date/time data, or null
	 *         values if input is null
	 */
	@Override
	public WikidataValue visit(TimeValue value) {
		if (value == null) return nullWikidataValue();

		this.value =
			value.getYear() +
			"-" +
			value.getMonth() +
			"-" +
			value.getDay() +
			" (" +
			value.getHour() +
			":" +
			value.getMinute() +
			":" +
			value.getSecond() +
			")";
		this.context = value.getPreferredCalendarModel() != null ? value.getPreferredCalendarModel() : "";
		this.type = ValueType.DATE_TIME;

		return this;
	}

	/**
	 * Processes a StringValue from Wikidata.
	 *
	 * @param value the StringValue to process, can be null
	 * @return this WikidataValue instance with string data, or null values if input
	 *         is null
	 */
	@Override
	public WikidataValue visit(StringValue value) {
		if (value == null) return nullWikidataValue();

		this.value = value.getString() != null ? value.getString() : "";
		this.context = "";
		this.type = ValueType.STRING;
		return this;
	}

	/**
	 * Processes a QuantityValue from Wikidata.
	 *
	 * @param value the QuantityValue to process, can be null
	 * @return this WikidataValue instance with quantity data and unit IRI, or null
	 *         values if input is null
	 */
	@Override
	public WikidataValue visit(QuantityValue value) {
		if (value == null) return nullWikidataValue();

		this.value = value.toString() != null ? value.toString() : "";
		this.context = value.getUnitItemId() != null && value.getUnitItemId().getIri() != null
			? value.getUnitItemId().getIri()
			: "";
		this.type = ValueType.QUANTITY;
		return this;
	}

	/**
	 * Processes a MonolingualTextValue from Wikidata.
	 *
	 * @param value the MonolingualTextValue to process, can be null
	 * @return this WikidataValue instance with text and language code, or null
	 *         values if input is null
	 */
	@Override
	public WikidataValue visit(MonolingualTextValue value) {
		if (value == null) return nullWikidataValue();

		this.value = value.getText() != null ? value.getText() : "";
		this.context = value.getLanguageCode() != null ? value.getLanguageCode() : "";
		this.type = ValueType.MONOLANG;
		return this;
	}

	/**
	 * Processes a GlobeCoordinatesValue from Wikidata.
	 * Currently unsupported - returns null values.
	 *
	 * @param value the GlobeCoordinatesValue to process, can be null
	 * @return this WikidataValue instance with null values
	 */
	@Override
	public WikidataValue visit(GlobeCoordinatesValue value) {
		return nullWikidataValue();
	}

	/**
	 * Processes an UnsupportedValue from Wikidata.
	 * Returns null values for unsupported value types.
	 *
	 * @param value the UnsupportedValue to process, can be null
	 * @return this WikidataValue instance with null values
	 */
	@Override
	public WikidataValue visit(UnsupportedValue value) {
		return nullWikidataValue();
	}

	/**
	 * Helper method to set the needed NULL values when data is found to be NULL
	 * from the Wikidata API.
	 *
	 * @return this WikidataValue instance with null values set
	 */
	private WikidataValue nullWikidataValue() {
		this.value = null;
		this.context = null;
		this.type = ValueType.NULL;
		return this;
	}

	/**
	 * Returns a string representation of this WikidataValue.
	 *
	 * @return a formatted string containing the value, context, and type
	 */
	@Override
	public String toString() {
		return "WikidataValue{" + "value='" + value + '\'' + ", context='" + context + '\'' + ", type=" + type + '}';
	}
}
