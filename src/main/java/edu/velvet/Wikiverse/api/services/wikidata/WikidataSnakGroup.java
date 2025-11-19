package edu.velvet.Wikiverse.api.services.wikidata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import java.util.stream.Collectors;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WikidataSnakGroup {

	/* List of Snak's inside the group, typically as references or qualifiers */
	private List<WikidataSnak> snaks;

	private WikidataValue property = null;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public WikidataSnakGroup() {}

	/**
	 * Constructs a WikidataSnakGroup from a given SnakGroup.
	 *
	 * <p>
	 * This constructor takes a {@link SnakGroup} from the Wikidata Toolkit API and
	 * transforms its list of Snaks into
	 * a list of {@link WikidataSnak} objects. Null snaks and snaks with a datatype
	 * of "external-id" are filtered out,
	 * as external-ids are typically not relevant for further processing within the
	 * application.
	 *
	 * <p>
	 * If the group contains more than one snak after filtering, the property
	 * information from the original SnakGroup
	 * is also converted to a {@link WikidataValue} and assigned to this instance.
	 *
	 * @param group the original SnakGroup to extract snaks and property from, must
	 *              not be null
	 */
	public WikidataSnakGroup(SnakGroup group) {
		this.snaks = group.getSnaks() == null
			? List.of()
			: group
				.getSnaks()
				.stream()
				.map(snak -> snak != null ? snak.accept(new WikidataSnak()) : null)
				// Compare to string in case snak.getDatatype() is null
				.filter(
					snak ->
						snak != null &&
						!"external-id".equals(snak.getDatatype()) &&
						!"monolingualtext".equals(snak.getDatatype())
				)
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
