package edu.velvet.Wikiverse.api.services.wikidata;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.SiteLink;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementDocument;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

import edu.velvet.Wikiverse.api.models.core.Claim;
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
			GraphsetRequest request) {
		logger.log(SOURCE + ".ingestInitialGraphsetDocument(" + doc.getEntityId().getId() + ")", doc);

		Vertex origin = new Vertex();

		String lang = request.getMetadata().getWikiLangTarget();
		if (doc instanceof ItemDocument iDoc) {
			origin.setId(doc.getEntityId().getId());
			origin.setDescription(iDoc.findDescription(lang));
			origin.setLabel(iDoc.findLabel(lang));
			Map<String, SiteLink> links = iDoc.getSiteLinks();
			SiteLink link = links.get(lang + "wiki");
			String pageTitle = link.getPageTitle();
			pageTitle = pageTitle.replace(" ", "_").replace("%3A", ":").replace("%2F", "/");
			String encodedTitle;
			try {
				encodedTitle = URLEncoder.encode(pageTitle, "utf-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UTF-8 Encoding is not supported in this RunTime", e);
			}
			String PROTOCOL = "https://";
			String SITE_MAIN = ".wikipedia.org/wiki/";
			origin.setUrl(PROTOCOL + lang + SITE_MAIN + encodedTitle);
		}

		// Statements
		if (doc instanceof StatementDocument sDoc) {
			Iterator<Statement> statements = sDoc.getAllStatements();
			while (statements.hasNext()) {
				// Set subject/establish claim
				Claim claim = new Claim();

				// Sets main Snak()
				Statement statement = statements.next();
				WikidataSnak main = statement.getMainSnak().accept(new WikidataSnak());
				claim.setMain(main);

				if (main.getDatatype() != null && main.getDatatype().equals("external-id")) {
					// Ignore any external-id snaks... skip to next
					continue;
				}

				if (main.getDatatype() != null && main.getDatatype().equals("monolingualtext")) {
					// Ignore monolingual text restatements of properties...
					continue;
				}

				if (main.getProperty() != null && main.getProperty().getValue().equals("P8687")) {
					// Ignore social media followers, data is almost always very stale...
					continue;
				}

				// Set Qualifiers
				List<SnakGroup> qualifiers = statement.getQualifiers();
				for (SnakGroup group : qualifiers) {
					WikidataSnakGroup qualifier = new WikidataSnakGroup(group);
					if (!qualifier.getSnaks().isEmpty()) {
						claim.addQualifier(qualifier);
					}
				}

				// Set References
				List<Reference> references = statement.getReferences();
				for (Reference reference : references) {
					WikidataReference ref = new WikidataReference();
					for (SnakGroup group : reference.getSnakGroups()) {
						WikidataSnakGroup refData = new WikidataSnakGroup(group);
						if (!refData.getSnaks().isEmpty()) {
							ref.addSnakGroup(refData);
						}
					}
					claim.addReference(ref);
				}

				// Add claim back to the initial Vertex
				origin.addClaim(claim);
			}
		}

		request.getGraphset().addVertex(origin);
	}
}
