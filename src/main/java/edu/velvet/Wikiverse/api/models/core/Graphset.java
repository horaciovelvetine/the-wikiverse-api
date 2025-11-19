package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vavr.Tuple2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a graphset (collection) in the Wikiverse graph structure.
 * A graphset contains collections of vertices, properties, and edges that form
 * a cohesive subgraph within the larger Wikiverse knowledge graph.
 *
 * <p>
 * This class provides methods to manage graphset collections such as:
 * <ul>
 * <li>Vertex collection for graph nodes</li>
 * <li>Property collection for relationship types</li>
 * <li>Edge collection for graph connections</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping.
 * All collections are thread-safe using ConcurrentHashMap-based sets.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Vertex
 * @see Property
 * @see Edge
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Graphset {

	/** Collection of vertices (nodes) in this graphset. Thread-safe. */
	private final Set<Vertex> vertices = ConcurrentHashMap.newKeySet();

	/**
	 * Collection of properties (relationship types) in this graphset. Thread-safe.
	 */
	private final Set<Property> properties = ConcurrentHashMap.newKeySet();

	/** Collection of edges (connections) in this graphset. Thread-safe. */
	private final Set<Edge> edges = ConcurrentHashMap.newKeySet();

	/**
	 * Gets the collection of vertices in this graphset.
	 *
	 * @return the set of vertices, never null
	 */
	public Set<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Gets the collection of properties in this graphset.
	 *
	 * @return the set of properties, never null
	 */
	public Set<Property> getProperties() {
		return properties;
	}

	/**
	 * Gets the collection of edges in this graphset.
	 *
	 * @return the set of edges, never null
	 */
	public Set<Edge> getEdges() {
		return edges;
	}

	/**
	 * Checks if this graphset contains a specific vertex.
	 *
	 * @param vertex the vertex to check for, cannot be null
	 * @return true if the vertex is in this graphset
	 * @throws IllegalArgumentException if vertex is null
	 */
	public boolean containsVertex(Vertex vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException("Vertex cannot be null");
		}
		return vertices.contains(vertex);
	}

	/**
	 * Checks if this graphset contains a specific property.
	 *
	 * @param property the property to check for, cannot be null
	 * @return true if the property is in this graphset
	 * @throws IllegalArgumentException if property is null
	 */
	public boolean containsProperty(Property property) {
		if (property == null) {
			throw new IllegalArgumentException("Property cannot be null");
		}
		return properties.contains(property);
	}

	/**
	 * Checks if this graphset contains a specific edge.
	 *
	 * @param edge the edge to check for, cannot be null
	 * @return true if the edge is in this graphset
	 * @throws IllegalArgumentException if edge is null
	 */
	public boolean containsEdge(Edge edge) {
		if (edge == null) {
			throw new IllegalArgumentException("Edge cannot be null");
		}
		return edges.contains(edge);
	}

	/**
	 * Gets the number of vertices in this graphset.
	 *
	 * @return the number of vertices
	 */
	public int getVertexCount() {
		return vertices.size();
	}

	/**
	 * Gets the number of properties in this graphset.
	 *
	 * @return the number of properties
	 */
	public int getPropertyCount() {
		return properties.size();
	}

	/**
	 * Gets the number of edges in this graphset.
	 *
	 * @return the number of edges
	 */
	public int getEdgeCount() {
		return edges.size();
	}

	/**
	 * Checks if this graphset is empty (contains no vertices, properties, or
	 * edges).
	 *
	 * @return true if all collections are empty
	 */
	@JsonIgnore
	public boolean isEmpty() {
		return vertices.isEmpty() && properties.isEmpty() && edges.isEmpty();
	}

	/**
	 * Clears all collections in this graphset.
	 */
	public void clear() {
		vertices.clear();
		properties.clear();
		edges.clear();
	}

	/**
	 * Retrieves a vertex from the graphset by its unique identifier (QID).
	 * <p>
	 * This method iterates through the collection of vertices in this graphset to
	 * find
	 * a vertex whose ID matches the supplied QID. If a matching vertex is found, it
	 * is
	 * returned; otherwise, {@code null} is returned.
	 * </p>
	 *
	 * @param QID the unique identifier of the vertex to retrieve (e.g., a Wikidata
	 *            QID)
	 * @return the found {@link Vertex} with the specified ID, or {@code null} if
	 *         not present
	 */
	public Vertex getVertexByID(String QID) {
		for (Vertex vertex : vertices) {
			if (vertex != null && vertex.getId().equals(QID)) {
				return vertex;
			}
		}
		return null;
	}

	/**
	 * Removes all entities from the graphset that match the specified ID.
	 * <p>
	 * This method will:
	 * <ul>
	 * <li>Remove any {@link Vertex} from the vertices collection whose ID equals
	 * the provided value.</li>
	 * <li>Remove any {@link Property} from the properties collection whose ID
	 * equals the provided value.</li>
	 * <li>Remove any {@link Edge} from the edges collection where the property ID,
	 * source ID, or target ID matches the provided value.</li>
	 * </ul>
	 * This is useful for maintaining consistency within the graph when an entity or
	 * property needs to be fully deleted, ensuring all references in edges,
	 * vertices, and properties are removed.
	 * </p>
	 *
	 * @param ID the unique identifier (entity or property ID) to match for removal;
	 *           may be {@code null}
	 */
	public void removeAnyEntitiesMatchingID(String ID) {
		// Remove matching vertex
		vertices.removeIf(
			vertex -> vertex != null && vertex.getId() != null && ID != null && ID.equals(vertex.getId())
		);

		// Remove matching property
		properties.removeIf(
			property -> property != null && property.getId() != null && ID != null && ID.equals(property.getId())
		);

		// Remove any edges where the property ID, source ID, or target ID matches
		edges.removeIf(
			edge ->
				ID != null &&
				((edge.getPropertyID() != null && ID.equals(edge.getPropertyID())) ||
					(edge.getSourceID() != null && ID.equals(edge.getSourceID())) ||
					(edge.getTargetID() != null && ID.equals(edge.getTargetID())))
		);
	}

	/**
	 * Returns a list of IDs (entity or property IDs) referenced by edges in this
	 * graphset
	 * that have not yet been fetched or populated fully in the graphset. An
	 * entity/property
	 * is considered unfetched if it appears in an edge, but does not exist as a
	 * fully
	 * populated Vertex or Property in the graphset collections.
	 * <p>
	 * This method is useful for determining which additional Wikidata entities or
	 * properties
	 * need to be fetched in order to complete the current graphset structure.
	 * </p>
	 *
	 * @return a list of unfetched entity and property IDs referenced by edges but
	 *         not present in the graphset
	 */
	@JsonIgnore
	public List<String> getUnfetchedEntityList() {
		// Use a Set for efficient lookups of fetched IDs (O(1) per lookup)
		Set<String> fetchedIds = new HashSet<>(getAllFetchedIDs());
		// Use a LinkedHashSet to maintain insertion order and avoid duplicates
		Set<String> unfetched = new LinkedHashSet<>();

		for (Edge edge : edges) {
			String propId = edge.getPropertyID();
			String srcId = edge.getSourceID();
			String tgtId = edge.getTargetID();

			// Only add if not already fetched and not null
			if (propId != null && !fetchedIds.contains(propId)) {
				unfetched.add(propId);
			}
			if (srcId != null && !fetchedIds.contains(srcId)) {
				unfetched.add(srcId);
			}
			if (tgtId != null && !fetchedIds.contains(tgtId)) {
				unfetched.add(tgtId);
			}
		}
		return new ArrayList<>(unfetched);
	}

	/**
	 * Retrieves the source and target Vertex objects for the given edge from this
	 * graphset.
	 * <p>
	 * Looks up each endpoint by its ID in the edge. If the corresponding Vertex is
	 * not
	 * present in the graphset, that endpoint will be {@code null}.
	 * </p>
	 *
	 * @param e the {@link Edge} for which to retrieve endpoints
	 * @return a {@link Tuple2} containing the source and target {@link Vertex}
	 *         objects;
	 *         either element of the tuple can be {@code null} if the corresponding
	 *         vertex is
	 *         not present in the graphset
	 */
	@JsonIgnore
	public Tuple2<Vertex, Vertex> getEndpoints(Edge e) {
		Vertex source = getVertexByID(e.getSourceID());
		Vertex target = getVertexByID(e.getTargetID());
		return new Tuple2<>(source, target);
	}

	// !PRIVATE ============================================================>
	// !PRIVATE ============================================================>

	/**
	 * Returns a list of IDs for all entities (vertices) and properties in this
	 * graphset
	 * that are not fully fetched.
	 * <p>
	 * An entity or property is considered "not fully fetched" if its
	 * {@code isFetched()} method
	 * returns {@code false}. Only IDs that are not null are included in the result
	 * list.
	 * This method is useful for identifying which vertices or properties require
	 * additional data fetching or population within the graphset structure.
	 * </p>
	 *
	 * @return a list of IDs for unfetched vertices and properties, or an empty list
	 *         if all are fetched
	 */
	/**
	 * Returns a list of IDs of all fetched vertices and properties in this
	 * graphset.
	 * Only IDs for which isFetched() returns true and are non-null are included.
	 *
	 * @return a list of fully-fetched vertex and property IDs.
	 */
	private List<String> getAllFetchedIDs() {
		List<String> fetchedIds = new ArrayList<>();

		// Add fully-fetched vertex IDs
		for (Vertex vertex : vertices) {
			if (vertex.isFetched()) {
				String id = vertex.getId();
				if (id != null) {
					fetchedIds.add(id);
				}
			}
		}

		// Add fully-fetched property IDs
		for (Property property : properties) {
			if (property.isFetched()) {
				String id = property.getId();
				if (id != null) {
					fetchedIds.add(id);
				}
			}
		}

		return fetchedIds;
	}
}
