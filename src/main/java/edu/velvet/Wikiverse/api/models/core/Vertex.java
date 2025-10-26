package edu.velvet.Wikiverse.api.models.core;

import java.util.ArrayList;
import java.util.List;

import org.wikidata.wdtk.datamodel.implementation.ItemDocumentImpl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Represents a vertex (node) in the Wikiverse graph structure.
 * A vertex represents a Wikipedia article or concept with its associated
 * metadata,
 * including position in 3D space and locking state.
 *
 * <p>
 * This class provides methods to manage vertex properties such as:
 * <ul>
 * <li>Unique identifier and label</li>
 * <li>Description and URL</li>
 * <li>3D position coordinates</li>
 * <li>Locking mechanism for preventing modifications</li>
 * <li>Data fetching status verification</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Point3D
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Vertex {

	/** The unique identifier for this vertex. Cannot be null or empty. */
	private String id;

	/** The display label for this vertex. Cannot be null or empty. */
	private String label;

	/** The description text for this vertex. Can be null. */
	private String description;

	/** The URL associated with this vertex. Cannot be null or empty. */
	private String url;

	/** The 3D position coordinates of this vertex in the graph. */
	private Point3D position = new Point3D();

	/** Flag indicating whether this vertex is locked from modifications. */
	private boolean locked = false;

	private final List<Claim> claims = new ArrayList<>();

	public Vertex() {
	}

	public Vertex(ItemDocumentImpl doc, String wikiLangTarget) {
		this.id = doc.getEntityId().getId();
		this.label = doc.findLabel(wikiLangTarget);
		this.description = doc.findDescription(wikiLangTarget);
	}

	/**
	 * Gets the unique identifier of this vertex.
	 *
	 * @return the vertex ID, or null if not set
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for this vertex.
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
	 * Gets the display label of this vertex.
	 *
	 * @return the vertex label, or null if not set
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the display label for this vertex.
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
	 * Gets the URL associated with this vertex.
	 *
	 * @return the vertex URL, or null if not set
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the URL for this vertex.
	 *
	 * @param url the URL, cannot be null, empty, or whitespace-only
	 * @throws IllegalArgumentException if url is null, empty, or whitespace-only
	 */
	public void setUrl(String url) {
		if (url == null || url.trim().isEmpty()) {
			throw new IllegalArgumentException("URL cannot be null or empty");
		}
		this.url = url;
	}

	/**
	 * Gets the description of this vertex.
	 *
	 * @return the vertex description, or null if not set
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for this vertex.
	 *
	 * @param description the description text, can be null
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the 3D position of this vertex.
	 *
	 * @return the position coordinates as a Point3D object
	 */
	public Point3D getPosition() {
		return position;
	}

	/**
	 * Sets the 3D position for this vertex.
	 *
	 * @param position the position coordinates, cannot be null
	 * @throws IllegalArgumentException if position is null
	 */
	public void setPosition(Point3D position) {
		if (position == null) {
			throw new IllegalArgumentException("Position cannot be null");
		}
		this.position = position;
	}

	/**
	 * Returns the list of claims associated with this vertex.
	 *
	 * @return a list of {@link Claim} objects; never null but may be empty
	 */
	public List<Claim> getClaims() {
		return this.claims;
	}

	/**
	 * Adds a claim to this vertex.
	 *
	 * @param claim the {@link Claim} to add; cannot be null
	 * @throws NullPointerException if claim is null
	 */
	public void addClaim(Claim claim) {
		this.claims.add(claim);
	}

	/**
	 * Checks if this vertex is locked from modifications.
	 *
	 * @return true if the vertex is locked, false otherwise
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Locks this vertex to prevent modifications.
	 * Once locked, the vertex cannot be modified until unlocked.
	 */
	public void lock() {
		this.locked = true;
	}

	/**
	 * Unlocks this vertex to allow modifications.
	 * After unlocking, the vertex can be modified again.
	 */
	public void unlock() {
		this.locked = false;
	}

	/**
	 * Checks if this vertex has been fully fetched with all required data.
	 * A vertex is considered fetched when it has:
	 * <ul>
	 * <li>A label (not null, empty string is acceptable)</li>
	 * <li>A description (not null, empty string is acceptable)</li>
	 * <li>A URL (not null, empty string is acceptable)</li>
	 * <li>A position that is not at the default origin (0,0,0)</li>
	 * </ul>
	 *
	 * @return true if all required fields have been provided, false otherwise
	 */
	public boolean fetched() {
		// Check if label is provided (not null, empty string is acceptable)
		if (label == null) {
			return false;
		}

		// Check if description is provided (not null, empty string is acceptable)
		if (description == null) {
			return false;
		}

		// Check if URL is provided (not null, empty string is acceptable)
		if (url == null) {
			return false;
		}

		// Check if position is not at the default origin (0,0,0)
		if (position == null) {
			return false;
		}

		// Check if position is not at origin (0,0,0)
		// Using a small epsilon for floating-point comparison
		final double EPSILON = 1e-9;
		boolean isAtOrigin = Math.abs(position.getX()) < EPSILON &&
				Math.abs(position.getY()) < EPSILON &&
				Math.abs(position.getZ()) < EPSILON;

		return !isAtOrigin;
	}
}
