package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Represents a property in the Wikiverse graph structure.
 * A property defines a characteristic or attribute that can be associated
 * with vertices in the graph, providing structured metadata about concepts.
 *
 * <p>This class provides methods to manage property information such as:
 * <ul>
 *   <li>Unique identifier for property identification</li>
 *   <li>Human-readable label for display purposes</li>
 *   <li>Detailed description explaining the property's purpose</li>
 * </ul>
 *
 * <p>The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Property {

	/** The unique identifier for this property. Cannot be null or empty. */
	private String id;

	/** The display label for this property. Cannot be null or empty. */
	private String label;

	/** The description text for this property. Can be null. */
	private String description;

	/**
	 * Gets the unique identifier of this property.
	 *
	 * @return the property ID, or null if not set
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for this property.
	 *
	 * @param id the unique identifier, cannot be null, empty, or whitespace-only
	 * @throws IllegalArgumentException if id is null, empty, or whitespace-only
	 */
	public void setId(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("ID cannot be null or empty");
		}
		this.id = id;
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
	 * Sets the display label for this property.
	 *
	 * @param label the display label, cannot be null, empty, or whitespace-only
	 * @throws IllegalArgumentException if label is null, empty, or whitespace-only
	 */
	public void setLabel(String label) {
		if (label == null || label.trim().isEmpty()) {
			throw new IllegalArgumentException("Label cannot be null or empty");
		}
		this.label = label;
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
	 * Sets the description for this property.
	 *
	 * @param description the description text, can be null
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
