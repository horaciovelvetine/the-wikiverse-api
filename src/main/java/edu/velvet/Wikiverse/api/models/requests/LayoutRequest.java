package edu.velvet.Wikiverse.api.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.velvet.Wikiverse.api.models.core.Graphset;
import edu.velvet.Wikiverse.api.models.core.Metadata;
import edu.velvet.Wikiverse.api.services.layout.FR3DLayout;

public class LayoutRequest extends Request {

	private final Metadata metadata;
	private final Graphset graphset;

	public LayoutRequest(@JsonProperty("metadata") Metadata data, @JsonProperty("graphset") Graphset graph) {
		this.metadata = data;
		this.graphset = graph;
	}

	@JsonIgnore
	public LayoutRequest updateLayout() {
		new FR3DLayout(graphset, this.metadata.getLayoutSettings()).runLayout(graphset);

		return this;
	}
}
