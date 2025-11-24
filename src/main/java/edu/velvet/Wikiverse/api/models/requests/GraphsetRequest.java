package edu.velvet.Wikiverse.api.models.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.velvet.Wikiverse.api.models.WikiverseError;
import edu.velvet.Wikiverse.api.models.core.Edge;
import edu.velvet.Wikiverse.api.models.core.Graphset;
import edu.velvet.Wikiverse.api.models.core.Metadata;
import edu.velvet.Wikiverse.api.models.core.Property;
import edu.velvet.Wikiverse.api.models.core.Vertex;
import edu.velvet.Wikiverse.api.services.layout.FR3DLayout;
import edu.velvet.Wikiverse.api.services.logging.WikidataDocumentLogger;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataService;
import io.vavr.control.Either;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GraphsetRequest extends Request {

	@JsonIgnore
	private final WikidataDocumentLogger logger = new WikidataDocumentLogger("graphset-request.log");

	private final Metadata metadata;
	private final Graphset graphset;

	/**
	 * Constructs a new {@code GraphsetRequest} for initializing the origin graph
	 * entity.
	 * <p>
	 * This constructor is used by the <b>GET (api/graphset/initialize-origin)</b>
	 * endpoint defined in
	 * the WikiverseRequestsController. It initializes a new graphset with the
	 * provided origin entity ID,
	 * target language, and 3D preference, then immediately fetches the
	 * corresponding Wikidata entity
	 * using the supplied {@link WikidataService} instance. The graphset is
	 * populated with the origin
	 * vertex and related data ingested from the Wikidata API.
	 *
	 * <ul>
	 * <li>Creates the {@link Metadata} object with the specified parameters.</li>
	 * <li>Creates an empty {@link Graphset} instance.</li>
	 * <li>Calls {@code initializeOrigin} to fetch and populate the initial origin
	 * entity and its relationships.</li>
	 * <li>If any error occurs during data retrieval or ingestion, sets this
	 * request's error state.</li>
	 * </ul>
	 *
	 * @param originID       the unique identifier for the Wikidata entity to use as
	 *                       the graph origin; must not be null
	 * @param wikiLangTarget the target language code for rendering graph content;
	 *                       must not be null
	 * @param prefers3D      user preference flag for 3D visualization (as a string)
	 * @param wikidata       the {@link WikidataService} instance for data fetching
	 *                       and ingestion; must not be null
	 */
	public GraphsetRequest(String originID, String wikiLangTarget, String prefers3D, WikidataService wikidata) {
		this.metadata = new Metadata(originID, wikiLangTarget, prefers3D);
		this.graphset = new Graphset();
		initializeOrigin(wikidata);
	}

	// POST (api/graphset/initialize-data)
	@JsonCreator
	public GraphsetRequest(@JsonProperty("metadata") Metadata data, @JsonProperty("graphset") Graphset graph) {
		this.metadata = data;
		this.graphset = graph;
	}

	public Metadata getMetadata() {
		return this.metadata;
	}

	public Graphset getGraphset() {
		return this.graphset;
	}

	/**
	 * Initializes the origin of the graphset by fetching the Wikidata entity
	 * corresponding
	 * to the origin ID specified in this request's metadata and populating the
	 * graphset
	 * with the entity's data.
	 * <p>
	 * This method uses the provided {@link WikidataService} instance to fetch the
	 * Wikidata
	 * entity for the origin ID and target language. Upon successful fetch, the
	 * entity
	 * document is ingested into this graphset request to set up the initial vertex
	 * and
	 * related structure. If an error occurs during the fetch, it sets the error
	 * state
	 * of this request.
	 *
	 * @param wikidata the {@link WikidataService} instance used to fetch the origin
	 *                 entity
	 */
	@JsonIgnore
	private void initializeOrigin(WikidataService wikidata) {
		String wikiLangTarget = this.metadata.getWikiLangTarget();

		wikidata
				.api()
				.fetchEntityByID(this.metadata.getOriginID(), wikiLangTarget)
				.fold(
						wikiverseError -> {
							this.setError(wikiverseError);
							return null;
						},
						result -> {
							ingestFetchedDocumentResult(result, wikidata);
							// Lock Origin Position in Place (@ 0,0,0)
							Vertex origin = this.graphset.getVertexByID(this.metadata.getOriginID());
							if (origin != null) {
								origin.lock();
							}
							return null;
						});
	}

	/**
	 * !! WORKING HERE WORKING HERE WORKING HERE WORKING HERE WORKING HERE WORKING
	 * !! WORKING HERE WORKING HERE WORKING HERE WORKING HERE WORKING HERE WORKING
	 * !! WORKING HERE WORKING HERE WORKING HERE WORKING HERE WORKING HERE WORKING
	 */
	@JsonIgnore
	public GraphsetRequest initializeData(WikidataService wikidata) {
		// ? Setup queue, retry count, and fetched document list
		AtomicInteger failedRequestCount = new AtomicInteger(0);
		List<String> fetchQueue = this.graphset.getUnfetchedEntityList();
		List<EntityDocument> fetchedDocs = new ArrayList<>();
		String wikiLangTarget = this.metadata.getWikiLangTarget();

		// ? Fetch all documents from the Wikidata API, allow 3 retries for API Offline
		while (!fetchQueue.isEmpty() && failedRequestCount.get() < 3) {
			wikidata
					.api()
					.fetchEntitiesByIDs(getRequestBatch(fetchQueue), wikiLangTarget)
					.fold(
							// ? Handle Error Counting for API Requests, retries 3 times.
							(WikiverseError error) -> handleInitializeDataRequestFailed(error, failedRequestCount),
							// ? Handle API Fetch Success, add result to Queue and Remove Key from List
							(Map<String, Either<WikiverseError, EntityDocument>> results) -> {
								results.forEach((String key, Either<WikiverseError, EntityDocument> result) -> {
									result.map((EntityDocument doc) -> fetchedDocs.add(doc));

									/**
									 * Result being an Error could only be a NoSuchResults found error. QID/PID
									 * values are sourced directly from Wikidata this shouldn't happen, either way
									 * just defaults to removing this from the queue.
									 */
									fetchQueue.remove(key);
								});
								return null;
							});
		}

		// ? Ingest/Process documents
		fetchedDocs.forEach(entDoc -> {
			ingestFetchedDocumentResult(entDoc, wikidata);
		});

		// ? Run the Layout Algorithm
		new FR3DLayout(this.getGraphset(), this.metadata.getLayoutSettings()).runLayout(graphset);

		return this;
	}

	// !PRIVATE ============================================================>
	// !PRIVATE ============================================================>

	/**
	 * Handles the processing of a single fetched {@link EntityDocument} and updates
	 * the graphset accordingly.
	 * <p>
	 * Based on the type of {@link EntityDocument} (either {@link ItemDocument} or
	 * {@link PropertyDocument}),
	 * this method constructs the appropriate model object ({@link Vertex} or
	 * {@link Property}),
	 * processes its statements (when applicable), and adds the resultant elements
	 * to the underlying graphset.
	 * <ul>
	 * <li>For an {@link ItemDocument}, it creates a {@link Vertex}, processes its
	 * claims and edges,
	 * attaches these to the vertex and graphset, respectively.</li>
	 * <li>For a {@link PropertyDocument}, it creates a {@link Property} and adds it
	 * to the graphset's property collection.</li>
	 * </ul>
	 * If an unknown entity document type is encountered, it performs no action but
	 * may be extended for additional types.
	 *
	 * @param doc      the fetched {@link EntityDocument} (cannot be null)
	 * @param wikidata the {@link WikidataService} used to process document
	 *                 statements and for accessing document processing utilities
	 *                 (cannot be null)
	 */
	private void ingestFetchedDocumentResult(EntityDocument doc, WikidataService wikidata) {
		String wikiLangTarget = this.metadata.getWikiLangTarget();

		switch (doc) {
			case ItemDocument iDoc -> {
				Vertex vert = Vertex.createOrNull(iDoc, wikiLangTarget);

				if (vert == null) {
					// Unable to create Vertex (no SiteLink), remove all mentions from Graphset
					this.graphset.removeAnyEntitiesMatchingID(doc.getEntityId().getId());
					return;
				}

				// Otherwise Ingest Edges and Claims, and add to Graphset
				List<Edge> edges = wikidata.docProc().ingestDocumentsStatements(iDoc);

				// Add new Ent's to the Graphset
				this.graphset.getEdges().addAll(edges);
				this.graphset.getVertices().add(vert);
			}
			case PropertyDocument pDoc -> {
				// Add Props to the Graphset
				Property prop = new Property(pDoc, wikiLangTarget);
				this.graphset.getProperties().add(prop);
			}
			default -> {
				// TODO Handle Unknown Entity Document Type
				logger.log(
						"GraphsetRequest().ingestFetchedDocumentResult() - Unknown EntityDocument type found - " +
								doc.getClass(),
						doc);
			}
		}
	}

	/**
	 * Handles failures when initializing data in the graphset request.
	 * <p>
	 * Increments the failed request count each time an API call fails. If the
	 * number of failed
	 * requests reaches three or more, sets the encountered error on this
	 * GraphsetRequest
	 * using {@link #setError(WikiverseError)}.
	 *
	 * @param error              the {@link WikiverseError} encountered during the
	 *                           API request
	 * @param failedRequestCount an {@link AtomicInteger} tracking the number of
	 *                           failed requests
	 * @return always returns {@code null} (used as a functional programming
	 *         placeholder)
	 */
	private Object handleInitializeDataRequestFailed(WikiverseError error, AtomicInteger failedRequestCount) {
		failedRequestCount.incrementAndGet();
		if (failedRequestCount.get() >= 3) {
			this.setError(error);
		}
		return null;
	}

	/**
	 * Returns a batch of entity IDs to be fetched from the fetch queue.
	 * <p>
	 * If the size of the {@code fetchQueue} exceeds 50, this method returns a
	 * sublist containing the first 50 elements (IDs); otherwise, it returns a new
	 * list containing all elements of the {@code fetchQueue}. This approach helps
	 * in batching requests and limiting the number of IDs processed or sent in a
	 * single operation, which can be important for API limits or efficiency.
	 * </p>
	 *
	 * @param fetchQueue the complete list of entity IDs pending fetching (must not
	 *                   be null)
	 * @return a list of up to 50 entity IDs to process in the current batch
	 */
	private List<String> getRequestBatch(List<String> fetchQueue) {
		return fetchQueue.size() > 50 ? fetchQueue.subList(0, 50) : new ArrayList<>(fetchQueue);
	}
}
