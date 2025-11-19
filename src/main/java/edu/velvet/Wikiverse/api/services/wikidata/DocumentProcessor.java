package edu.velvet.Wikiverse.api.services.wikidata;

import edu.velvet.Wikiverse.api.models.core.Claim;
import edu.velvet.Wikiverse.api.models.core.Edge;
import edu.velvet.Wikiverse.api.models.core.SearchResult;
import edu.velvet.Wikiverse.api.models.core.Vertex;
import edu.velvet.Wikiverse.api.services.logging.WikidataDocumentLogger;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

/**
 * Service responsible for processing and transforming Wikidata search results
 * into
 * application-specific data structures.
 *
 * <p>
 * This service provides functionality to convert raw Wikidata search results
 * from the Wikidata Toolkit into the internal SearchResult model used by the
 * Wikiverse application. It acts as a bridge between external Wikidata APIs
 * and the application's data layer.
 *
 * <p>
 * The class provides methods to:
 * <ul>
 * <li>Transform Wikidata search entities into SearchResult objects</li>
 * <li>Handle batch processing of multiple search results</li>
 * <li>Maintain data integrity during transformation</li>
 * </ul>
 *
 * <p>
 * This service is automatically detected and managed by Spring's dependency
 * injection framework through the @Service annotation.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see SearchResult
 * @see WbSearchEntitiesResult
 */
@Service
public class DocumentProcessor {

	private static final String SOURCE = "DocumentProcessor";

	private final WikidataDocumentLogger logger;

	/**
	 * Constructs a new DocumentProcessor with the specified logger.
	 *
	 * @param logger the WikidataDocumentLogger to use for logging operations
	 */
	public DocumentProcessor(WikidataDocumentLogger logger) {
		this.logger = logger;
	}

	/**
	 * Transforms a list of Wikidata search entity results into SearchResult
	 * objects.
	 *
	 * <p>
	 * This method processes raw Wikidata search results and converts them into
	 * the application's internal SearchResult model. Each WbSearchEntitiesResult
	 * is mapped to a corresponding SearchResult object using the SearchResult
	 * constructor that accepts a WbSearchEntitiesResult parameter.
	 *
	 * <p>
	 * The transformation preserves all relevant data from the original search
	 * results while adapting it to the application's data structure requirements.
	 * The resulting list maintains the same order as the input list.
	 *
	 * @param results the list of Wikidata search entity results to process,
	 *                cannot be null but can be empty
	 * @return a list of SearchResult objects corresponding to the input results,
	 *         never null but may be empty if input is empty
	 * @throws IllegalArgumentException if results is null
	 * @see SearchResult#SearchResult(WbSearchEntitiesResult)
	 */
	public List<SearchResult> ingestWikidataSearchResults(List<WbSearchEntitiesResult> results) {
		return logger
			.log(SOURCE + ".ingestWikidataSearchResults()", results)
			.stream()
			.map(result -> new SearchResult(result))
			.collect(Collectors.toList());
	}

	public Tuple2<List<Edge>, List<Claim>> ingestDocumentsStatements(ItemDocument doc) {
		logger.log(SOURCE + ".ingestDocumentsStatements(" + doc.getEntityId().getId() + ")", doc);

		List<Edge> edges = new ArrayList<>();
		List<Claim> claims = new ArrayList<>();

		// Iterate over all of the Statements and Check for any which qualify as Edges
		doc
			.getAllStatements()
			.forEachRemaining((Statement statement) -> {
				// * Check for Exclusionary Statement Details (null/external-id/monolingual)
				if (this.statementDefinesExclusionaryInfo(statement)) {
					return;
				}

				// * Check for properties present which we don't care about
				if (this.statementIncludesFilteredProperty(statement)) {
					return;
				}

				// * Statement Defines Relevant Data, Create Edges -or- Claims w/ Statement
				WikidataSnak mainSnak = statement.getMainSnak().accept(new WikidataSnak());
				if (mainSnak.hasWikidataEntityTarget()) {
					// Create MainSank Edge and add to List
					edges.add(new Edge(statement, mainSnak));

					// Check for QualifiedSnakEdge and also add to list
					statement
						.getQualifiers()
						.forEach(group -> {
							group
								.getSnaks()
								.forEach(snak -> {
									WikidataSnak qualifiedSnak = snak.accept(new WikidataSnak());
									if (qualifiedSnak.hasWikidataEntityTarget()) {
										edges.add(new Edge(statement, mainSnak, qualifiedSnak));
									}
									// TODO: Handle qualified non-edge Claims?
								});
						});
				} else {
					// Statement outlines a non-target Claim, not an Edge, save for Claim details.
					claims.add(new Claim(statement, mainSnak));
				}
			});

		return Tuple.of(edges, claims);
	}

	// !PRIVATE ============================================================>
	// !PRIVATE ============================================================>

	/**
	 * Processes an {@link EntityDocument} to initialize and ingest a {@link Vertex}
	 * into the graphset.
	 * <p>
	 * This method extracts relevant information from the given entity document,
	 * such as the entity's label,
	 * description, and Wikipedia URL based on a target language. It iterates
	 * through all statements of the document,
	 * excluding those that match exclusionary or filtered criteria, and processes
	 * each statement to determine whether
	 * it represents edge data or property claim data. Claims are added as
	 * attributes to the vertex, while edge data
	 * is handled separately (see implementation for extension).
	 * </p>
	 * <p>
	 * Once the vertex and its associated claims are constructed, the vertex is
	 * added to the {@code GraphsetRequest}'s graphset.
	 * </p>
	 *
	 * @param doc     the Wikidata entity document containing statements and entity
	 *                info to ingest (must not be null)
	 * @param request the graphset request context, including metadata such as
	 *                language and the graphset to update (must not be null)
	 */
	private boolean statementDefinesExclusionaryInfo(Statement statement) {
		WikidataSnak mainSnak = statement.getMainSnak().accept(new WikidataSnak());

		/**
		 * ? mainSnak has a null value for one of: datatype, property, value
		 *
		 * ? Do not include for missing some component of data
		 */
		if (mainSnak.isNull()) {
			return true;
		}

		/**
		 * ? mainSnak points to an external source/link which is hard to decipher/use
		 *
		 * ? Do not include for exteranl sourcing/pathing
		 */
		if (mainSnak.getDatatype().equals("external-id")) {
			return true;
		}
		/**
		 * ? mainSnak is a 'monolingual value' which is typically a value containing
		 * ? repetitive data such as the label/description
		 * ? Users select a language target so these can be safely ignored
		 *
		 * ? Do not include for repetitive information
		 */
		return mainSnak.getDatatype().equals("monolingualtext");
	}

	/**
	 * Checks whether the given statement includes a property that should be
	 * filtered out
	 * from further processing. This filtering is based on a predefined list of
	 * property
	 * IDs that are considered irrelevant or unsuitable for inclusion in the
	 * application's data model.
	 *
	 * @param statement the Wikidata Statement to check for filtered properties
	 * @return true if the statement's main property matches a filtered property ID,
	 *         false otherwise
	 */
	private boolean statementIncludesFilteredProperty(Statement statement) {
		WikidataSnak mainSnak = statement.getMainSnak().accept(new WikidataSnak());
		List<String> filteredProperties = List.of(
			"P8687", // SOCIAL MEDIA FOLLOWERS
			"P9352", // PORTRAIT ARCHIVE ID / NON_EXTERNAL ID MASQUERADING
			"P935", // COMMONS GALLERY TITLE
			"P373", // WIKIMEDIA COMMONS CATEGORY
			"P1343" // DESCRIBED BY SOURCE
		);
		return filteredProperties.contains(mainSnak.getProperty().getValue());
	}
}
