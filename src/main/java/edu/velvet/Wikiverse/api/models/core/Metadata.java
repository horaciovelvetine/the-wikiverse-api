package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
 * <li>Force-directed algorithm parameters (attraction, repulsion,
 * adjustments)</li>
 * <li>Layout convergence controls (iterations, movement limits,
 * temperature)</li>
 * <li>Vertex positioning and density settings</li>
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
 * @see Point3D
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Metadata {

	private String originID;

	@JsonIgnore
	private Vertex origin;

	private Number attractionMultiplier;

	private Number repulsionMultiplier;

	private final Number[] forceAdjustmentMultipliers;

	private final Number vertexDensity;

	private final Number maxLayoutIterations;

	private final Number maxIterationMovement;

	private final Number temperatureCurveMultiplier;

	private String wikiLangTarget = "en";

	public Metadata(String origindID, String wikiLangTarget) {
		this.originID = origindID;
		this.wikiLangTarget = wikiLangTarget;
		this.attractionMultiplier = 0.5;
		this.repulsionMultiplier = 0.5;
		this.forceAdjustmentMultipliers = new Number[] { 0.9, 1.1 };
		this.vertexDensity = 0.5;
		this.maxIterationMovement = 30;
		this.maxLayoutIterations = 250;
		this.temperatureCurveMultiplier = 30;
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
	 * Gets the attraction multiplier used in force-directed layout algorithms.
	 * Higher values increase the attractive force between connected vertices,
	 * causing them to be drawn closer together in the final layout.
	 *
	 * @return the attraction multiplier
	 */
	public Number getAttractionMultiplier() {
		return attractionMultiplier;
	}

	/**
	 * Sets the attraction multiplier for force-directed layout algorithms.
	 * Higher values increase the attractive force between connected vertices,
	 * causing them to be drawn closer together in the final layout.
	 *
	 * @param attractionMultiplier the attraction multiplier
	 */
	public void setAttractionMultiplier(Number attractionMultiplier) {
		this.attractionMultiplier = attractionMultiplier;
	}

	/**
	 * Gets the repulsion multiplier used in force-directed layout algorithms.
	 * Higher values increase the repulsive force between all vertices,
	 * causing them to spread apart and avoid overlapping.
	 *
	 * @return the repulsion multiplier
	 */
	public Number getRepulsionMultiplier() {
		return repulsionMultiplier;
	}

	/**
	 * Sets the repulsion multiplier for force-directed layout algorithms.
	 * Higher values increase the repulsive force between all vertices,
	 * causing them to spread apart and avoid overlapping.
	 *
	 * @param repulsionMultiplier the repulsion multiplier
	 */
	public void setRepulsionMultiplier(Number repulsionMultiplier) {
		this.repulsionMultiplier = repulsionMultiplier;
	}

	/**
	 * Gets the force adjustment multipliers used to fine-tune the layout algorithm.
	 * These multipliers are applied during different phases of the layout process
	 * to control the convergence behavior and stability of the algorithm.
	 *
	 * @return array of force adjustment multipliers
	 */
	public Number[] getForceAdjustmentMultipliers() {
		return forceAdjustmentMultipliers;
	}

	/**
	 * Gets the vertex density parameter that controls how tightly packed vertices
	 * are.
	 * Higher values result in more compact layouts with vertices positioned closer
	 * together,
	 * while lower values create more spread-out layouts.
	 *
	 * @return the vertex density
	 */
	public Number getVertexDensity() {
		return vertexDensity;
	}

	/**
	 * Gets the maximum number of layout iterations the algorithm will perform.
	 * More iterations generally lead to better convergence and more stable layouts
	 * but require longer computation time.
	 *
	 * @return the maximum layout iterations
	 */
	public Number getMaxLayoutIterations() {
		return maxLayoutIterations;
	}

	/**
	 * Gets the maximum movement allowed per iteration during layout calculation.
	 * This parameter helps prevent vertices from moving too far in a single step,
	 * ensuring stable convergence of the layout algorithm.
	 *
	 * @return the maximum iteration movement
	 */
	public Number getMaxIterationMovement() {
		return maxIterationMovement;
	}

	/**
	 * Gets the temperature curve multiplier used in simulated annealing layout
	 * algorithms.
	 * This parameter controls how quickly the "temperature" decreases during the
	 * layout process,
	 * affecting the balance between exploration and exploitation in the algorithm.
	 *
	 * @return the temperature curve multiplier
	 */
	public Number getTemperatureCurveMultiplier() {
		return temperatureCurveMultiplier;
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
}
