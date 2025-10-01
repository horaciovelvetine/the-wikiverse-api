package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Function;
import java.awt.Dimension;
import java.util.Date;
import java.util.Random;

/**
 * Generates random 3D coordinates for vertices within a specified dimensional space.
 * This class implements the Guava Function interface to provide a mapping from any input
 * type to a Point3D object with randomly generated coordinates.
 *
 * <p>The RandomPoint3D class is designed to position vertices in a 3D space where:
 * <ul>
 *   <li>X and Y coordinates are bounded by the provided width and height dimensions</li>
 *   <li>Z coordinates are bounded by the maximum of width and height (application convention)</li>
 *   <li>All coordinates are centered around the origin (0,0,0) with symmetric ranges</li>
 *   <li>Random generation uses the current system time as seed for reproducibility</li>
 * </ul>
 *
 * <p>This class is particularly useful for:
 * <ul>
 *   <li>Initial positioning of vertices in a 3D graph visualization</li>
 *   <li>Generating random layouts for graph algorithms</li>
 *   <li>Creating test data with controlled spatial distribution</li>
 * </ul>
 *
 * <p>The random number generator is seeded with the current time to ensure different
 * coordinate sets are generated across different instances, while maintaining
 * deterministic behavior within a single instance.
 *
 * @param <V> the type of input that will be mapped to Point3D coordinates
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Point3D
 * @see Function
 * @see Dimension
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RandomPoint3D<V> implements Function<V, Point3D> {

	/** The random number generator used for coordinate generation. */
	private Random random;

	/** The dimensional bounds for the 3D space. */
	private Dimension dimensions;

	/**
	 * Constructs a new RandomPoint3D generator with the specified dimensional bounds.
	 * The random number generator is initialized with the current system time as seed.
	 *
	 * @param dim the dimensional bounds for the 3D space, cannot be null
	 * @throws IllegalArgumentException if dim is null
	 */
	public RandomPoint3D(Dimension dim) {
		if (dim == null) {
			throw new IllegalArgumentException("Dimension cannot be null");
		}
		this.dimensions = dim;
		this.random = new Random(new Date().getTime());
	}

	/**
	 * Generates random 3D coordinates for the given input within the specified space.
	 *
	 * <p>The generated coordinates follow these rules:
	 * <ul>
	 *   <li>X coordinate: random value in range [-width, +width]</li>
	 *   <li>Y coordinate: random value in range [-height, +height]</li>
	 *   <li>Z coordinate: random value in range [-max(width,height), +max(width,height)]</li>
	 * </ul>
	 *
	 * <p>This method implements the Function interface, allowing it to be used
	 * with Guava's functional programming utilities and collections.
	 *
	 * @param input the input object to map to coordinates (unused in generation)
	 * @return a new Point3D object with randomly generated coordinates
	 */
	@Override
	public Point3D apply(V input) {
		double max = Math.max(dimensions.width, dimensions.height);
		return new Point3D(
			(random.nextDouble() - 0.5) * 2 * dimensions.width,
			(random.nextDouble() - 0.5) * 2 * dimensions.height,
			(random.nextDouble() - 0.5) * 2 * max
		);
	}
}
