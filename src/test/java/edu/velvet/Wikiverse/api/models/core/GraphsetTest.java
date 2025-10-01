package edu.velvet.Wikiverse.api.models.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Graphset class.
 *
 * <p>This test class verifies the functionality of the Graphset class including:
 * <ul>
 *   <li>Collection management (add, remove, contains)</li>
 *   <li>Getter and setter methods</li>
 *   <li>Null parameter validation</li>
 *   <li>Count and empty state methods</li>
 *   <li>Clear functionality</li>
 * </ul>
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 */
public class GraphsetTest {

	private Graphset graphset;
	private Vertex testVertex;
	private Property testProperty;
	private Edge testEdge;

	@BeforeEach
	void setUp() {
		graphset = new Graphset();

		// Create test objects
		testVertex = new Vertex();
		testVertex.setId("vertex1");
		testVertex.setLabel("Test Vertex");
		testVertex.setUrl("https://example.com/vertex1");

		testProperty = new Property();
		testProperty.setId("property1");
		testProperty.setLabel("Test Property");
		testProperty.setDescription("A test property");

		testEdge = new Edge();
		testEdge.setSourceID("vertex1");
		testEdge.setTargetID("vertex2");
		testEdge.setPropertyID("property1");
	}

	@Test
	void testDefaultConstructor() {
		assertNotNull(graphset);
		assertTrue(graphset.isEmpty());
		assertEquals(0, graphset.getVertexCount());
		assertEquals(0, graphset.getPropertyCount());
		assertEquals(0, graphset.getEdgeCount());
	}

	@Test
	void testGetVertices() {
		Set<Vertex> vertices = graphset.getVertices();
		assertNotNull(vertices);
		assertTrue(vertices.isEmpty());
	}

	@Test
	void testSetVertices() {
		Set<Vertex> newVertices = new HashSet<>();
		newVertices.add(testVertex);

		graphset.setVertices(newVertices);

		assertEquals(1, graphset.getVertexCount());
		assertTrue(graphset.containsVertex(testVertex));
	}

	@Test
	void testSetVerticesNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.setVertices(null);
		});
	}

	@Test
	void testGetProperties() {
		Set<Property> properties = graphset.getProperties();
		assertNotNull(properties);
		assertTrue(properties.isEmpty());
	}

	@Test
	void testSetProperties() {
		Set<Property> newProperties = new HashSet<>();
		newProperties.add(testProperty);

		graphset.setProperties(newProperties);

		assertEquals(1, graphset.getPropertyCount());
		assertTrue(graphset.containsProperty(testProperty));
	}

	@Test
	void testSetPropertiesNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.setProperties(null);
		});
	}

	@Test
	void testGetEdges() {
		Set<Edge> edges = graphset.getEdges();
		assertNotNull(edges);
		assertTrue(edges.isEmpty());
	}

	@Test
	void testSetEdges() {
		Set<Edge> newEdges = new HashSet<>();
		newEdges.add(testEdge);

		graphset.setEdges(newEdges);

		assertEquals(1, graphset.getEdgeCount());
		assertTrue(graphset.containsEdge(testEdge));
	}

	@Test
	void testSetEdgesNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.setEdges(null);
		});
	}

	@Test
	void testAddVertex() {
		assertTrue(graphset.addVertex(testVertex));
		assertEquals(1, graphset.getVertexCount());
		assertTrue(graphset.containsVertex(testVertex));
	}

	@Test
	void testAddVertexDuplicate() {
		graphset.addVertex(testVertex);
		assertFalse(graphset.addVertex(testVertex));
		assertEquals(1, graphset.getVertexCount());
	}

	@Test
	void testAddVertexNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.addVertex(null);
		});
	}

	@Test
	void testRemoveVertex() {
		graphset.addVertex(testVertex);
		assertTrue(graphset.removeVertex(testVertex));
		assertEquals(0, graphset.getVertexCount());
		assertFalse(graphset.containsVertex(testVertex));
	}

	@Test
	void testRemoveVertexNotPresent() {
		assertFalse(graphset.removeVertex(testVertex));
	}

	@Test
	void testRemoveVertexNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.removeVertex(null);
		});
	}

	@Test
	void testAddProperty() {
		assertTrue(graphset.addProperty(testProperty));
		assertEquals(1, graphset.getPropertyCount());
		assertTrue(graphset.containsProperty(testProperty));
	}

	@Test
	void testAddPropertyDuplicate() {
		graphset.addProperty(testProperty);
		assertFalse(graphset.addProperty(testProperty));
		assertEquals(1, graphset.getPropertyCount());
	}

	@Test
	void testAddPropertyNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.addProperty(null);
		});
	}

	@Test
	void testRemoveProperty() {
		graphset.addProperty(testProperty);
		assertTrue(graphset.removeProperty(testProperty));
		assertEquals(0, graphset.getPropertyCount());
		assertFalse(graphset.containsProperty(testProperty));
	}

	@Test
	void testRemovePropertyNotPresent() {
		assertFalse(graphset.removeProperty(testProperty));
	}

	@Test
	void testRemovePropertyNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.removeProperty(null);
		});
	}

	@Test
	void testAddEdge() {
		assertTrue(graphset.addEdge(testEdge));
		assertEquals(1, graphset.getEdgeCount());
		assertTrue(graphset.containsEdge(testEdge));
	}

	@Test
	void testAddEdgeDuplicate() {
		graphset.addEdge(testEdge);
		assertFalse(graphset.addEdge(testEdge));
		assertEquals(1, graphset.getEdgeCount());
	}

	@Test
	void testAddEdgeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.addEdge(null);
		});
	}

	@Test
	void testRemoveEdge() {
		graphset.addEdge(testEdge);
		assertTrue(graphset.removeEdge(testEdge));
		assertEquals(0, graphset.getEdgeCount());
		assertFalse(graphset.containsEdge(testEdge));
	}

	@Test
	void testRemoveEdgeNotPresent() {
		assertFalse(graphset.removeEdge(testEdge));
	}

	@Test
	void testRemoveEdgeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.removeEdge(null);
		});
	}

	@Test
	void testContainsVertex() {
		assertFalse(graphset.containsVertex(testVertex));
		graphset.addVertex(testVertex);
		assertTrue(graphset.containsVertex(testVertex));
	}

	@Test
	void testContainsVertexNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.containsVertex(null);
		});
	}

	@Test
	void testContainsProperty() {
		assertFalse(graphset.containsProperty(testProperty));
		graphset.addProperty(testProperty);
		assertTrue(graphset.containsProperty(testProperty));
	}

	@Test
	void testContainsPropertyNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.containsProperty(null);
		});
	}

	@Test
	void testContainsEdge() {
		assertFalse(graphset.containsEdge(testEdge));
		graphset.addEdge(testEdge);
		assertTrue(graphset.containsEdge(testEdge));
	}

	@Test
	void testContainsEdgeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphset.containsEdge(null);
		});
	}

	@Test
	void testGetVertexCount() {
		assertEquals(0, graphset.getVertexCount());
		graphset.addVertex(testVertex);
		assertEquals(1, graphset.getVertexCount());
	}

	@Test
	void testGetPropertyCount() {
		assertEquals(0, graphset.getPropertyCount());
		graphset.addProperty(testProperty);
		assertEquals(1, graphset.getPropertyCount());
	}

	@Test
	void testGetEdgeCount() {
		assertEquals(0, graphset.getEdgeCount());
		graphset.addEdge(testEdge);
		assertEquals(1, graphset.getEdgeCount());
	}

	@Test
	void testIsEmpty() {
		assertTrue(graphset.isEmpty());

		graphset.addVertex(testVertex);
		assertFalse(graphset.isEmpty());

		graphset.removeVertex(testVertex);
		assertTrue(graphset.isEmpty());
	}

	@Test
	void testIsEmptyWithMultipleCollections() {
		assertTrue(graphset.isEmpty());

		graphset.addVertex(testVertex);
		assertFalse(graphset.isEmpty());

		graphset.addProperty(testProperty);
		assertFalse(graphset.isEmpty());

		graphset.addEdge(testEdge);
		assertFalse(graphset.isEmpty());
	}

	@Test
	void testClear() {
		graphset.addVertex(testVertex);
		graphset.addProperty(testProperty);
		graphset.addEdge(testEdge);

		assertFalse(graphset.isEmpty());

		graphset.clear();

		assertTrue(graphset.isEmpty());
		assertEquals(0, graphset.getVertexCount());
		assertEquals(0, graphset.getPropertyCount());
		assertEquals(0, graphset.getEdgeCount());
	}

	@Test
	void testClearEmptyGraphset() {
		assertTrue(graphset.isEmpty());
		graphset.clear();
		assertTrue(graphset.isEmpty());
	}

	@Test
	void testMultipleVertices() {
		Vertex vertex2 = new Vertex();
		vertex2.setId("vertex2");
		vertex2.setLabel("Test Vertex 2");
		vertex2.setUrl("https://example.com/vertex2");

		graphset.addVertex(testVertex);
		graphset.addVertex(vertex2);

		assertEquals(2, graphset.getVertexCount());
		assertTrue(graphset.containsVertex(testVertex));
		assertTrue(graphset.containsVertex(vertex2));
	}

	@Test
	void testMultipleProperties() {
		Property property2 = new Property();
		property2.setId("property2");
		property2.setLabel("Test Property 2");
		property2.setDescription("Another test property");

		graphset.addProperty(testProperty);
		graphset.addProperty(property2);

		assertEquals(2, graphset.getPropertyCount());
		assertTrue(graphset.containsProperty(testProperty));
		assertTrue(graphset.containsProperty(property2));
	}

	@Test
	void testMultipleEdges() {
		Edge edge2 = new Edge();
		edge2.setSourceID("vertex2");
		edge2.setTargetID("vertex3");
		edge2.setLabel("Custom relationship");

		graphset.addEdge(testEdge);
		graphset.addEdge(edge2);

		assertEquals(2, graphset.getEdgeCount());
		assertTrue(graphset.containsEdge(testEdge));
		assertTrue(graphset.containsEdge(edge2));
	}

	@Test
	void testMixedOperations() {
		// Add one of each type
		graphset.addVertex(testVertex);
		graphset.addProperty(testProperty);
		graphset.addEdge(testEdge);

		assertEquals(1, graphset.getVertexCount());
		assertEquals(1, graphset.getPropertyCount());
		assertEquals(1, graphset.getEdgeCount());
		assertFalse(graphset.isEmpty());

		// Remove one of each type
		graphset.removeVertex(testVertex);
		graphset.removeProperty(testProperty);
		graphset.removeEdge(testEdge);

		assertEquals(0, graphset.getVertexCount());
		assertEquals(0, graphset.getPropertyCount());
		assertEquals(0, graphset.getEdgeCount());
		assertTrue(graphset.isEmpty());
	}

	@Test
	void testThreadSafety() {
		// This test verifies that the collections are thread-safe
		// by using ConcurrentHashMap-based sets
		Set<Vertex> vertices = graphset.getVertices();
		assertTrue(vertices instanceof java.util.concurrent.ConcurrentHashMap.KeySetView);

		Set<Property> properties = graphset.getProperties();
		assertTrue(properties instanceof java.util.concurrent.ConcurrentHashMap.KeySetView);

		Set<Edge> edges = graphset.getEdges();
		assertTrue(edges instanceof java.util.concurrent.ConcurrentHashMap.KeySetView);
	}

	@Test
	@DisplayName("Should return empty list when no vertices are present")
	void shouldReturnEmptyListWhenNoVerticesArePresent() {
		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertTrue(unfetchedVertices.isEmpty());
	}

	@Test
	@DisplayName("Should return empty list when all vertices are fetched")
	void shouldReturnEmptyListWhenAllVerticesAreFetched() {
		// Create a fully fetched vertex
		Vertex fetchedVertex = new Vertex();
		fetchedVertex.setId("vertex-1");
		fetchedVertex.setLabel("Test Label");
		fetchedVertex.setDescription("Test Description");
		fetchedVertex.setUrl("https://example.com");
		fetchedVertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		graphset.addVertex(fetchedVertex);

		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertTrue(unfetchedVertices.isEmpty());
	}

	@Test
	@DisplayName("Should return unfetched vertices when some are not fetched")
	void shouldReturnUnfetchedVerticesWhenSomeAreNotFetched() {
		// Create a fetched vertex
		Vertex fetchedVertex = new Vertex();
		fetchedVertex.setId("vertex-1");
		fetchedVertex.setLabel("Fetched Label");
		fetchedVertex.setDescription("Fetched Description");
		fetchedVertex.setUrl("https://example.com");
		fetchedVertex.setPosition(new Point3D(1.0, 2.0, 3.0));

		// Create an unfetched vertex (missing label)
		Vertex unfetchedVertex1 = new Vertex();
		unfetchedVertex1.setId("vertex-2");
		unfetchedVertex1.setDescription("Unfetched Description");
		unfetchedVertex1.setUrl("https://example.com");
		unfetchedVertex1.setPosition(new Point3D(1.0, 2.0, 3.0));

		// Create another unfetched vertex (position at origin)
		Vertex unfetchedVertex2 = new Vertex();
		unfetchedVertex2.setId("vertex-3");
		unfetchedVertex2.setLabel("Unfetched Label");
		unfetchedVertex2.setDescription("Unfetched Description");
		unfetchedVertex2.setUrl("https://example.com");
		// Position remains at origin (0,0,0)

		graphset.addVertex(fetchedVertex);
		graphset.addVertex(unfetchedVertex1);
		graphset.addVertex(unfetchedVertex2);

		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertEquals(2, unfetchedVertices.size());
		assertTrue(unfetchedVertices.contains(unfetchedVertex1));
		assertTrue(unfetchedVertices.contains(unfetchedVertex2));
		assertFalse(unfetchedVertices.contains(fetchedVertex));
	}

	@Test
	@DisplayName("Should limit results to maximum 50 vertices")
	void shouldLimitResultsToMaximum50Vertices() {
		// Add 60 unfetched vertices
		for (int i = 0; i < 60; i++) {
			Vertex unfetchedVertex = new Vertex();
			unfetchedVertex.setId("vertex-" + i);
			unfetchedVertex.setLabel("Label " + i);
			unfetchedVertex.setDescription("Description " + i);
			unfetchedVertex.setUrl("https://example.com/" + i);
			// Position remains at origin (0,0,0) - making it unfetched
			graphset.addVertex(unfetchedVertex);
		}

		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertEquals(50, unfetchedVertices.size());
	}

	@Test
	@DisplayName("Should return all unfetched vertices when count is less than 50")
	void shouldReturnAllUnfetchedVerticesWhenCountIsLessThan50() {
		// Add 25 unfetched vertices
		for (int i = 0; i < 25; i++) {
			Vertex unfetchedVertex = new Vertex();
			unfetchedVertex.setId("vertex-" + i);
			unfetchedVertex.setLabel("Label " + i);
			unfetchedVertex.setDescription("Description " + i);
			unfetchedVertex.setUrl("https://example.com/" + i);
			// Position remains at origin (0,0,0) - making it unfetched
			graphset.addVertex(unfetchedVertex);
		}

		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertEquals(25, unfetchedVertices.size());
	}

	@Test
	@DisplayName("Should handle mixed fetched and unfetched vertices correctly")
	void shouldHandleMixedFetchedAndUnfetchedVerticesCorrectly() {
		// Add 30 unfetched vertices
		for (int i = 0; i < 30; i++) {
			Vertex unfetchedVertex = new Vertex();
			unfetchedVertex.setId("unfetched-" + i);
			unfetchedVertex.setLabel("Unfetched Label " + i);
			unfetchedVertex.setDescription("Unfetched Description " + i);
			unfetchedVertex.setUrl("https://example.com/unfetched/" + i);
			// Position remains at origin (0,0,0) - making it unfetched
			graphset.addVertex(unfetchedVertex);
		}

		// Add 20 fetched vertices
		for (int i = 0; i < 20; i++) {
			Vertex fetchedVertex = new Vertex();
			fetchedVertex.setId("fetched-" + i);
			fetchedVertex.setLabel("Fetched Label " + i);
			fetchedVertex.setDescription("Fetched Description " + i);
			fetchedVertex.setUrl("https://example.com/fetched/" + i);
			fetchedVertex.setPosition(new Point3D(i + 1.0, i + 2.0, i + 3.0));
			graphset.addVertex(fetchedVertex);
		}

		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertEquals(30, unfetchedVertices.size());

		// Verify all returned vertices are unfetched
		for (Vertex vertex : unfetchedVertices) {
			assertFalse(vertex.fetched());
		}
	}

	@Test
	@DisplayName("Should return all unfetched vertices regardless of order")
	void shouldReturnAllUnfetchedVerticesRegardlessOfOrder() {
		// Create vertices with specific IDs
		Vertex vertex1 = new Vertex();
		vertex1.setId("vertex-1");
		vertex1.setLabel("Label 1");
		vertex1.setDescription("Description 1");
		vertex1.setUrl("https://example.com/1");
		// Position remains at origin (0,0,0) - making it unfetched

		Vertex vertex2 = new Vertex();
		vertex2.setId("vertex-2");
		vertex2.setLabel("Label 2");
		vertex2.setDescription("Description 2");
		vertex2.setUrl("https://example.com/2");
		// Position remains at origin (0,0,0) - making it unfetched

		graphset.addVertex(vertex1);
		graphset.addVertex(vertex2);

		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertEquals(2, unfetchedVertices.size());
		assertTrue(unfetchedVertices.contains(vertex1));
		assertTrue(unfetchedVertices.contains(vertex2));
	}

	@Test
	@DisplayName("Should handle edge cases for vertex fetching status")
	void shouldHandleEdgeCasesForVertexFetchingStatus() {
		// Test vertex with null description
		Vertex vertex1 = new Vertex();
		vertex1.setId("vertex-1");
		vertex1.setLabel("Label 1");
		vertex1.setUrl("https://example.com/1");
		vertex1.setPosition(new Point3D(1.0, 2.0, 3.0));
		// Description is null - should be unfetched

		// Test vertex with null URL
		Vertex vertex2 = new Vertex();
		vertex2.setId("vertex-2");
		vertex2.setLabel("Label 2");
		vertex2.setDescription("Description 2");
		vertex2.setPosition(new Point3D(1.0, 2.0, 3.0));
		// URL is null - should be unfetched

		// Test vertex with position at origin
		Vertex vertex3 = new Vertex();
		vertex3.setId("vertex-3");
		vertex3.setLabel("Label 3");
		vertex3.setDescription("Description 3");
		vertex3.setUrl("https://example.com/3");
		// Position remains at origin (0,0,0) - should be unfetched

		graphset.addVertex(vertex1);
		graphset.addVertex(vertex2);
		graphset.addVertex(vertex3);

		List<Vertex> unfetchedVertices = graphset.getUnfetchedVertices();
		assertNotNull(unfetchedVertices);
		assertEquals(3, unfetchedVertices.size());
		assertTrue(unfetchedVertices.contains(vertex1));
		assertTrue(unfetchedVertices.contains(vertex2));
		assertTrue(unfetchedVertices.contains(vertex3));
	}
}
