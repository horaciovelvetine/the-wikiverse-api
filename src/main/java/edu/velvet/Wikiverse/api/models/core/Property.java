package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;

/**
 * Represents a property in the Wikiverse graph structure.
 * A property defines a characteristic or attribute that can be associated
 * with vertices in the graph, providing structured metadata about concepts.
 *
 * <p>
 * This class provides methods to manage property information such as:
 * <ul>
 * <li>Unique identifier for property identification</li>
 * <li>Human-readable label for display purposes</li>
 * <li>Detailed description explaining the property's purpose</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Property {

	/** The unique identifier for this property. Cannot be null or empty. */
	private final String id;

	/** The display label for this property. Cannot be null or empty. */
	private final String label;

	/** The description text for this property. Can be null. */
	private final String description;

	public Property(PropertyDocument pDoc, String wikiLangTarget) {
		this.id = pDoc.getEntityId().getId();
		this.label = pDoc.findLabel(wikiLangTarget);
		this.description = pDoc.findDescription(wikiLangTarget);
	}

	/**
	 * Gets the unique identifier of this property.
	 *
	 * @return the property ID, or null if not set
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the display label of this property.
	 *
	 * @return the property label, or null if not set
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Gets the description of this property.
	 *
	 * @return the property description, or null if not set
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Determines whether this property has been fully fetched from the data source.
	 * <p>
	 * A property is considered "fetched" if all of its key fields (id, label, and
	 * description)
	 * are non-null. This check is used to verify that the property data is complete
	 * and suitable for use or display.
	 * </p>
	 *
	 * @return {@code true} if the property has non-null id, label, and description;
	 *         {@code false} otherwise
	 */
	public boolean isFetched() {
		return this.id != null && this.label != null && this.description != null;
	}
}
