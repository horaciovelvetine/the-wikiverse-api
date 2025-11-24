package edu.velvet.Wikiverse.api.services.layout;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.velvet.Wikiverse.api.models.core.Edge;
import edu.velvet.Wikiverse.api.models.core.Graphset;
import edu.velvet.Wikiverse.api.models.core.LayoutSettings;
import edu.velvet.Wikiverse.api.models.core.Point3D;
import edu.velvet.Wikiverse.api.models.core.RandomPoint3D;
import edu.velvet.Wikiverse.api.models.core.Vertex;
import edu.velvet.Wikiverse.api.services.logging.ProcessLogger;
import io.vavr.Tuple2;
import java.awt.Dimension;
import javax.annotation.Nonnull;

public class FR3DLayout {

	private final ProcessLogger logger = new ProcessLogger("FR3DLayout.log");

	private final double EPSILON = 0.000001D; // Prevent 0/div errors
	private int iterationCount = 0;
	private int maxLayoutIterations = 300;

	private double temperature;
	private final double vertexDensity;
	private final double repulsionForce;
	private final double attractionForce;
	private final int temperatureCurveMultiplier;
	private final int lockedVertexForceScaler = 2;

	private final Dimension dimensions;

	/**
	 * Stores the accumulated offset used to find the new position on each iterative
	 * step through the layout process
	 */
	LoadingCache<String, Point3D> offsets;

	/**
	 * Stores the position of each Vertex (by QID) in the layout
	 */
	LoadingCache<Vertex, Point3D> locations;

	public FR3DLayout(Graphset graph, LayoutSettings layoutSettings) {
		// Only update to maxIterations restriction less than 300...
		if (layoutSettings.getMaxLayoutIterations().intValue() < 300) {
			this.maxLayoutIterations = layoutSettings.getMaxLayoutIterations().intValue();
		}
		this.temperatureCurveMultiplier = layoutSettings.getTemperatureCurveMultiplier().intValue();
		this.vertexDensity = layoutSettings.getVertexDensity().doubleValue();

		// Initialize computed values (or values which rely on calc/computation)
		int vertexCount = graph.getVertexCount();
		this.dimensions = calculateLayoutDimensions(vertexCount);
		this.offsets = buildOffsetCache();
		this.locations = buildLocationCache();

		// PHYSICAL FORCE THINGS
		// Initialize temperature based on dimension size, ensuring it's large enough
		// to allow meaningful movement in early iterations
		this.temperature = Math.max(dimensions.getHeight() / this.temperatureCurveMultiplier, 10.0);
		double forceConstant = Math.sqrt((dimensions.getHeight() * dimensions.getWidth()) / vertexCount);
		this.attractionForce = forceConstant * layoutSettings.getAttractionMultiplier().doubleValue();
		this.repulsionForce = forceConstant * layoutSettings.getRepulsionMultiplier().doubleValue();
	}

	/**
	 * Executes the full 3D force-directed layout for the provided graph.
	 * <p>
	 * This method repeatedly applies one layout step by calling
	 * {@link #stepLayout(Graphset)} until either the layout converges or the
	 * maximum
	 * number of iterations is reached, as defined by {@link #layoutCompleted()}.
	 * </p>
	 * <p>
	 * After completing the layout, all vertex positions in the given
	 * {@link Graphset}
	 * are updated to their final computed locations.
	 * </p>
	 *
	 * @param graph the {@link Graphset} to arrange; must be non-null and populated
	 *              with vertices
	 */
	public void runLayout(Graphset graph) {
		while (!layoutCompleted()) {
			stepLayout(graph);
		}

		graph
			.getVertices()
			.forEach((Vertex v) -> {
				Point3D newPosition = locations.getIfPresent(v);
				if (newPosition != null) {
					v.getPosition().setLocation(locations.getIfPresent(v));
				}
			});
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FR3DLayout{");
		sb.append("iterationCount=").append(iterationCount).append(", ");
		sb.append("maxLayoutIterations=").append(maxLayoutIterations).append(", ");
		sb.append("temperature=").append(temperature).append(", ");
		sb.append("vertexDensity=").append(vertexDensity).append(", ");
		sb.append("repulsionForce=").append(repulsionForce).append(", ");
		sb.append("attractionForce=").append(attractionForce).append(", ");
		sb.append("temperatureCurveMultiplier=").append(temperatureCurveMultiplier).append(", ");
		sb.append("lockedVertexForceScaler=").append(lockedVertexForceScaler).append(", ");
		sb.append("dimensions=").append(dimensions).append(", ");
		sb.append("offsets size=").append(offsets != null ? offsets.size() : "null").append(", ");
		sb.append("locations size=").append(locations != null ? locations.size() : "null");
		sb.append("}");
		return sb.toString();
	}

	// !PRIVATE ===================================================================>
	// !PRIVATE ===================================================================>

	/**
	 * Calculates the layout dimensions for the current graph, based on the number
	 * of vertices
	 * and the configured vertex density.
	 *
	 * <p>
	 * The method estimates the target area by dividing the number of vertices (as
	 * determined by
	 * the size of {@code locationData}) by the {@code vertexDensity} parameter. It
	 * then computes
	 * a square dimension where the side length is the integer square root of the
	 * target area.
	 * The resulting {@link Dimension} has both width and height set to this side
	 * length.
	 * </p>
	 * <p>
	 * A minimum dimension size of 100x100 is enforced to ensure the layout has
	 * sufficient space
	 * for force-directed calculations and to prevent premature convergence due to
	 * overly small
	 * temperature thresholds.
	 * </p>
	 *
	 * @return a {@link Dimension} object representing the calculated width and
	 *         height for
	 *         the layout space.
	 */
	private Dimension calculateLayoutDimensions(int vertexCount) {
		// Use a default size in cases which would otherwise cause bad calculations
		if (vertexCount <= 1) {
			return new Dimension(300, 300);
		}

		double targetArea = Math.pow(vertexCount, 3) / this.vertexDensity;
		int sideLength = ((int) Math.sqrt(targetArea));

		return new Dimension(sideLength, sideLength);
	}

	/**
	 * Updates the offset of the given vertex by applying the specified deltas in
	 * each dimension,
	 * scaled by the provided factor.
	 *
	 * <p>
	 * This method modifies the current offset position of the vertex in
	 * three-dimensional space.
	 * The deltas {@code dx}, {@code dy}, and {@code dz} are each multiplied by the
	 * {@code scale}
	 * factor, and the results are added to the current X, Y, and Z coordinates of
	 * the vertex's offset,
	 * respectively. The updated offset is set in-place.
	 * </p>
	 *
	 * @param v     the vertex whose offset is to be updated
	 * @param dx    the change in the X coordinate (before scaling)
	 * @param dy    the change in the Y coordinate (before scaling)
	 * @param dz    the change in the Z coordinate (before scaling)
	 * @param scale the factor by which to scale the deltas before applying
	 */
	private void updateOffset(Vertex v, double dx, double dy, double dz, double scale) {
		if (!v.isLocked()) {
			// Only update the offset if the provided Vertex is not locked...
			Point3D currentOffset = offsets.getUnchecked(v.getId());
			double nX = currentOffset.getX() + (scale * dx);
			double nY = currentOffset.getY() + (scale * dy);
			double nZ = currentOffset.getZ() + (scale * dz);

			currentOffset.setLocation(nX, nY, nZ);
		}
	}

	/**
	 * Clamps a position value to remain within the allowed boundaries of the layout
	 * dimensions.
	 *
	 * <p>
	 * This method ensures that a coordinate position {@code nP} does not exceed the
	 * limits defined
	 * by the layout's dimensions. The allowable range for each coordinate is
	 * symmetric around zero
	 * and restricted to {@code [-maximum, +maximum]}, where {@code maximum} is
	 * obtained from the
	 * square layout's height (which is also its width and depth).
	 * </p>
	 *
	 * @param nP the proposed new coordinate value for a layout axis (X, Y, or Z)
	 * @return the clamped coordinate, guaranteed to remain within the layout's
	 *         allowed boundaries
	 */
	private double clampPositionToDimensions(double nP) {
		double maximum = dimensions.getHeight(); // Dimensions are square, height == width == depth
		return Math.max(-maximum, Math.min(maximum, nP));
	}

	/**
	 * Initializes the offset cache for vertices in the layout.
	 * <p>
	 * This method constructs a Guava Cache that maps vertex IDs (as Strings) to
	 * their current
	 * offset values (as {@link Point3D} objects). The cache loader returns a new
	 * {@code Point3D}
	 * object for each previously unseen vertex ID. This structure is used
	 * throughout the layout
	 * algorithm to efficiently retrieve and update each vertex's offset from its
	 * initial position,
	 * enabling incremental position adjustments during force-directed iterations.
	 * </p>
	 *
	 * @return
	 */
	private LoadingCache<String, Point3D> buildOffsetCache() {
		return CacheBuilder.newBuilder().build(
				new CacheLoader<String, Point3D>() {
					@Override
					public Point3D load(@Nonnull String QID) {
						return new Point3D();
					}
				}
			);
	}

	/**
	 * Initializes the location cache for all vertices in the layout.
	 * <p>
	 * This method constructs a Guava LoadingCache that maps each {@link Vertex} to
	 * its current
	 * {@link Point3D} position in the layout. Positions are generated as follows:
	 * <ul>
	 * <li>If a vertex is locked and has a predefined position, that position is
	 * used.</li>
	 * <li>Otherwise, a new random position is generated for the vertex within the
	 * layout's
	 * dimensions using {@link RandomPoint3D}.</li>
	 * </ul>
	 * The returned positions are defensive clones to ensure immutability for cache
	 * retrievals.
	 * <p>
	 * This cache structure is essential to efficiently initialize, access, and
	 * reuse positions
	 * for all vertices across layout steps, supporting both deterministic and
	 * randomly seeded
	 * initial configurations.
	 * </p>
	 *
	 * @return
	 */
	private LoadingCache<Vertex, Point3D> buildLocationCache() {
		Function<Vertex, Point3D> pointRandomizer = new RandomPoint3D<>(dimensions);
		Function<Vertex, Point3D> chain = Functions.<Vertex, Point3D, Point3D>compose(
			(Point3D input) -> (Point3D) input.clone(),
			(Vertex v) -> {
				if (v.isLocked() && v.getPosition() != null) {
					return v.getPosition();
				} else {
					return pointRandomizer.apply(v);
				}
			}
		);
		return CacheBuilder.newBuilder().build(CacheLoader.from(chain));
	}

	/**
	 * Determines whether the layout algorithm has completed its execution.
	 * <p>
	 * The layout is considered complete if the number of iterations exceeds the
	 * maximum allowed
	 * ({@code maxLayoutIterations}), or if the current temperature has cooled down
	 * below
	 * a minimal threshold. The threshold is set to a small fixed value (0.01) to
	 * ensure
	 * the layout runs for a reasonable number of iterations regardless of dimension
	 * size.
	 * </p>
	 *
	 * @return {@code true} if the layout is finished, {@code false} otherwise.
	 */
	private boolean layoutCompleted() {
		// Use a fixed small threshold instead of dimension-based threshold
		// to prevent premature convergence with small dimensions
		final double MIN_TEMPERATURE_THRESHOLD = 0.01;
		return iterationCount > maxLayoutIterations || temperature <= MIN_TEMPERATURE_THRESHOLD;
	}

	/**
	 * Performs a single iteration (step) of the 3D Fruchterman-Reingold layout
	 * algorithm.
	 * <p>
	 * This method updates the layout in the following sequence:
	 * <ol>
	 * <li><b>Repulsion Calculation:</b> For each vertex in the graph that is not
	 * locked and has been fetched,
	 * computes the repulsive force offsets from all other vertices.</li>
	 * <li><b>Attraction Calculation:</b> For each edge in the graph, computes the
	 * attractive force offsets
	 * between connected vertices.</li>
	 * <li><b>Position Update:</b> For each vertex that is not locked and has been
	 * fetched, updates its
	 * accumulated position offset according to the previously calculated forces and
	 * current temperature.</li>
	 * <li><b>Temperature Cooling:</b> Updates the layout temperature to gradually
	 * reduce movement over iterations.</li>
	 * </ol>
	 * Steps are executed in retries to ensure resilient processing in the event of
	 * transient exceptions;
	 * exceptions during each phase are caught and re-tried until success within
	 * their respective loops.
	 * <p>
	 * The {@code iterationCount} is incremented at the start of each step.
	 *
	 * @param graph the graph whose vertices and edges are to be laid out in the
	 *              current iteration
	 */
	private void stepLayout(Graphset graph) {
		iterationCount++;

		// Repulsion Calc's
		while (true) {
			try {
				for (Vertex v : graph.getVertices()) {
					if (v.isLocked() || !v.isFetched()) {
						// Vertex is either locked or unfetched, and shouldn't be included
						continue;
					}
					calculateRepulsionOffsets(v, graph);
				}
				break;
			} catch (Exception e) {
				// Ignore and continue...
			}
		}

		// Attraction Calc's
		while (true) {
			try {
				for (Edge e : graph.getEdges()) {
					calculateAttractionOffsets(e, graph);
				}
				break;
			} catch (Exception e) {
				// Ignore and continue...
			}
		}

		// Position Updates
		while (true) {
			try {
				for (Vertex v : graph.getVertices()) {
					if (v.isLocked() || !v.isFetched()) {
						// Vertex is locked or unfetched, shouldn't be updated...
						continue;
					}
					accumulatePositionOffsets(v);
				}
				break;
			} catch (Exception e) {
				// Ignore and continue
			}
		}
		updateLayoutTemperature();
		logger.logInfo(this.toString());
	}

	/**
	 * Updates the layout's temperature based on the current progress of the
	 * algorithm.
	 * <p>
	 * This cooling schedule simulates the reduction of vertex movement as the
	 * force-directed
	 * layout runs. The temperature is decayed using a curve controlled by
	 * {@code temperatureCurveMultiplier}
	 * and the ratio of {@code iterationCount} to {@code maxLayoutIterations}. The
	 * decay factor uses
	 * a modified logarithmic function to provide a smooth, adjustable reduction in
	 * temperature over time.
	 * </p>
	 * <p>
	 * As a result, vertex movements become gradually limited, guiding the system
	 * toward convergence
	 * as the temperature approaches zero or the maximum number of iterations is
	 * reached.
	 * </p>
	 */
	private void updateLayoutTemperature() {
		double progress = (double) iterationCount / (double) maxLayoutIterations;
		// The higher the denominator, the slower the temperature cools
		double decay = Math.pow(0.98, progress * temperatureCurveMultiplier);
		// Ensure temperature always decays, never increases or goes negative
		if (decay < 0.0) {
			decay = 0.0;
		}
		temperature *= decay;

		// Prevent temperature from dropping too far below a useful threshold,
		// so there's always a little movement possible
		final double minTemperature = 0.01;
		if (temperature < minTemperature) {
			temperature = minTemperature;
		}
	}

	// *===========================================================================>
	// ! FORCE CALCULATIONS...
	// *===========================================================================>
	// *===========================================================================>

	private void calculateRepulsionOffsets(Vertex v1, Graphset graph) {
		Point3D offset = offsets.getUnchecked(v1.getId());
		offset.reset(); // Reset offset to 0 at top of calculations...

		try {
			for (Vertex v2 : graph.getVertices()) {
				if (v1.equals(v2)) {
					// Do not evaluate against self...
					continue;
				}

				Point3D p1 = locations.getUnchecked(v1);
				Point3D p2 = locations.getUnchecked(v2);

				double deltaDistanceSq = Math.max(EPSILON, p1.distanceSq(p2));
				double deltaDistance = Math.sqrt(deltaDistanceSq);

				double force = (repulsionForce * repulsionForce) / deltaDistanceSq;

				if (Double.isNaN(force)) {
					throw new RuntimeException("calculateRepulsionOffsets() found NaN value for: force");
				}

				// Calculate normalized direction vector from v2 to v1 (repulsion pushes v1 away
				// from v2)
				double xDisplacement = ((p1.getX() - p2.getX()) / deltaDistance) * force;
				double yDisplacement = ((p1.getY() - p2.getY()) / deltaDistance) * force;
				double zDisplacement = ((p1.getZ() - p2.getZ()) / deltaDistance) * force;

				// If other Vertex is locked push harder...
				int updateScaler = v2.isLocked() ? lockedVertexForceScaler : 1;
				updateOffset(v1, xDisplacement, yDisplacement, zDisplacement, updateScaler);
			}
		} catch (Exception e) {
			logger.logError("calculateRepulsionOffsets()", e);
			// Redo Calculation ?
			// calculateRepulsionOffsets(v1, graph);
		}
	}

	// *===========================================================================>
	// *===========================================================================>
	private void calculateAttractionOffsets(Edge e, Graphset graph) {
		Tuple2<Vertex, Vertex> endpoints = graph.getEndpoints(e);
		Vertex sV = endpoints._1();
		Vertex tV = endpoints._2();

		// Skip if either endpoint is null...
		if (sV == null || tV == null) {
			return;
		}

		try {
			Point3D p1 = locations.getUnchecked(sV);
			Point3D p2 = locations.getUnchecked(tV);

			double deltaDistanceSq = Math.max(EPSILON, p1.distanceSq(p2));
			double deltaDistance = Math.sqrt(deltaDistanceSq);
			double force = (deltaDistanceSq) / attractionForce;

			if (Double.isNaN(force)) {
				throw new RuntimeException("calculateAttractionOffsets() found NaN value for: force");
			}

			// Calculate normalized direction vector from p1 to p2 (attraction pulls
			// vertices together)
			double xDirection = (p2.getX() - p1.getX()) / deltaDistance;
			double yDirection = (p2.getY() - p1.getY()) / deltaDistance;
			double zDirection = (p2.getZ() - p1.getZ()) / deltaDistance;

			double xDisplacement = xDirection * force;
			double yDisplacement = yDirection * force;
			double zDisplacement = zDirection * force;

			// If other Vertex is locked, pull harder on the moving vertex...
			int sourceScaler = tV.isLocked() ? lockedVertexForceScaler : 1;
			int targetScaler = sV.isLocked() ? lockedVertexForceScaler : 1;

			// Apply attraction: source moves toward target, target moves toward source
			updateOffset(sV, xDisplacement, yDisplacement, zDisplacement, sourceScaler);
			updateOffset(tV, -xDisplacement, -yDisplacement, -zDisplacement, targetScaler);
		} catch (Exception exception) {
			logger.logError("calculateAttractionOffsets()", exception);
		}
	}

	// *===========================================================================>
	// *===========================================================================>

	private void accumulatePositionOffsets(Vertex v) {
		Point3D offset = offsets.getUnchecked(v.getId());
		Point3D location = locations.getUnchecked(v);

		try {
			double magnitude = Math.max(
				EPSILON,
				offset.getX() * offset.getX() + offset.getY() * offset.getY() + offset.getZ() * offset.getZ()
			);

			// Calculate the scaled offset for each axis: normalize the offset vector and
			// multiply
			// it by the lesser of sqrt(magnitude) and the current temperature (limits
			// per-iteration movement)
			double xOffset = (offset.getX() / magnitude) * Math.min(Math.sqrt(magnitude), temperature);
			double yOffset = (offset.getY() / magnitude) * Math.min(Math.sqrt(magnitude), temperature);
			double zOffset = (offset.getZ() / magnitude) * Math.min(Math.sqrt(magnitude), temperature);

			// Check for NaN in offsets
			if (Double.isNaN(xOffset) || Double.isNaN(yOffset) || Double.isNaN(zOffset)) {
				throw new RuntimeException("accumulatePositionOffsets() found NaN value for: xOffset/yOffset/zOffset");
			}

			// Update locations with clamps to dimensions...
			double newX = clampPositionToDimensions(location.getX() + xOffset);
			double newY = clampPositionToDimensions(location.getY() + yOffset);
			double newZ = clampPositionToDimensions(location.getZ() + zOffset);

			location.setLocation(newX, newY, newZ);
		} catch (Exception e) {
			logger.logError("accumulatePositionOffsets()", e);
		}
	}

	// End FR3DLayout...
}
