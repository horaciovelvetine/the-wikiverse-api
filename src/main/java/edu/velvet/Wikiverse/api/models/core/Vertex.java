package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.SiteLink;

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
	private final String id;

	/** The display label for this vertex. Cannot be null or empty. */
	private final String label;

	/** The description text for this vertex. Can be null. */
	private final String description;

	/** The URL associated with this vertex. Cannot be null or empty. */
	private final String url;

	/** The 3D position coordinates of this vertex in the graph. */
	private final Point3D position = new Point3D();

	/** Flag indicating whether this vertex is locked from modifications. */
	private boolean locked = false;

	private final List<Claim> claims = new ArrayList<>();

	/**
	 * Constructs a Vertex instance from a Wikidata ItemDocument and a target wiki
	 * language.
	 * <p>
	 * This constructor extracts the entity's identifier, label, description, and
	 * Wikipedia
	 * article URL from the provided ItemDocument in the specified target language.
	 * The
	 * identifier and label are used as display metadata, while the description
	 * provides
	 * additional context. The URL is built using {@link WikipediaSitelinkBuilder}
	 * and is
	 * intended to link to the corresponding Wikipedia article in the target
	 * language.
	 * The position is initialized to a default 3D coordinate, the vertex is
	 * initially
	 * unlocked, and the claims list is empty.
	 * </p>
	 *
	 * @param doc            the Wikidata ItemDocument representing the entity; must
	 *                       not be null
	 * @param wikiLangTarget the language code in which to fetch label, description,
	 *                       and URL; must not be null
	 * @throws IllegalArgumentException if the ItemDocument has no SiteLinks
	 *                                  available
	 */
	public Vertex(ItemDocument doc, String wikiLangTarget) {
		this.id = doc.getEntityId().getId();
		this.label = doc.findLabel(wikiLangTarget);
		this.description = doc.findDescription(wikiLangTarget);
		this.url = buildWikipediaURL(doc, wikiLangTarget);
	}

	public static Vertex createOrNull(ItemDocument doc, String wikiLangTarget) {
		try {
			return new Vertex(doc, wikiLangTarget);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Constructs a Vertex object from JSON deserialization.
	 * This constructor is used by Jackson to deserialize Vertex from JSON.
	 *
	 * @param id          the unique identifier for this vertex
	 * @param label       the display label for this vertex
	 * @param description the description text for this vertex
	 * @param url         the URL associated with this vertex
	 * @param position    the 3D position coordinates of this vertex
	 * @param locked      flag indicating whether this vertex is locked from
	 *                    modifications
	 * @param claims      the list of claims associated with this vertex
	 */
	@JsonCreator
	public Vertex(
		@JsonProperty("id") String id,
		@JsonProperty("label") String label,
		@JsonProperty("description") String description,
		@JsonProperty("url") String url,
		@JsonProperty("position") Point3D position,
		@JsonProperty("locked") boolean locked,
		@JsonProperty("claims") List<Claim> claims
	) {
		this.id = id;
		this.label = label;
		this.description = description;
		this.url = url;
		if (position != null) {
			this.position.setLocation(position);
		}
		this.locked = locked;
		if (claims != null) {
			this.claims.addAll(claims);
		}
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
	 * Gets the display label of this vertex.
	 *
	 * @return the vertex label, or null if not set
	 */
	public String getLabel() {
		return label;
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
	 * Gets the description of this vertex.
	 *
	 * @return the vertex description, or null if not set
	 */
	public String getDescription() {
		return description;
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
	 * Returns the list of claims associated with this vertex.
	 *
	 * @return a list of {@link Claim} objects; never null but may be empty
	 */
	public List<Claim> getClaims() {
		return this.claims;
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
	public boolean isFetched() {
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
		boolean isAtOrigin =
			Math.abs(position.getX()) < EPSILON &&
			Math.abs(position.getY()) < EPSILON &&
			Math.abs(position.getZ()) < EPSILON;

		return !isAtOrigin;
	}

	/**
	 * Builds a Wikipedia URL for the given ItemDocument in the specified language.
	 * <p>
	 * This method retrieves the SiteLink for the target language and constructs
	 * a properly formatted Wikipedia URL. If the SiteLink for the target language
	 * is not available, it checks whether any SiteLinks exist at all.
	 * </p>
	 *
	 * @param doc      the Wikidata ItemDocument; must not be null
	 * @param language the language code for the Wikipedia URL; must not be null
	 * @return the constructed Wikipedia URL
	 * @throws IllegalArgumentException if no SiteLinks are available for this
	 *                                  entity
	 */
	private String buildWikipediaURL(ItemDocument doc, String language) {
		String PROTOCOL_PREFIX = "https://";
		String DOMAIN_SUFFIX = ".wikipedia.org/wiki/";

		SiteLink link = doc.getSiteLinks().get(language + "wiki");

		// Check if link is null and if there are any SiteLinks at all
		if (link == null) {
			// If there are SiteLinks but not for this language, still throw an error
			throw new IllegalArgumentException(
				"Cannot create Vertex: Entity " +
				doc.getEntityId().getId() +
				" has no SiteLink for language: " +
				language
			);
		}

		String pageTitle = link.getPageTitle();
		// Replace Spaces w/ Underscores and replace percent encoded chars...
		pageTitle = pageTitle.replace(" ", "_").replace("%3A", ":").replace("%2F", "/");
		String encodeTitle;
		try {
			encodeTitle = URLEncoder.encode(pageTitle, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// Most any runtime should be fine with UTF-8 encoding...
			throw new RuntimeException("UTF-8 Encoding not supported in the buildWikipediaURL()");
		}
		// Return a lang target specific wikipedia string
		return PROTOCOL_PREFIX + language + DOMAIN_SUFFIX + encodeTitle;
	}
}
