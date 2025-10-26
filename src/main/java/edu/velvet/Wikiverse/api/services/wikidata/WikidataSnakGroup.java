package edu.velvet.Wikiverse.api.services.wikidata;

import java.util.List;
import java.util.stream.Collectors;

import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WikidataSnakGroup {

	/* List of Snak's inside the group, typically as references or qualifiers */
	private List<WikidataSnak> snaks;

	private WikidataValue property = null;

	public WikidataSnakGroup(SnakGroup group) {
		this.snaks = group.getSnaks() == null
			? List.of()
			: group
				.getSnaks()
				.stream()
				.map(snak -> snak != null ? snak.accept(new WikidataSnak()) : null)
				.filter(snak -> snak != null && !"external-id".equals(snak.getDatatype()))
				.collect(Collectors.toList());

		if (this.snaks.size() > 1) {
			this.property = group.getProperty() != null ? group.getProperty().accept(new WikidataValue()) : null;
		}
	}

	public WikidataValue getProperty() {
		return this.property;
	}

	public List<WikidataSnak> getSnaks() {
		return snaks;
	}

	public void setSnaks(List<WikidataSnak> snaks) {
		this.snaks = snaks;
	}
}
