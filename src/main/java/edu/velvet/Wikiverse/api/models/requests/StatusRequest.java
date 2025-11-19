package edu.velvet.Wikiverse.api.models.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataService;
import java.time.Instant;

/**
 * Represents a status request for monitoring the health and performance of the
 * Wikiverse system.
 * This class extends the base Request class and provides comprehensive status
 * information
 * about the Wikiverse application and its external dependencies, particularly
 * the Wikidata service.
 *
 * <p>
 * This class provides methods to manage status information including:
 * <ul>
 * <li>Wikiverse system status monitoring</li>
 * <li>Wikidata service connectivity and health checks</li>
 * <li>Performance metrics such as ping response times</li>
 * <li>Test entity validation for service verification</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping. The test
 * target
 * entity is marked with {@code @JsonIgnore} to exclude it from serialization.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Request
 * @see WikidataService
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class StatusRequest extends Request {

	/** The status of the Wikiverse system. */
	private String wikiverseStatus;

	/** The status of the Wikidata service. */
	private String wikidataStatus;

	/** The ping response time to Wikidata service in milliseconds. */
	private Long wikidataPingMillis;

	/** https://www.wikidata.org/wiki/Q4115189 ==> "Sandbox Item" */
	@JsonIgnore
	private final String testTargetEntity = "Q4115189";

	/**
	 * Gets the status of the Wikiverse system.
	 *
	 * @return the Wikiverse status, or null if not set
	 */
	public String getWikiverseStatus() {
		return wikiverseStatus;
	}

	/**
	 * Sets the status of the Wikiverse system.
	 *
	 * @param wikiverseStatus the Wikiverse status, can be null
	 */
	public void setWikiverseStatus(String wikiverseStatus) {
		this.wikiverseStatus = wikiverseStatus;
	}

	/**
	 * Gets the status of the Wikidata service.
	 *
	 * @return the Wikidata status, or null if not set
	 */
	public String getWikidataStatus() {
		return wikidataStatus;
	}

	/**
	 * Sets the status of the Wikidata service.
	 *
	 * @param wikidataStatus the Wikidata status, can be null
	 */
	public void setWikidataStatus(String wikidataStatus) {
		this.wikidataStatus = wikidataStatus;
	}

	/**
	 * Gets the ping response time to Wikidata service.
	 *
	 * @return the ping response time in milliseconds, or null if not set
	 */
	public Long getWikidataPingMillis() {
		return wikidataPingMillis;
	}

	/**
	 * Sets the ping response time to Wikidata service.
	 *
	 * @param wikidataPing the ping response time in milliseconds, can be null
	 */
	public void setWikidataPing(Long wikidataPing) {
		this.wikidataPingMillis = wikidataPing;
	}

	/**
	 * Checks the status of both the Wikiverse system and the Wikidata service.
	 * This method performs a health check by attempting to fetch a test entity
	 * from the Wikidata service and measuring the response time.
	 *
	 * <p>
	 * The method updates the following fields based on the health check results:
	 * <ul>
	 * <li>{@code wikiverseStatus} - Always set to "Wikiverse is ONLINE"</li>
	 * <li>{@code wikidataStatus} - Set to "Wikidata is ONLINE" on success,
	 * "Wikidata is OFFLINE" on failure</li>
	 * <li>{@code wikidataPing} - Set to the response time in milliseconds on
	 * success</li>
	 * <li>Error information - Set if the Wikidata service is unavailable</li>
	 * </ul>
	 *
	 * <p>
	 * The health check uses a test entity fetch operation to verify connectivity
	 * and functionality of the Wikidata service. If the operation fails, the error
	 * details are captured and stored in this request object for debugging
	 * purposes.
	 *
	 * @return this StatusRequest instance with updated status information
	 * @see WikidataService
	 * @see #setError(WikiverseError)
	 */
	public StatusRequest checkServiceStatus(WikidataService wikidata) {
		Instant startTime = Instant.now();
		this.wikiverseStatus = "Wikiverse is ONLINE";
		wikidata
			.api()
			.fetchEntityByID(testTargetEntity)
			.fold(
				error -> {
					this.setError(error);
					this.wikidataStatus = "Wikidata is OFFLINE";
					return null;
				},
				entity -> {
					this.wikidataPingMillis = Instant.now().toEpochMilli() - startTime.toEpochMilli();
					this.wikidataStatus = "Wikidata is ONLINE";
					return null;
				}
			);

		return this;
	}
}
