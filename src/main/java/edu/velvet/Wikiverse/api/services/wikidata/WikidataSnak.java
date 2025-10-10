package edu.velvet.Wikiverse.api.services.wikidata;

import org.wikidata.wdtk.datamodel.implementation.ValueSnakImpl;
import org.wikidata.wdtk.datamodel.interfaces.NoValueSnak;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakVisitor;
import org.wikidata.wdtk.datamodel.interfaces.SomeValueSnak;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;

/**
 * Represents a Wikidata Snak (statement) in the Wikiverse graph structure.
 * A Snak represents a property-value pair or property-only statement from
 * Wikidata,
 * including metadata about the statement type and datatype information.
 *
 * <p>
 * This class provides methods to manage Snak properties such as:
 * <ul>
 * <li>Property identifier and value</li>
 * <li>Statement type (VALUE, NO, SOME)</li>
 * <li>Datatype information for filtering</li>
 * <li>Null state verification</li>
 * </ul>
 *
 * <p>
 * The class implements SnakVisitor to handle different types of Wikidata
 * statements
 * and provides a unified interface for processing various Snak implementations.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see WikidataValue
 * @see SnakVisitor
 */
public class WikidataSnak implements SnakVisitor<WikidataSnak> {

	/**
	 * The property identifier for this Snak. All Snak's are supposed to have a
	 * PropertyIdValue at a minimum.
	 */
	private WikidataValue property;

	/**
	 * The value associated with this Snak. Only ValueSnak's will have a value.
	 */
	private WikidataValue value;

	/**
	 * Used to store a datatype value from the {@link ValueSnakImpl} to pre-filter
	 * out 'external-ids'
	 * which only seem to be available in the WDTK models through the Impl types,
	 * which are used for json serialization/de-serialization.
	 */
	private String datatype;

	/**
	 * Gets the datatype of this Snak.
	 *
	 * <p>
	 * The datatype is extracted from the underlying ValueSnakImpl and is used
	 * for filtering operations to exclude certain types of data (such as
	 * external-ids) from graph processing.
	 *
	 * @return the datatype string, or null if not set
	 */
	public String getDatatype() {
		return this.datatype;
	}

	/**
	 * Gets the property identifier for this Snak.
	 *
	 * <p>
	 * All Snaks are required to have a PropertyIdValue at minimum, which
	 * identifies the property being described in this statement.
	 *
	 * @return the WikidataValue representing the property, or null if not set
	 */
	public WikidataValue getProperty() {
		return this.property;
	}

	/**
	 * Gets the value associated with this Snak.
	 *
	 * <p>
	 * Only ValueSnaks will have an associated value. SomeValueSnaks and
	 * NoValueSnaks will return null for this method.
	 *
	 * @return the WikidataValue representing the value, or null if not set
	 */
	public WikidataValue getValue() {
		return this.value;
	}

	/**
	 * Checks if the Snak is null, meaning it has no value or datatype.
	 *
	 * @return true if the Snak is null, false otherwise
	 */
	public boolean isNull() {
		return this.value == null || this.datatype == null || this.property == null;
	}

	/**
	 * Visits a ValueSnak and extracts its property and value information.
	 * Snak's follow a traditional source, target flow with a Property and Value.
	 *
	 * @param snak the ValueSnak to process
	 * @return this WikidataSnak instance with populated property and value data
	 */
	@Override
	public WikidataSnak visit(ValueSnak snak) {
		if (snak == null)
			return this;

		this.property = getPropertyValue(snak);
		this.value = getValue(snak);
		// type narrow and check for the special datatype attribute... to help filter
		// out irrelevant data
		if (snak instanceof ValueSnakImpl impl) {
			this.datatype = impl.getDatatype();
		}
		return this;
	}

	/**
	 * Visits a SomeValueSnak and extracts its property information.
	 * Snak's have no Value and are likely a part of a larger group (where SomeValue
	 * is a range or unknown).
	 *
	 * @param snak the SomeValueSnak to process
	 * @return this WikidataSnak instance with populated property data
	 */
	@Override
	public WikidataSnak visit(SomeValueSnak snak) {
		if (snak == null)
			return this;

		this.property = getPropertyValue(snak);
		return this;
	}

	/**
	 * Visits a NoValueSnak and extracts its property information.
	 * Snak's literally have no value associated with them.
	 *
	 * @param snak the NoValueSnak to process
	 * @return this WikidataSnak instance with populated property data
	 */
	@Override
	public WikidataSnak visit(NoValueSnak snak) {
		if (snak == null)
			return this;

		this.property = getPropertyValue(snak);
		return this;
	}

	/**
	 * Extracts the property value from a Snak by accepting a WikidataValue visitor.
	 *
	 * @param snak the Snak to extract the property from
	 * @return the WikidataValue representation of the property
	 */
	private WikidataValue getPropertyValue(Snak snak) {
		return snak.getPropertyId().accept(new WikidataValue());
	}

	/**
	 * Extracts the value from a {@link ValueSnak} by accepting a
	 * {@link WikidataValue} visitor.
	 * <p>
	 * This method retrieves the value component of the given {@code ValueSnak} and
	 * processes it
	 * using the {@link WikidataValue} visitor to convert it into a unified value
	 * representation.
	 *
	 * @param snak the {@link ValueSnak} from which to extract the value
	 * @return the {@link WikidataValue} representation of the snak's value
	 */
	private WikidataValue getValue(ValueSnak snak) {
		return snak.getValue().accept(new WikidataValue());
	}

	/**
	 * Returns a string representation of this WikidataSnak.
	 *
	 * @return a formatted string containing property, type, value, and datatype
	 *         information
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("WikidataSnak{");

		// Add property info
		sb.append("property=").append(property);

		// Add value info if present
		if (value != null) {
			sb.append(", value=").append(value);
		}

		// Add datatype if present
		if (datatype != null) {
			sb.append(", datatype=").append(datatype);
		}

		sb.append("}");
		return sb.toString();
	}
}
