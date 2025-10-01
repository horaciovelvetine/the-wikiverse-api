package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

/**
 * Represents a search result from Wikidata containing entity information.
 * This class encapsulates the essential data returned from a Wikidata search
 * operation,
 * including entity identification, metadata, and associated URLs.
 *
 * <p>
 * This class provides direct access to search result properties such as:
 * <ul>
 * <li>Entity identification (ID and page ID)</li>
 * <li>Display information (title, label, description)</li>
 * <li>Associated URLs (entity URL and concept URI)</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see WbSearchEntitiesResult
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchResult {

	/** The unique entity identifier from Wikidata (e.g., "Q123"). */
	public String entityID;

	/** The Wikipedia page ID associated with this entity. */
	public long pageID;

	/** The title of the Wikipedia article. */
	public String title;

	/** The human-readable label for this entity. */
	public String label;

	/** The description text for this entity. */
	public String description;

	/** The URL to the Wikipedia article. */
	public String url;

	/**
	 * Constructs a new SearchResult from a Wikidata search entities result.
	 *
	 * @param wikiResult the Wikidata search entities result to convert
	 */
	public SearchResult(WbSearchEntitiesResult wikiResult) {
		this.entityID = wikiResult.getEntityId();
		this.pageID = wikiResult.getPageId();
		this.title = wikiResult.getTitle();
		this.label = wikiResult.getLabel();
		this.description = wikiResult.getDescription();
		this.url = wikiResult.getConceptUri();
	}

	/**
	 * Gets the unique entity identifier from Wikidata.
	 *
	 * @return the entity ID (e.g., "Q123"), or null if not set
	 */
	public String getEntityID() {
		return entityID;
	}

	/**
	 * Gets the Wikipedia page ID associated with this entity.
	 *
	 * @return the page ID, or 0 if not set
	 */
	public long getPageID() {
		return pageID;
	}

	/**
	 * Gets the title of the Wikipedia article.
	 *
	 * @return the article title, or null if not set
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the human-readable label for this entity.
	 *
	 * @return the entity label, or null if not set
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Gets the description text for this entity.
	 *
	 * @return the entity description, or null if not set
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the URL to the Wikipedia article.
	 *
	 * @return the article URL, or null if not set
	 */
	public String getUrl() {
		return url;
	}
}
