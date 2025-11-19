package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LayoutSettings {

	private Number attractionMultiplier;

	private Number repulsionMultiplier;

	private final Number vertexDensity;

	private final Number maxLayoutIterations;

	private final Number maxIterationMovement;

	private final Number temperatureCurveMultiplier;

	private final boolean prefers3D;

	public LayoutSettings(String prefers3D) {
		this.attractionMultiplier = 0.5;
		this.repulsionMultiplier = 0.5;
		this.vertexDensity = 0.5;
		this.maxIterationMovement = 30;
		this.maxLayoutIterations = 250;
		this.temperatureCurveMultiplier = 30;
		this.prefers3D = "True".equals(prefers3D);
	}

	/**
	 * Constructs a LayoutSettings object from JSON deserialization.
	 * This constructor is used by Jackson to deserialize LayoutSettings from JSON.
	 *
	 * @param prefers3D                  whether the layout prefers 3D visualization
	 * @param attractionMultiplier       the attraction multiplier value
	 * @param repulsionMultiplier        the repulsion multiplier value
	 * @param forceAdjustmentMultipliers the array of force adjustment multipliers
	 * @param vertexDensity              the vertex density value
	 * @param maxLayoutIterations        the maximum number of layout iterations
	 * @param maxIterationMovement       the maximum movement allowed per iteration
	 * @param temperatureCurveMultiplier the temperature curve multiplier value
	 */
	@JsonCreator
	public LayoutSettings(
		@JsonProperty("prefers3D") boolean prefers3D,
		@JsonProperty("attractionMultiplier") Number attractionMultiplier,
		@JsonProperty("repulsionMultiplier") Number repulsionMultiplier,
		@JsonProperty("vertexDensity") Number vertexDensity,
		@JsonProperty("maxLayoutIterations") Number maxLayoutIterations,
		@JsonProperty("maxIterationMovement") Number maxIterationMovement,
		@JsonProperty("temperatureCurveMultiplier") Number temperatureCurveMultiplier
	) {
		this.prefers3D = prefers3D;
		this.attractionMultiplier = attractionMultiplier;
		this.repulsionMultiplier = repulsionMultiplier;
		this.vertexDensity = vertexDensity;
		this.maxLayoutIterations = maxLayoutIterations;
		this.maxIterationMovement = maxIterationMovement;
		this.temperatureCurveMultiplier = temperatureCurveMultiplier;
	}

	/**
	 * Gets the attraction multiplier used in the layout algorithm.
	 *
	 * @return the attraction multiplier value
	 */
	public Number getAttractionMultiplier() {
		return attractionMultiplier;
	}

	/**
	 * Gets the repulsion multiplier used in the layout algorithm.
	 *
	 * @return the repulsion multiplier value
	 */
	public Number getRepulsionMultiplier() {
		return repulsionMultiplier;
	}

	/**
	 * Gets the vertex density used in the layout algorithm.
	 *
	 * @return the vertex density value
	 */
	public Number getVertexDensity() {
		return vertexDensity;
	}

	/**
	 * Gets the maximum number of layout iterations.
	 *
	 * @return the maximum layout iterations value
	 */
	public Number getMaxLayoutIterations() {
		return maxLayoutIterations;
	}

	/**
	 * Gets the maximum movement allowed per iteration in the layout algorithm.
	 *
	 * @return the maximum iteration movement value
	 */
	public Number getMaxIterationMovement() {
		return maxIterationMovement;
	}

	/**
	 * Gets the temperature curve multiplier used in the layout algorithm.
	 *
	 * @return the temperature curve multiplier value
	 */
	public Number getTemperatureCurveMultiplier() {
		return temperatureCurveMultiplier;
	}

	/**
	 * Gets whether the layout prefers 3D visualization.
	 *
	 * @return true if 3D is preferred, false otherwise
	 */
	public boolean isPrefers3D() {
		return prefers3D;
	}
}
