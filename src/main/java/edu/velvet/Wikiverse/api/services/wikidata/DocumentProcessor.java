package edu.velvet.Wikiverse.api.services.wikidata;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.implementation.ItemDocumentImpl;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

import edu.velvet.Wikiverse.api.models.core.SearchResult;
import edu.velvet.Wikiverse.api.models.core.Vertex;
import edu.velvet.Wikiverse.api.models.requests.GraphsetRequest;
import edu.velvet.Wikiverse.api.services.logging.WikidataDocumentLogger;

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

	public void ingestInitialGraphsetDocument(
			EntityDocument doc,
			GraphsetRequest request,
			DefaultWikidataFilters filters) {
		logger.log(SOURCE + ".ingestInitialGraphsetDocument(" + doc.getEntityId().getId() + ")", doc);

		if (doc instanceof ItemDocumentImpl itemDoc) {
			// ? create the origin from the result
			Vertex origin = new Vertex(itemDoc, request.getMetadata().getWikiLangTarget());
			request.getGraphset().addVertex(origin);
		}
	}

	// public void createUnfetchedEntitiesFromStatements(
	// ItemDocumentImpl doc,
	// GraphsetRequest request,
	// DefaultWikidataFilters filters
	// ) {
	// Iterator<Statement> statements = doc.getAllStatements();
	// while (statements.hasNext()) {
	// Statement statement = statements.next();
	// WikidataSnak mainSnak = statement.getMainSnak().accept(new WikidataSnak());

	// // ? check if this snak is something we don't want in our set...

	// // ? Check if the mainSnak is incomplete data
	// if (mainSnak.isNull()) {
	// // skip this
	// continue;
	// }

	// // ? Check if the value for the snak is null
	// if (mainSnak.getValue().isNull()) {
	// // skip this
	// continue;
	// }

	// // ? Check if excluded datatype
	// if (filters.isFilteredDataType(mainSnak.getDatatype())) {
	// // skip this
	// continue;
	// }

	// // ? Check if the value of this snak is a filtered EntID
	// if (filters.isFilteredWikidataID(mainSnak.getValue().getValue())) {
	// // skip this
	// continue;
	// }

	// // we like this keep it
	// System.out.println("Keep this");
	// }
	// }
}
