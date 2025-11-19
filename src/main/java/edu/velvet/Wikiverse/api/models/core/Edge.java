package edu.velvet.Wikiverse.api.models.core;

import org.wikidata.wdtk.datamodel.interfaces.Statement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.velvet.Wikiverse.api.services.wikidata.WikidataSnak;

/**
 * Represents an edge (connection) in the Wikiverse graph structure.
 * An edge represents a relationship between two vertices in the graph,
 * connecting a source vertex to a target vertex with an optional property
 * or label describing the nature of the relationship.
 *
 * <p>
 * This class provides methods to manage edge properties such as:
 * <ul>
 * <li>Source and target vertex identifiers</li>
 * <li>Property identifier for structured relationships</li>
 * <li>Alternative label for non-structured relationships (e.g., dates)</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
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
	private final String sourceID;

	/**
	 * The ID of the target {@link Vertex} that this edge connects to.
	 * Cannot be null or empty.
	 */
	private final String targetID;

	/**
	 * The ID of the {@link Property} that describes the relationship between the
	 * vertices.
	 * Can be null if using a label instead.
	 */
	private final String propertyID;

	/**
	 * An alternative text description of the relationship when a property ID is not
	 * appropriate (e.g. for dates).
	 * Can be null if using a property ID instead.
	 */
	private final String label;

	/**
	 * The statement ID from Wikidata that uniquely identifies the provenance of
	 * this edge.
	 * This value is derived from the Statement object and is used to trace the edge
	 * back to its Wikidata source. When multiple edges (such as main-snaks and
	 * qualifiers)
	 * originate from the same Wikidata statement, they will share the same
	 * statementID.
	 * This is useful for grouping graph relationships and tracking qualified or
	 * annotated edges.
	 */
	private final String statementID;

	/**
	 * Constructs an Edge representing a relationship between two entities based on
	 * a Wikidata statement's main snak.
	 * <p>
	 * This constructor is invoked from:
	 * {@link edu.velvet.Wikiverse.api.services.wikidata.DocumentProcessor#ingestInitialGraphsetDocument}
	 * when creating a 'MainSnak' Edge. The edge connects the statement subject
	 * (source) to the entity ID referenced
	 * in the value of the main snak (target), and uses the associated property ID
	 * for the relationship.
	 * This is applicable when the main snak's value is a confirmed Wikidata entity
	 * (ENTITY_ID).
	 *
	 * @param statement the Wikidata statement from which the edge is sourced
	 * @param mainSnak  the main WikidataSnak that supplies the property and target
	 *                  entity information
	 */
	public Edge(Statement statement, WikidataSnak mainSnak) {
		this.statementID = statement.getStatementId(); // ! Shared ID with any Qualified Edges
		this.sourceID = statement.getSubject().getId();
		this.targetID = mainSnak.getValue().getValue();
		this.propertyID = mainSnak.getProperty().getValue();
		this.label = null;
	}

	/**
	 * Constructs an Edge representing a "qualified" relationship between two
	 * entities based on a Wikidata statement,
	 * its main snak, and a qualifier snak where the qualifier value is a confirmed
	 * Wikidata entity ID.
	 * <p>
	 * This constructor is invoked from:
	 * {@link edu.velvet.Wikiverse.api.services.wikidata.DocumentProcessor#ingestInitialGraphsetDocument}
	 * when generating edges that further qualify a main relationship with
	 * additional entity-linked qualifiers.
	 * The resulting edge represents a path from the main statement's target entity
	 * (as source)
	 * to the qualifier snak's value (as target), with the relationship described by
	 * the qualifier's property.
	 *
	 * @param statement     the Wikidata statement which anchors this qualified edge
	 * @param mainSnak      the main WikidataSnak, providing the primary
	 *                      value/entity from the statement
	 * @param qualifierSnak the qualifier WikidataSnak, representing an additional
	 *                      entity-linked qualifier
	 */
	public Edge(Statement statement, WikidataSnak mainSnak, WikidataSnak qualifierSnak) {
		this.statementID = statement.getStatementId(); // ! Shares parent statement ID with MainSnak
		this.sourceID = mainSnak.getValue().getValue(); // ! Set MainSnak TargetID as Source
		this.targetID = qualifierSnak.getValue().getValue(); // ! Set QualifierSnak as TargetID
		this.propertyID = qualifierSnak.getProperty().getValue(); // ! Property from QualifierSnak
		this.label = null;
	}

	/**
	 * Constructs an Edge object from JSON deserialization.
	 * This constructor is used by Jackson to deserialize Edge from JSON.
	 *
	 * @param sourceID the ID of the source vertex that this edge connects from
	 * @param targetID the ID of the target vertex that this edge connects to
	 * @param propertyID the ID of the property that describes the relationship between the vertices
	 * @param label an alternative text description of the relationship when a property ID is not appropriate
	 * @param statementID the statement ID from Wikidata that uniquely identifies the provenance of this edge
	 */
	@JsonCreator
	public Edge(
		@JsonProperty("sourceID") String sourceID,
		@JsonProperty("targetID") String targetID,
		@JsonProperty("propertyID") String propertyID,
		@JsonProperty("label") String label,
		@JsonProperty("statementID") String statementID
	) {
		this.sourceID = sourceID;
		this.targetID = targetID;
		this.propertyID = propertyID;
		this.label = label;
		this.statementID = statementID;
	}

	/**
	 * Get the { @see Statement } ID value from which this edge was sourced.
	 *
	 * @return the source statement ID.
	 */
	public String getStatementID() {
		return statementID;
	}

	/**
	 * Gets the source {@link Vertex} ID.
	 *
	 * @return the source vertex ID, or null if not set
	 */
	public String getSourceID() {
		return sourceID;
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
	 * Gets the property {@link Property} ID.
	 *
	 * @return the property ID, or null if not set
	 */
	public String getPropertyID() {
		return propertyID;
	}

	/**
	 * Gets the alternative means of describing a relationship's nature when a
	 * property doesn't make sense (used for dates).
	 *
	 * @return the relationship label, or null if not set
	 */
	public String getLabel() {
		return label;
	}
}
