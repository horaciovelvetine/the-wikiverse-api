package edu.velvet.Wikiverse.api.models.core;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the RandomPoint3D class.
 * Tests all public methods including constructor validation, coordinate generation,
 * boundary conditions, and statistical properties of random generation.
 *
 * @author The Wikiverse Team
 * @version 1.0
 * @since 1.0
 */
@DisplayName("RandomPoint3D Tests")
class RandomPoint3DTest {

	private RandomPoint3D<String> randomPoint3D;
	private Dimension testDimensions;

	@BeforeEach
	void setUp() {
		testDimensions = new Dimension(100, 200);
		randomPoint3D = new RandomPoint3D<>(testDimensions);
	}

	// ========== Constructor Tests ==========

	@Test
	void testConstructorWithValidDimensions() {
		Dimension dim = new Dimension(50, 75);
		RandomPoint3D<String> generator = new RandomPoint3D<>(dim);
		assertNotNull(generator, "RandomPoint3D should be created successfully");
	}

	@Test
	void testConstructorWithNullDimension() {
		assertThrows(
			IllegalArgumentException.class,
			() -> {
				new RandomPoint3D<>(null);
			},
			"Constructor should throw IllegalArgumentException for null dimension"
		);
	}

	@Test
	void testConstructorWithZeroDimensions() {
		Dimension zeroDim = new Dimension(0, 0);
		RandomPoint3D<String> generator = new RandomPoint3D<>(zeroDim);
		assertNotNull(generator, "RandomPoint3D should handle zero dimensions");
	}

	@Test
	void testConstructorWithNegativeDimensions() {
		Dimension negativeDim = new Dimension(-10, -20);
		RandomPoint3D<String> generator = new RandomPoint3D<>(negativeDim);
		assertNotNull(generator, "RandomPoint3D should handle negative dimensions");
	}

	// ========== Apply Method Tests ==========

	@Test
	void testApplyReturnsNonNullPoint3D() {
		Point3D result = randomPoint3D.apply("test");
		assertNotNull(result, "apply() should return a non-null Point3D");
	}

	@Test
	void testApplyWithNullInput() {
		Point3D result = randomPoint3D.apply(null);
		assertNotNull(result, "apply() should handle null input gracefully");
	}

	@Test
	void testApplyWithDifferentInputTypes() {
		RandomPoint3D<Integer> intGenerator = new RandomPoint3D<>(testDimensions);
		RandomPoint3D<Double> doubleGenerator = new RandomPoint3D<>(testDimensions);

		Point3D intResult = intGenerator.apply(42);
		Point3D doubleResult = doubleGenerator.apply(3.14);

		assertNotNull(intResult, "Should work with Integer input");
		assertNotNull(doubleResult, "Should work with Double input");
	}

	// ========== Coordinate Boundary Tests ==========

	@Test
	void testXCoordinateWithinBounds() {
		for (int i = 0; i < 1000; i++) {
			Point3D point = randomPoint3D.apply("test");
			double expectedMax = testDimensions.width;
			assertTrue(
				point.getX() >= -expectedMax && point.getX() <= expectedMax,
				"X coordinate should be within bounds [-" + expectedMax + ", " + expectedMax + "]"
			);
		}
	}

	@Test
	void testYCoordinateWithinBounds() {
		for (int i = 0; i < 1000; i++) {
			Point3D point = randomPoint3D.apply("test");
			double expectedMax = testDimensions.height;
			assertTrue(
				point.getY() >= -expectedMax && point.getY() <= expectedMax,
				"Y coordinate should be within bounds [-" + expectedMax + ", " + expectedMax + "]"
			);
		}
	}

	@Test
	void testZCoordinateWithinBounds() {
		for (int i = 0; i < 1000; i++) {
			Point3D point = randomPoint3D.apply("test");
			double expectedMax = Math.max(testDimensions.width, testDimensions.height);
			assertTrue(
				point.getZ() >= -expectedMax && point.getZ() <= expectedMax,
				"Z coordinate should be within bounds [-" + expectedMax + ", " + expectedMax + "]"
			);
		}
	}

	@Test
	void testZCoordinateUsesMaxDimension() {
		// Test with width > height
		Dimension wideDim = new Dimension(200, 100);
		RandomPoint3D<String> wideGenerator = new RandomPoint3D<>(wideDim);

		for (int i = 0; i < 1000; i++) {
			Point3D point = wideGenerator.apply("test");
			double expectedMax = 200; // max(200, 100) = 200
			assertTrue(
				point.getZ() >= -expectedMax && point.getZ() <= expectedMax,
				"Z coordinate should use max dimension when width > height"
			);
		}
	}

	@Test
	void testZCoordinateUsesMaxDimensionWhenHeightGreater() {
		// Test with height > width
		Dimension tallDim = new Dimension(100, 300);
		RandomPoint3D<String> tallGenerator = new RandomPoint3D<>(tallDim);

		for (int i = 0; i < 1000; i++) {
			Point3D point = tallGenerator.apply("test");
			double expectedMax = 300; // max(100, 300) = 300
			assertTrue(
				point.getZ() >= -expectedMax && point.getZ() <= expectedMax,
				"Z coordinate should use max dimension when height > width"
			);
		}
	}

	// ========== Randomness Tests ==========

	@Test
	void testMultipleCallsGenerateDifferentPoints() {
		Set<Point3D> points = new HashSet<>();

		// Generate 1000 points and check they're mostly different
		for (int i = 0; i < 1000; i++) {
			Point3D point = randomPoint3D.apply("test");
			points.add(point);
		}

		// With 1000 random points in a reasonable space, we should have many unique points
		assertTrue(
			points.size() > 500,
			"Multiple calls should generate mostly different points. Got " +
			points.size() +
			" unique points out of 1000"
		);
	}

	@Test
	void testSameInstanceGeneratesDifferentPoints() {
		Point3D point1 = randomPoint3D.apply("test1");
		Point3D point2 = randomPoint3D.apply("test2");

		// While it's theoretically possible for two random points to be identical,
		// the probability is extremely low with double precision
		boolean coordinatesDifferent =
			point1.getX() != point2.getX() || point1.getY() != point2.getY() || point1.getZ() != point2.getZ();

		assertTrue(coordinatesDifferent, "Same instance should generate different points on subsequent calls");
	}

	// ========== Statistical Distribution Tests ==========

	@Test
	void testCoordinateDistributionIsCentered() {
		double sumX = 0,
			sumY = 0,
			sumZ = 0;
		int sampleSize = 10000;

		for (int i = 0; i < sampleSize; i++) {
			Point3D point = randomPoint3D.apply("test");
			sumX += point.getX();
			sumY += point.getY();
			sumZ += point.getZ();
		}

		double avgX = sumX / sampleSize;
		double avgY = sumY / sampleSize;
		double avgZ = sumZ / sampleSize;

		// Averages should be close to 0 (centered distribution)
		assertTrue(Math.abs(avgX) < 5.0, "X coordinate average should be close to 0, got: " + avgX);
		assertTrue(Math.abs(avgY) < 5.0, "Y coordinate average should be close to 0, got: " + avgY);
		assertTrue(Math.abs(avgZ) < 5.0, "Z coordinate average should be close to 0, got: " + avgZ);
	}

	@Test
	void testCoordinateRangesAreSymmetric() {
		double minX = Double.MAX_VALUE,
			maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE,
			maxY = Double.MIN_VALUE;
		double minZ = Double.MAX_VALUE,
			maxZ = Double.MIN_VALUE;

		int sampleSize = 10000;
		for (int i = 0; i < sampleSize; i++) {
			Point3D point = randomPoint3D.apply("test");
			minX = Math.min(minX, point.getX());
			maxX = Math.max(maxX, point.getX());
			minY = Math.min(minY, point.getY());
			maxY = Math.max(maxY, point.getY());
			minZ = Math.min(minZ, point.getZ());
			maxZ = Math.max(maxZ, point.getZ());
		}

		// Ranges should be approximately symmetric around 0
		assertTrue(Math.abs(minX + maxX) < 10.0, "X range should be symmetric around 0");
		assertTrue(Math.abs(minY + maxY) < 10.0, "Y range should be symmetric around 0");
		assertTrue(Math.abs(minZ + maxZ) < 10.0, "Z range should be symmetric around 0");
	}

	// ========== Edge Case Tests ==========

	@Test
	void testWithSquareDimensions() {
		Dimension squareDim = new Dimension(100, 100);
		RandomPoint3D<String> squareGenerator = new RandomPoint3D<>(squareDim);

		for (int i = 0; i < 1000; i++) {
			Point3D point = squareGenerator.apply("test");
			double expectedMax = 100; // max(100, 100) = 100
			assertTrue(
				point.getZ() >= -expectedMax && point.getZ() <= expectedMax,
				"Z coordinate should use max dimension for square dimensions"
			);
		}
	}

	@Test
	void testWithVerySmallDimensions() {
		Dimension smallDim = new Dimension(1, 1);
		RandomPoint3D<String> smallGenerator = new RandomPoint3D<>(smallDim);

		for (int i = 0; i < 1000; i++) {
			Point3D point = smallGenerator.apply("test");
			assertTrue(point.getX() >= -1.0 && point.getX() <= 1.0, "X should be in [-1, 1] range");
			assertTrue(point.getY() >= -1.0 && point.getY() <= 1.0, "Y should be in [-1, 1] range");
			assertTrue(point.getZ() >= -1.0 && point.getZ() <= 1.0, "Z should be in [-1, 1] range");
		}
	}

	@Test
	void testWithVeryLargeDimensions() {
		Dimension largeDim = new Dimension(10000, 20000);
		RandomPoint3D<String> largeGenerator = new RandomPoint3D<>(largeDim);

		for (int i = 0; i < 100; i++) {
			// Fewer iterations for large dimensions
			Point3D point = largeGenerator.apply("test");
			assertTrue(point.getX() >= -10000.0 && point.getX() <= 10000.0, "X should be in large range");
			assertTrue(point.getY() >= -20000.0 && point.getY() <= 20000.0, "Y should be in large range");
			assertTrue(point.getZ() >= -20000.0 && point.getZ() <= 20000.0, "Z should be in large range");
		}
	}

	// ========== Function Interface Tests ==========

	@Test
	void testImplementsFunctionInterface() {
		assertTrue(
			randomPoint3D instanceof com.google.common.base.Function,
			"RandomPoint3D should implement Function interface"
		);
	}

	@Test
	void testFunctionInterfaceCompatibility() {
		com.google.common.base.Function<String, Point3D> function = randomPoint3D;
		Point3D result = function.apply("test");
		assertNotNull(result, "Function interface should work correctly");
	}

	// ========== Thread Safety Tests ==========

	@Test
	void testConcurrentAccess() throws InterruptedException {
		final int numThreads = 10;
		final int pointsPerThread = 100;
		final Set<Point3D> allPoints = new HashSet<>();
		final Thread[] threads = new Thread[numThreads];

		for (int i = 0; i < numThreads; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0; j < pointsPerThread; j++) {
					Point3D point = randomPoint3D.apply("thread-" + Thread.currentThread().getName());
					synchronized (allPoints) {
						allPoints.add(point);
					}
				}
			});
		}

		// Start all threads
		for (Thread thread : threads) {
			thread.start();
		}

		// Wait for all threads to complete
		for (Thread thread : threads) {
			thread.join();
		}

		// Should have generated many unique points across threads
		assertTrue(
			allPoints.size() > numThreads * pointsPerThread * 0.8,
			"Concurrent access should generate mostly unique points"
		);
	}
}
