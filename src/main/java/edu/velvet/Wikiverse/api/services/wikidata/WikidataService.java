package edu.velvet.Wikiverse.api.services.wikidata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for coordinating Wikidata operations and providing
 * a high-level interface for entity retrieval and search functionality.
 * This service acts as a facade over the FetchBroker, providing a clean
 * API for the rest of the application to interact with Wikidata data.
 *
 * <p>
 * This service provides methods to:
 * <ul>
 * <li>Search for entities in Wikidata by query string</li>
 * <li>Retrieve specific entity documents by ID</li>
 * <li>Fetch multiple entities in batch operations</li>
 * </ul>
 *
 * <p>
 * The service uses the FetchBroker for all actual Wikidata API interactions,
 * ensuring proper error handling and logging throughout the data retrieval
 * process.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see FetchBroker
 */
@Service
public class WikidataService {

	/** The fetch broker used for all Wikidata API operations. */
	@Autowired
	private FetchBroker fetchBroker;

	@Autowired
	/** The Document Processor for Wikidata Documents */
	private DocumentProcessor docProc;

	/** The Default list of Filtered entities */
	private final DefaultWikidataFilters defaultFilters = new DefaultWikidataFilters();

	/**
	 * Provides access to the FetchBroker for direct Wikidata API operations.
	 * This method exposes the underlying FetchBroker instance to allow callers
	 * to perform low-level Wikidata operations when the high-level service
	 * methods are insufficient.
	 *
	 * <p>
	 * The returned FetchBroker provides direct access to entity fetching
	 * capabilities including single and batch entity retrieval operations
	 * with proper error handling and logging.
	 *
	 * @return the FetchBroker instance used by this service
	 * @see FetchBroker
	 */
	public FetchBroker api() {
		return this.fetchBroker;
	}

	/**
	 * Provides access to the DocumentProcessor for Wikidata document processing.
	 * This method exposes the underlying DocumentProcessor instance to allow
	 * callers to perform document transformation and processing operations
	 * on retrieved Wikidata entity documents.
	 *
	 * <p>
	 * The returned DocumentProcessor provides capabilities for transforming
	 * raw Wikidata entity documents into application-specific formats and
	 * extracting relevant information from entity data.
	 *
	 * @return the DocumentProcessor instance used by this service
	 * @see DocumentProcessor
	 */
	public DocumentProcessor docProc() {
		return this.docProc;
	}

	public DefaultWikidataFilters getDefaultFilter() {
		return this.defaultFilters;
	}
}
