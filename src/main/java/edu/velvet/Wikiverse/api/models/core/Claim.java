package edu.velvet.Wikiverse.api.models.core;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import edu.velvet.Wikiverse.api.services.wikidata.WikidataReference;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataSnak;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataSnakGroup;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Claim {

	private WikidataSnak main;
	private final List<WikidataSnakGroup> qualifiers = new ArrayList<>();
	private final List<WikidataReference> references = new ArrayList<>();

	public WikidataSnak getMain() {
		return main;
	}

	public void setMain(WikidataSnak main) {
		this.main = main;
	}

	public List<WikidataSnakGroup> getQualifiers() {
		return qualifiers;
	}

	public void addQualifier(WikidataSnakGroup group) {
		this.qualifiers.add(group);
	}

	public List<WikidataReference> getReferences() {
		return references;
	}

	public void addReference(WikidataReference ref) {
		this.references.add(ref);
	}
}
