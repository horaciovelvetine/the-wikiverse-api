package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Represents an edge (connection) in the Wikiverse graph structure.
 * An edge represents a relationship between two vertices in the graph,
 * connecting a source vertex to a target vertex with an optional property
 * or label describing the nature of the relationship.
 *
 * <p>This class provides methods to manage edge properties such as:
 * <ul>
 *   <li>Source and target vertex identifiers</li>
 *   <li>Property identifier for structured relationships</li>
 *   <li>Alternative label for non-structured relationships (e.g., dates)</li>
 * </ul>
 *
 * <p>The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Vertex
 * @see Property
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Edge {

	/**
	 * The ID of the source {@link Vertex} that this edge connects from.
	 * Cannot be null or empty.
	 */
	private String sourceID;

	/**
	 * The ID of the target {@link Vertex} that this edge connects to.
	 * Cannot be null or empty.
	 */
	private String targetID;

	/**
	 * The ID of the {@link Property} that describes the relationship between the vertices.
	 * Can be null if using a label instead.
	 */
	private String propertyID;

	/**
	 * An alternative text description of the relationship when a property ID is not appropriate (e.g. for dates).
	 * Can be null if using a property ID instead.
	 */
	private String label;

	/**
	 * Gets the source {@link Vertex} ID.
	 *
	 * @return the source vertex ID, or null if not set
	 */
	public String getSourceID() {
		return sourceID;
	}

	/**
	 * Sets the source {@link Vertex} ID.
	 *
	 * @param sourceID the source vertex ID, cannot be null, empty, or whitespace-only
	 * @throws IllegalArgumentException if sourceID is null, empty, or whitespace-only
	 */
	public void setSourceID(String sourceID) {
		if (sourceID == null || sourceID.trim().isEmpty()) {
			throw new IllegalArgumentException("Source ID cannot be null or empty");
		}
		this.sourceID = sourceID;
	}

	/**
	 * Gets the target {@link Vertex} ID.
	 *
	 * @return the target vertex ID, or null if not set
	 */
	public String getTargetID() {
		return targetID;
	}

	/**
	 * Sets the target {@link Vertex} ID.
	 *
	 * @param targetID the target vertex ID, cannot be null, empty, or whitespace-only
	 * @throws IllegalArgumentException if targetID is null, empty, or whitespace-only
	 */
	public void setTargetID(String targetID) {
		if (targetID == null || targetID.trim().isEmpty()) {
			throw new IllegalArgumentException("Target ID cannot be null or empty");
		}
		this.targetID = targetID;
	}

	/**
	 * Gets the property {@link Property} ID.
	 *
	 * @return the property ID, or null if not set
	 */
	public String getPropertyID() {
		return propertyID;
	}

	/**
	 * Sets the property {@link Property} ID.
	 *
	 * @param propertyID the property ID, can be null
	 */
	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}

	/**
	 * Gets the alternative means of describing a relationship's nature when a property doesn't make sense (used for dates).
	 *
	 * @return the relationship label, or null if not set
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the alternative means of describing a relationship's nature when a property doesn't make sense (used for dates).
	 *
	 * @param label the relationship label, can be null
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}
