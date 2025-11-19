package edu.velvet.Wikiverse.api.models.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import edu.velvet.Wikiverse.api.models.core.SearchResult;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataService;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a search request for querying Wikidata entities with specific
 * language targeting.
 * This class encapsulates the necessary parameters and functionality for
 * performing
 * search operations against the Wikidata API and managing the resulting search
 * results.
 *
 * <p>
 * This class provides methods to manage search request properties such as:
 * <ul>
 * <li>Search query string and language targeting</li>
 * <li>Search result collection and management</li>
 * <li>Asynchronous search execution with error handling</li>
 * <li>Integration with WikidataService for API operations</li>
 * </ul>
 *
 * <p>
 * The class extends the base Request class to inherit common request
 * functionality
 * and uses Jackson annotations for JSON serialization/deserialization with
 * field
 * visibility set to ANY for automatic property mapping.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Request
 * @see SearchResult
 * @see WikidataService
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchRequest extends Request {

	/** The search query string used for searching Wikidata entities. */
	private final String query;

	/** The target Wikipedia language code for search results (e.g., "en", "es"). */
	private final String wikiLangTarget;

	/** The list of search results returned from the Wikidata search operation. */
	private List<SearchResult> searchResults = new ArrayList<>();

	/**
	 * Constructs a new {@code SearchRequest} and immediately executes the search
	 * operation.
	 * <p>
	 * This constructor initializes the request with the provided search query and
	 * language target,
	 * then uses the supplied {@link WikidataService} instance to perform an API
	 * search for matching entities.
	 * The resulting search results are automatically fetched and stored in this
	 * request instance.
	 *
	 * @param query          the search query string to use for Wikidata API search;
	 *                       cannot be null or empty
	 * @param wikiLangTarget the Wikipedia language code to target (e.g., "en",
	 *                       "es"); cannot be null or empty
	 * @param wikidata       the {@link WikidataService} instance used to perform
	 *                       the search; must not be null
	 */
	public SearchRequest(String query, String wikiLangTarget, WikidataService wikidata) {
		this.query = query;
		this.wikiLangTarget = wikiLangTarget;
		// Fetch and Ingest the Search Results from the Wikidata API
		fetchSearchResults(wikidata);
	}

	/**
	 * Gets the search query string used for this request.
	 *
	 * @return the search query, or null if not set
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Gets the target Wikipedia language for the search results.
	 *
	 * @return the Wikipedia language code (e.g., "en", "es"), or null if not set
	 */
	public String getWikiLangTarget() {
		return wikiLangTarget;
	}

	/**
	 * Gets the list of search results from the Wikidata search operation.
	 *
	 * @return the list of SearchResult objects, never null but may be empty
	 */
	public List<SearchResult> getSearchResults() {
		return searchResults;
	}

	/**
	 * Fetches search results from Wikidata using the stored query and language
	 * target.
	 *
	 * <p>
	 * This method performs an asynchronous search operation using the
	 * WikidataService
	 * and populates the searchResults list with the processed results. If the
	 * search
	 * operation fails, the error is stored in the request's error field.
	 *
	 * <p>
	 * The method uses a functional approach with fold to handle both success and
	 * error cases, ensuring proper error handling and result processing.
	 *
	 * @param wikidata the WikidataService instance to use for fetching results
	 * @return this SearchRequest instance for method chaining
	 * @throws IllegalArgumentException if wikidata is null
	 * @see WikidataService#api()
	 * @see edu.velvet.Wikiverse.api.services.wikidata.DocumentProcessor#ingestWikidataSearchResults(List)
	 */
	private void fetchSearchResults(WikidataService wikidata) {
		wikidata
			.api()
			.fetchSearchResultsByAny(query, wikiLangTarget)
			.fold(
				error -> {
					this.setError(error);
					return null;
				},
				results -> {
					this.searchResults = wikidata.docProc().ingestWikidataSearchResults(results);
					return null;
				}
			);
	}
}
