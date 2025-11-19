package edu.velvet.Wikiverse.api.services.wikidata;

import edu.velvet.Wikiverse.api.models.WikiverseError;
import edu.velvet.Wikiverse.api.services.logging.ProcessLogger;
import io.vavr.CheckedFunction0;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;

/**
 * Service responsible for fetching search results from Wikidata API.
 * This broker acts as an intermediary between the application and the Wikidata
 * API,
 * providing error handling, logging, and result validation for entity searches.
 *
 * <p>
 * This class provides methods to:
 * <ul>
 * <li>Search for entities in Wikidata by query string</li>
 * <li>Handle API offline scenarios with proper error reporting</li>
 * <li>Validate search results and handle empty result sets</li>
 * <li>Log all fetch operations for debugging and monitoring</li>
 * </ul>
 *
 * <p>
 * The service uses Vavr's Either monad for functional error handling,
 * ensuring that API failures are properly captured and returned as
 * WikiverseError
 * instances rather than throwing exceptions.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see WikibaseDataFetcher
 * @see ProcessLogger
 * @see WikiverseError
 */
@Service
public class FetchBroker {

	/** The source identifier used for logging operations. */
	private final String SOURCE = "FetchBroker()";

	/** The Wikidata data fetcher used to interact with the Wikidata API. */
	private final WikibaseDataFetcher fetcher;

	/** The logger instance for recording fetch operations and errors. */
	private final ProcessLogger logger = new ProcessLogger("wikidata-fetch-broker.log");

	/**
	 * Default constructor that initializes the FetchBroker with the standard
	 * Wikidata data fetcher.
	 * This constructor is used by Spring's dependency injection framework.
	 */
	@Autowired
	public FetchBroker() {
		this(new WikibaseDataFetcher(BasicApiConnection.getWikidataApiConnection(), Datamodel.SITE_WIKIDATA));
	}

	/**
	 * Injectable constructor to allow testing with mocked resources.
	 * This constructor enables dependency injection of a custom WikibaseDataFetcher
	 * for unit testing scenarios.
	 *
	 * @param fetcher the WikibaseDataFetcher instance to use for API calls
	 */
	public FetchBroker(WikibaseDataFetcher fetcher) {
		this.fetcher = fetcher; // provides an injectable constructor for non-IT testing
	}

	/**
	 * Fetches search results from Wikidata for the given query string.
	 *
	 * @param query           the search query string to look up entities for
	 * @param wikiLanguageKey the language key to narrow the search to (default
	 *                        "en")
	 * @return Either a WikiverseError on failure, or a List of
	 *         WbSearchEntitiesResult on success
	 */
	public Either<WikiverseError, List<WbSearchEntitiesResult>> fetchSearchResultsByAny(
		String query,
		String wikiLanguageKey
	) {
		return logger.log(SOURCE + ".fetchSearchResultsByAny(" + query + ")", () ->
			useWikidataAPIOfflineHandler(() -> fetcher.searchEntities(query, wikiLanguageKey)).flatMap(res ->
				this.useNoSearchResultsHandler(res, query)
			)
		);
	}

	/**
	 * Fetches a specific entity document from Wikidata by its unique identifier.
	 * This method retrieves the complete entity data for a given Wikidata entity
	 * ID,
	 * including all properties, labels, descriptions, and claims associated with
	 * the entity.
	 *
	 * <p>
	 * The method applies language filtering to restrict the returned data to the
	 * specified target language. This filtering affects labels, descriptions, and
	 * aliases but does not apply to monolingual text values within statements.
	 *
	 * <p>
	 * The method handles various error scenarios:
	 * <ul>
	 * <li>API offline or network connectivity issues</li>
	 * <li>Entity not found or invalid entity ID</li>
	 * <li>Malformed or inaccessible entity data</li>
	 * </ul>
	 *
	 * <p>
	 * All operations are logged for debugging and monitoring purposes.
	 * The method uses functional error handling with Vavr's Either monad to ensure
	 * that failures are properly captured and returned as WikiverseError instances.
	 *
	 * @param entityID       the unique Wikidata entity identifier (e.g., "Q42" for
	 *                       Douglas Adams)
	 * @param wikiLangTarget the target Wikipedia language code for filtering
	 *                       entity data (e.g., "en", "es")
	 * @return Either a WikiverseError on failure, or the complete EntityDocument on
	 *         success
	 * @see EntityDocument
	 * @see WikiverseError.WikidataServiceError.NoMatchingResultsFound
	 * @see WikiverseError.WikidataServiceError.APIOffline
	 */
	public Either<WikiverseError, EntityDocument> fetchEntityByID(String entityID, String wikiLangTarget) {
		fetcher.getFilter().setLanguageFilter(Collections.singleton(wikiLangTarget));
		return logger.log(SOURCE + ".fetchEntityByID(" + entityID + ") - with language filter:" + wikiLangTarget, () ->
			useWikidataAPIOfflineHandler(() -> fetcher.getEntityDocument(entityID)).flatMap(res ->
				this.useNoSuchEntityHandler(res, entityID)
			)
		);
	}

	public Either<WikiverseError, EntityDocument> fetchEntityByID(String entityID) {
		return logger.log(SOURCE + ".fetchEntityByID(" + entityID + ")", () ->
			useWikidataAPIOfflineHandler(() -> fetcher.getEntityDocument(entityID)).flatMap(res ->
				this.useNoSuchEntityHandler(res, entityID)
			)
		);
	}

	/**
	 * Fetches multiple entity documents from Wikidata by their unique identifiers.
	 * This method retrieves complete entity data for a list of Wikidata entity IDs,
	 * returning a map where each entity ID is associated with either a successful
	 * EntityDocument or a WikiverseError for that specific entity.
	 *
	 * <p>
	 * The method applies language filtering to restrict the returned data to the
	 * specified target language. This filtering affects labels, descriptions, and
	 * aliases but does not apply to monolingual text values within statements.
	 *
	 * <p>
	 * The method handles various error scenarios for individual entities:
	 * <ul>
	 * <li>API offline or network connectivity issues (affects all entities)</li>
	 * <li>Individual entities not found or invalid entity IDs</li>
	 * <li>Malformed or inaccessible entity data for specific entities</li>
	 * </ul>
	 *
	 * <p>
	 * Unlike single entity fetching, this method provides granular error handling
	 * where some entities may succeed while others fail. Each entity ID in the
	 * result
	 * map will have its own Either result indicating success or failure for that
	 * specific entity.
	 *
	 * <p>
	 * All operations are logged for debugging and monitoring purposes.
	 * The method uses functional error handling with Vavr's Either monad to ensure
	 * that failures are properly captured and returned as WikiverseError instances.
	 *
	 * @param entityIDs      the list of unique Wikidata entity identifiers to fetch
	 * @param wikiLangTarget the target Wikipedia language code for filtering
	 *                       entity data (e.g., "en", "es")
	 * @return Either a WikiverseError on complete failure, or a Map where each
	 *         entity ID
	 *         maps to Either a WikiverseError or EntityDocument for that specific
	 *         entity
	 * @see EntityDocument
	 * @see WikiverseError.WikidataServiceError.NoMatchingResultsFound
	 * @see WikiverseError.WikidataServiceError.APIOffline
	 */
	public Either<WikiverseError, Map<String, Either<WikiverseError, EntityDocument>>> fetchEntitiesByIDs(
		List<String> entityIDs,
		String wikiLangTarget
	) {
		fetcher.getFilter().setLanguageFilter(Collections.singleton(wikiLangTarget));
		return logger.log(SOURCE + ".fetchEntitiesByIDs(" + entityIDs.size() + ")", () ->
			useWikidataAPIOfflineHandler(() -> fetcher.getEntityDocuments(entityIDs)).map(results ->
				this.useEntitiesResultsMapValidator(results)
			)
		);
	}

	// !PRIVATE ============================================================>
	// !PRIVATE ============================================================>

	/**
	 * Handles the case when no entity document is found for the given entity ID.
	 * This method validates the entity document result and returns an appropriate
	 * error if the entity was not found, otherwise returns the successful result.
	 *
	 * <p>
	 * The method checks if the entity document is null, which indicates that
	 * the requested entity ID does not exist in Wikidata or is inaccessible.
	 * In such cases, it returns a NoMatchingResultsFound error with the entity ID
	 * for proper error reporting and debugging.
	 *
	 * @param res      the entity document result from the Wikidata API, can be null
	 * @param entityID the original entity ID that was requested
	 * @return Either a NoMatchingResultsFound error if entity is null,
	 *         or the successful entity document
	 */
	private Either<WikiverseError, EntityDocument> useNoSuchEntityHandler(EntityDocument results, String entityID) {
		return results == null
			? Either.left(new WikiverseError.WikidataServiceError.NoMatchingResultsFound(entityID))
			: Either.right(results);
	}

	/**
	 * Validates and transforms a map of entity documents from the Wikidata API
	 * response.
	 * This method processes the raw results from a batch entity fetch operation and
	 * converts each entity document result into a properly validated Either monad
	 * result.
	 *
	 * <p>
	 * For each entity in the results map, this method checks if the entity document
	 * is null (indicating the entity was not found or is inaccessible) and wraps it
	 * in an appropriate Either result. Successful entities are wrapped in
	 * Either.right(),
	 * while missing entities are converted to NoMatchingResultsFound errors wrapped
	 * in Either.left().
	 *
	 * <p>
	 * This transformation enables granular error handling where individual entity
	 * failures don't affect the processing of other entities in the batch
	 * operation.
	 * Each entity ID in the returned map will have its own success or failure
	 * state.
	 *
	 * @param results the raw map of entity IDs to EntityDocument objects from the
	 *                Wikidata API,
	 *                where null values indicate entities that were not found
	 * @return a map where each entity ID maps to Either a WikiverseError for
	 *         missing entities
	 *         or a successful EntityDocument for found entities
	 * @see #useNoSuchEntityHandler(EntityDocument, String)
	 * @see WikiverseError.WikidataServiceError.NoMatchingResultsFound
	 */
	private Map<String, Either<WikiverseError, EntityDocument>> useEntitiesResultsMapValidator(
		Map<String, EntityDocument> results
	) {
		return results
			.entrySet()
			.stream()
			.collect(
				Collectors.toMap(Map.Entry::getKey, entry -> useNoSuchEntityHandler(entry.getValue(), entry.getKey()))
			);
	}

	/**
	 * Handles the case when no search results are found for the given query.
	 * Returns an appropriate error if the results list is empty, otherwise
	 * returns the successful results.
	 *
	 * @param results the list of search results from Wikidata
	 * @param query   the original search query that produced these results
	 * @return Either a NoMatchingResultsFound error if results are empty,
	 *         or the successful results list
	 */
	private Either<WikiverseError, List<WbSearchEntitiesResult>> useNoSearchResultsHandler(
		List<WbSearchEntitiesResult> results,
		String query
	) {
		return results.isEmpty()
			? Either.left(new WikiverseError.WikidataServiceError.NoMatchingResultsFound(query))
			: Either.right(results);
	}

	/**
	 * Handles potential API offline scenarios by wrapping the fetch operation
	 * in a Try monad and converting any exceptions to appropriate error types.
	 * This method provides a functional approach to error handling for Wikidata API
	 * calls.
	 *
	 * <p>
	 * The method handles the following error scenarios:
	 * <ul>
	 * <li>Exceptions thrown during the API call</li>
	 * <li>Null return values from the API, which are treated as API offline
	 * scenarios</li>
	 * </ul>
	 *
	 * @param <T>           the type of data being fetched
	 * @param fetchSupplier the supplier function that performs the actual API call
	 * @return Either an APIOffline error if the operation fails or returns null,
	 *         or the successful result
	 */
	private <T> Either<WikiverseError, T> useWikidataAPIOfflineHandler(CheckedFunction0<T> fetchSupplier) {
		String methodSource = SOURCE + ".useWikidataAPIOfflineHandler()";
		return Try.of(fetchSupplier)
			.toEither()
			.fold(
				error -> {
					return Either.left(
						new WikiverseError.WikidataServiceError.APIOffline(
							error.getMessage(),
							methodSource,
							error.getStackTrace()
						)
					);
				},
				result -> {
					if (result == null) {
						return Either.left(
							new WikiverseError.WikidataServiceError.APIOffline(
								"The Wikidata API Appears to be Offline and returned a null",
								methodSource,
								Thread.currentThread().getStackTrace()
							)
						);
					} else {
						return Either.right(result);
					}
				}
			);
	}
}
