package edu.velvet.Wikiverse.api.services.wikidata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import edu.velvet.Wikiverse.api.services.logging.Mappable;
import edu.velvet.Wikiverse.api.services.logging.ProcessLogger;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DefaultWikidataFilters implements Mappable {

	@JsonIgnore
	private final String SOURCE = "DefaultWikidataFilters()";

	@JsonIgnore
	private final ProcessLogger logger = new ProcessLogger("default-wikidata-filters.log");

	public final String filterLabel = "Default Wikidata Filters";

	private final Set<IDFilterRecord> ids = ConcurrentHashMap.newKeySet();
	private final Set<String> dataTypes = Set.of(
		"external-id",
		"monolingualtext",
		"commonsMedia",
		"globe-coordinate",
		"geo-shape",
		"wikibase-lexeme"
		// url? /==> not included for now
	);

	public DefaultWikidataFilters() {
		loadDefaultWikidataIDFiltersFromFile();
	}

	public boolean isFilteredDataType(String type) {
		return dataTypes.contains(type);
	}

	public boolean isFilteredWikidataID(String id) {
		return getAllIDValues().contains(id);
	}

	private Set<String> getAllIDValues() {
		return ids.stream().map(IDFilterRecord::id).collect(Collectors.toSet());
	}

	private void loadDefaultWikidataIDFiltersFromFile() {
		try {
			JsonNode root = mapper.readTree(
				new ClassPathResource("data/default-filtered-wikidata-ids.json").getInputStream()
			);

			for (JsonNode entry : root.get("default-ids-list")) {
				String id = entry.get("id").asText();
				String label = entry.get("label").asText();
				String notes = entry.get("notes").asText();

				ids.add(new IDFilterRecord(id, label, notes));
			}
		} catch (IOException e) {
			logger.logError(
				SOURCE +
				".loadDefaultWikidataFilterItemsFromFile() - Failed to load default filtered entities from file.",
				e
			);
		}
	}
}
