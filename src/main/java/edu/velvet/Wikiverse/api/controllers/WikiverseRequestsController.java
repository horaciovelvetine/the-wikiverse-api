package edu.velvet.Wikiverse.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.velvet.Wikiverse.api.models.requests.GraphsetRequest;
import edu.velvet.Wikiverse.api.models.requests.Request;
import edu.velvet.Wikiverse.api.models.requests.SearchRequest;
import edu.velvet.Wikiverse.api.models.requests.StatusRequest;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataService;

@CrossOrigin
@RestController
public class WikiverseRequestsController {

	@Autowired
	private WikidataService wikidata;

	/**
	 * Retrieves the current status of the Wikiverse service and its dependencies.
	 * This endpoint provides health check information for the application and
	 * external services, particularly the Wikidata service integration.
	 *
	 * <p>
	 * The status check performs validation of:
	 * <ul>
	 * <li>Application service health and availability</li>
	 * <li>Wikidata service connectivity and responsiveness</li>
	 * <li>Overall system operational status</li>
	 * </ul>
	 *
	 * <p>
	 * This endpoint is useful for monitoring, health checks, and debugging
	 * connectivity issues. The response includes timing information and any
	 * errors encountered during the status verification process.
	 *
	 * @return ResponseEntity containing the StatusRequest with service health
	 *         information and appropriate HTTP status code
	 * @see StatusRequest
	 * @see WikidataService
	 * @see #buildRequestResponse(Request)
	 * @author The Wikiverse Team
	 * @version 1.0
	 * @since 1.0
	 */
	@GetMapping("/api/status")
	public ResponseEntity<Request> getWikiverseServiceStatus() {
		return this.buildRequestResponse(new StatusRequest().checkServiceStatus(wikidata));
	}

	/**
	 * Retrieves search results from Wikidata based on the provided query string.
	 * This endpoint performs a search operation against the Wikidata service and
	 * returns relevant entity matches for the specified query.
	 *
	 * <p>
	 * The search operation processes:
	 * <ul>
	 * <li>Entity name matching and fuzzy search capabilities</li>
	 * <li>Wikidata entity retrieval and result formatting</li>
	 * <li>Error handling for failed search operations</li>
	 * </ul>
	 *
	 * <p>
	 * This endpoint provides search functionality for discovering Wikidata entities
	 * that match user queries. The response includes search results and any errors
	 * encountered during the search process.
	 *
	 * @param query the search query string to match against Wikidata entities
	 * @return ResponseEntity containing the SearchRequest with search results
	 *         and appropriate HTTP status code
	 * @see SearchRequest
	 * @see WikidataService
	 * @see #buildRequestResponse(Request)
	 * @author The Wikiverse Team
	 * @version 1.0
	 * @since 1.0
	 */
	@GetMapping("/api/search-results")
	public ResponseEntity<Request> getSearchResults(@RequestParam String query, @RequestParam String wikiLangTarget) {
		return buildRequestResponse(new SearchRequest(query, wikiLangTarget).fetchSearchResults(wikidata));
	}

	/**
	 * Initializes a new graphset with the specified origin entity and language
	 * target.
	 * This endpoint creates a new graph structure starting from a given Wikidata
	 * entity
	 * and configures it for the specified language target.
	 *
	 * <p>
	 * The graphset initialization process includes:
	 * <ul>
	 * <li>Validating the origin entity ID and language target parameters</li>
	 * <li>Creating a new GraphsetRequest with the provided parameters</li>
	 * <li>Initializing the graph structure through the Wikidata service</li>
	 * <li>Setting up the foundational graph data and metadata</li>
	 * </ul>
	 *
	 * <p>
	 * This endpoint is essential for starting new graph exploration sessions,
	 * providing the initial data structure that can be expanded through subsequent
	 * graph filling operations. The response includes the initialized graphset
	 * and any errors encountered during the initialization process.
	 *
	 * @param originID       the unique identifier of the Wikidata entity to use as
	 *                       the
	 *                       graph origin, cannot be null or empty
	 * @param wikiLangTarget the target language code for the graph content,
	 *                       cannot be null or empty
	 * @return ResponseEntity containing the GraphsetRequest with initialized
	 *         graphset data and appropriate HTTP status code
	 * @see GraphsetRequest
	 * @see WikidataService
	 * @see #buildRequestResponse(Request)
	 * @author The Wikiverse Team
	 * @version 1.0
	 * @since 1.0
	 */
	@GetMapping("api/graphset/initialize")
	public ResponseEntity<Request> getInitializedGraphset(
			@RequestParam String targetID,
			@RequestParam String wikiLangTarget,
			@RequestParam String prefers3D) {
		return buildRequestResponse(new GraphsetRequest(targetID, wikiLangTarget).initializeGraphset(wikidata));
	}

	// !PRIVATE ============================================================>
	// !PRIVATE ============================================================>

	/**
	 * Builds an appropriate HTTP response entity for a request based on its error
	 * status.
	 * This method examines the request to determine if an error occurred and
	 * constructs
	 * a ResponseEntity with the appropriate HTTP status code and request body.
	 *
	 * <p>
	 * The method handles response building by:
	 * <ul>
	 * <li>Checking if the request encountered an error</li>
	 * <li>Setting HTTP status code based on the error's httpStatusCode() if
	 * present</li>
	 * <li>Defaulting to 200 OK status for successful requests</li>
	 * <li>Including the request object as the response body for debugging and
	 * tracking</li>
	 * </ul>
	 *
	 * <p>
	 * This utility method provides consistent error handling across all controller
	 * endpoints by ensuring that error responses include appropriate HTTP status
	 * codes while successful responses use standard 200 OK status.
	 *
	 * @param request the Request object containing timing information and potential
	 *                error details
	 * @return ResponseEntity with appropriate HTTP status code and the request as
	 *         body
	 * @see Request#errored()
	 * @see Request#getError()
	 * @see WikiverseError
	 */
	private ResponseEntity<Request> buildRequestResponse(Request request) {
		return request.markCompleted().errored()
				? ResponseEntity.status(request.getError().httpStatusCode()).body(request)
				: ResponseEntity.status(200).body(request);
	}
}
