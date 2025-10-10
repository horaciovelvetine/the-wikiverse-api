package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LayoutSettings {
    private Number attractionMultiplier;

    private Number repulsionMultiplier;

    private final Number[] forceAdjustmentMultipliers;

    private final Number vertexDensity;

    private final Number maxLayoutIterations;

    private final Number maxIterationMovement;

    private final Number temperatureCurveMultiplier;

    private final boolean prefers3D;

    public LayoutSettings() {
        this.attractionMultiplier = 0.5;
        this.repulsionMultiplier = 0.5;
        this.forceAdjustmentMultipliers = new Number[] { 0.9, 1.1 };
        this.vertexDensity = 0.5;
        this.maxIterationMovement = 30;
        this.maxLayoutIterations = 250;
        this.temperatureCurveMultiplier = 30;
        this.prefers3D = true;
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
     * Sets the attraction multiplier for the layout algorithm.
     *
     * @param attractionMultiplier the attraction multiplier value to set
     */
    public void setAttractionMultiplier(Number attractionMultiplier) {
        this.attractionMultiplier = attractionMultiplier;
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
     * Sets the repulsion multiplier for the layout algorithm.
     *
     * @param repulsionMultiplier the repulsion multiplier value to set
     */
    public void setRepulsionMultiplier(Number repulsionMultiplier) {
        this.repulsionMultiplier = repulsionMultiplier;
    }

    /**
     * Gets the force adjustment multipliers used in the layout algorithm.
     *
     * @return the array of force adjustment multipliers
     */
    public Number[] getForceAdjustmentMultipliers() {
        return forceAdjustmentMultipliers;
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
