package edu.velvet.Wikiverse.api.models.requests;

import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.velvet.Wikiverse.api.models.WikiverseError;
import edu.velvet.Wikiverse.api.models.core.Graphset;
import edu.velvet.Wikiverse.api.models.core.Metadata;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataService;
import io.vavr.control.Either;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GraphsetRequest extends Request {

	private final Metadata metadata;
	private final Graphset graphset;

	// GET (api/graphset/initialize)
	public GraphsetRequest(String originID, String wikiLangTarget) {
		this.metadata = new Metadata(originID, wikiLangTarget);
		this.graphset = new Graphset();
	}

	// POST (api/graphset/fill)
	public GraphsetRequest(Graphset graph, Metadata data) {
		this.metadata = data;
		this.graphset = graph;
	}

	public Metadata getMetadata() {
		return this.metadata;
	}

	public Graphset getGraphset() {
		return this.graphset;
	}

	@JsonIgnore
	public GraphsetRequest initializeGraphset(WikidataService wikidata) {
		Either<WikiverseError, EntityDocument> initialFetch = wikidata
			.api()
			.fetchEntityByID(this.metadata.getOriginID(), this.metadata.getWikiLangTarget());

		if (initialFetch.isLeft()) {
			this.setError(initialFetch.getLeft());
		} else {
			wikidata.docProc().ingestInitialGraphsetDocument(initialFetch.get(), this, wikidata.getDefaultFilter());
		}
		return this;
	}
}
