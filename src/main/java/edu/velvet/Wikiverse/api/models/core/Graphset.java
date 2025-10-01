package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
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
	private Set<Vertex> vertices = ConcurrentHashMap.newKeySet();

	/**
	 * Collection of properties (relationship types) in this graphset. Thread-safe.
	 */
	private Set<Property> properties = ConcurrentHashMap.newKeySet();

	/** Collection of edges (connections) in this graphset. Thread-safe. */
	private Set<Edge> edges = ConcurrentHashMap.newKeySet();

	/**
	 * Gets the collection of vertices in this graphset.
	 *
	 * @return the set of vertices, never null
	 */
	public Set<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Sets the collection of vertices for this graphset.
	 *
	 * @param vertices the set of vertices, cannot be null
	 * @throws IllegalArgumentException if vertices is null
	 */
	public void setVertices(Set<Vertex> vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("Vertices cannot be null");
		}
		this.vertices = vertices;
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
	 * Sets the collection of properties for this graphset.
	 *
	 * @param properties the set of properties, cannot be null
	 * @throws IllegalArgumentException if properties is null
	 */
	public void setProperties(Set<Property> properties) {
		if (properties == null) {
			throw new IllegalArgumentException("Properties cannot be null");
		}
		this.properties = properties;
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
	 * Sets the collection of edges for this graphset.
	 *
	 * @param edges the set of edges, cannot be null
	 * @throws IllegalArgumentException if edges is null
	 */
	public void setEdges(Set<Edge> edges) {
		if (edges == null) {
			throw new IllegalArgumentException("Edges cannot be null");
		}
		this.edges = edges;
	}

	/**
	 * Adds a vertex to this graphset.
	 *
	 * @param vertex the vertex to add, cannot be null
	 * @return true if the vertex was added (was not already present)
	 * @throws IllegalArgumentException if vertex is null
	 */
	public boolean addVertex(Vertex vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException("Vertex cannot be null");
		}
		return vertices.add(vertex);
	}

	/**
	 * Removes a vertex from this graphset.
	 *
	 * @param vertex the vertex to remove, cannot be null
	 * @return true if the vertex was removed (was present)
	 * @throws IllegalArgumentException if vertex is null
	 */
	public boolean removeVertex(Vertex vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException("Vertex cannot be null");
		}
		return vertices.remove(vertex);
	}

	/**
	 * Adds a property to this graphset.
	 *
	 * @param property the property to add, cannot be null
	 * @return true if the property was added (was not already present)
	 * @throws IllegalArgumentException if property is null
	 */
	public boolean addProperty(Property property) {
		if (property == null) {
			throw new IllegalArgumentException("Property cannot be null");
		}
		return properties.add(property);
	}

	/**
	 * Removes a property from this graphset.
	 *
	 * @param property the property to remove, cannot be null
	 * @return true if the property was removed (was present)
	 * @throws IllegalArgumentException if property is null
	 */
	public boolean removeProperty(Property property) {
		if (property == null) {
			throw new IllegalArgumentException("Property cannot be null");
		}
		return properties.remove(property);
	}

	/**
	 * Adds an edge to this graphset.
	 *
	 * @param edge the edge to add, cannot be null
	 * @return true if the edge was added (was not already present)
	 * @throws IllegalArgumentException if edge is null
	 */
	public boolean addEdge(Edge edge) {
		if (edge == null) {
			throw new IllegalArgumentException("Edge cannot be null");
		}
		return edges.add(edge);
	}

	/**
	 * Removes an edge from this graphset.
	 *
	 * @param edge the edge to remove, cannot be null
	 * @return true if the edge was removed (was present)
	 * @throws IllegalArgumentException if edge is null
	 */
	public boolean removeEdge(Edge edge) {
		if (edge == null) {
			throw new IllegalArgumentException("Edge cannot be null");
		}
		return edges.remove(edge);
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
	 * Gets a list of vertices that are not fetched (maximum 50).
	 * A vertex is considered not fetched if it doesn't have all required data
	 * (label, description, URL, and position not at origin).
	 *
	 * @return a list of unfetched vertices, limited to 50 items maximum
	 */
	@JsonIgnore
	public List<Vertex> getUnfetchedVertices() {
		List<Vertex> unfetchedVertices = new ArrayList<>();

		for (Vertex vertex : vertices) {
			if (!vertex.fetched()) {
				unfetchedVertices.add(vertex);

				// Limit to maximum 50 vertices
				if (unfetchedVertices.size() >= 50) {
					break;
				}
			}
		}

		return unfetchedVertices;
	}
}
