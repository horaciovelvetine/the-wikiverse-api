package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataSnak;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataSnakGroup;
import java.util.ArrayList;
import java.util.List;
import org.wikidata.wdtk.datamodel.interfaces.Statement;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Claim {

	private final WikidataSnak main;
	private final List<WikidataSnakGroup> qualifiers = new ArrayList<>();

	/**
	 * Constructs a Claim object from a Wikidata Statement.
	 *
	 * <p>
	 * This constructor extracts the main snak from the provided statement
	 * and initializes the qualifiers list by converting each SnakGroup
	 * in the statement's qualifiers to a {@link WikidataSnakGroup}.
	 * Only qualifier groups with at least one Snak are included.
	 *
	 * @param statement the Wikidata Statement object to extract claim data from,
	 *                  must not be null
	 */
	public Claim(Statement statement, WikidataSnak mainSnak) {
		this.main = mainSnak;
		// Check if statements to add...
		if (!statement.getQualifiers().isEmpty()) {
			statement
				.getQualifiers()
				.forEach(group -> {
					// Create a SnakGroup which captures and defines group
					WikidataSnakGroup qualifier = new WikidataSnakGroup(group);
					// If there are snaks added, also add the qualifier
					if (!qualifier.getSnaks().isEmpty()) {
						this.qualifiers.add(qualifier);
					}
				});
		}
	}

	/**
	 * Constructs a Claim object from JSON deserialization.
	 * This constructor is used by Jackson to deserialize Claim from JSON.
	 *
	 * @param main the main WikidataSnak for this claim
	 * @param qualifiers the list of WikidataSnakGroup qualifiers for this claim
	 */
	@JsonCreator
	public Claim(
		@JsonProperty("main") WikidataSnak main,
		@JsonProperty("qualifiers") List<WikidataSnakGroup> qualifiers
	) {
		this.main = main;
		if (qualifiers != null) {
			this.qualifiers.addAll(qualifiers);
		}
	}

	public WikidataSnak getMain() {
		return main;
	}

	public List<WikidataSnakGroup> getQualifiers() {
		return qualifiers;
	}
}
