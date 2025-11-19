package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents metadata configuration for graph layout algorithms in the
 * Wikiverse system.
 * This class contains all the parameters necessary to control how vertices are
 * positioned
 * and arranged in 3D space during the graph layout process.
 *
 * <p>
 * This class provides configuration for various aspects of graph layout
 * including:
 * <ul>
 * <li>Settings related to the layout preferences set by the user</li>
 * <li>Wikipedia language and origin vertex specifications</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping. Some fields
 * are marked with @JsonIgnore to prevent serialization of internal references.
 *
 * <p>
 * Default values are provided in the constructor to ensure reasonable
 * layout behavior out of the box, while allowing customization through
 * getter and setter methods for mutable parameters.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Vertex
 * @see LayoutSettings
 * @see Point3D
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Metadata {

	private String originID;

	@JsonIgnore
	private Vertex origin;

	private final LayoutSettings layoutSettings;

	private String wikiLangTarget = "en";

	public Metadata(String origindID, String wikiLangTarget, String prefers3D) {
		this.originID = origindID;
		this.wikiLangTarget = wikiLangTarget;
		this.layoutSettings = new LayoutSettings(prefers3D);
	}

	/**
	 * Constructs a Metadata object from JSON deserialization.
	 * This constructor is used by Jackson to deserialize Metadata from JSON.
	 *
	 * @param originID the unique identifier of the Wikidata entity to use as the graph origin
	 * @param wikiLangTarget the target language code for the graph content
	 * @param layoutSettings the layout settings configuration
	 */
	@JsonCreator
	public Metadata(
		@JsonProperty("originID") String originID,
		@JsonProperty("wikiLangTarget") String wikiLangTarget,
		@JsonProperty("layoutSettings") LayoutSettings layoutSettings
	) {
		this.originID = originID;
		this.wikiLangTarget = wikiLangTarget;
		this.layoutSettings = layoutSettings;
	}

	/**
	 * Gets the origin ID of the graph layout.
	 *
	 * @return the origin ID string, or null if not set
	 */
	public String getOriginID() {
		return originID;
	}

	/**
	 * Sets the origin ID for the graph layout.
	 *
	 * @param originID the origin ID string, can be null
	 */
	public void setOriginID(String originID) {
		this.originID = originID;
	}

	/**
	 * Gets the origin vertex of the graph layout.
	 *
	 * @return the origin vertex, or null if not set
	 */
	public Vertex getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin vertex for the graph layout.
	 *
	 * @param origin the origin vertex, can be null
	 */
	public void setOrigin(Vertex origin) {
		this.origin = origin;
	}

	/**
	 * Gets the target Wikipedia language for the graph layout.
	 *
	 * @return the wiki language target (e.g., "en"), or null if not set
	 */
	public String getWikiLangTarget() {
		return wikiLangTarget;
	}

	/**
	 * Sets the target Wikipedia language for the graph layout.
	 *
	 * @param wikiLanguageTarget the wiki language target (e.g., "en"), can be null
	 */
	public void setWikiLangTarget(String wikiLanguageTarget) {
		this.wikiLangTarget = wikiLanguageTarget;
	}

	public LayoutSettings getLayoutSettings() {
		return this.layoutSettings;
	}
}
