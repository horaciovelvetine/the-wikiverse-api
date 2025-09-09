package edu.velvet.Wikiverse.api.models.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the Point3D class.
 * Tests all constructors, methods, and edge cases.
 */
class Point3DTest {

  private Point3D point1;
  private Point3D point2;
  private Point3D point3;

  @BeforeEach
  void setUp() {
    point1 = new Point3D(1.0, 2.0, 3.0);
    point2 = new Point3D(4.0, 5.0, 6.0);
    point3 = new Point3D(0.0, 0.0, 0.0);
  }

  // ========== Constructor Tests ==========

  @Test
  void testDefaultConstructor() {
    Point3D point = new Point3D();
    assertEquals(0.0, point.getX(), "Default x should be 0.0");
    assertEquals(0.0, point.getY(), "Default y should be 0.0");
    assertEquals(0.0, point.getZ(), "Default z should be 0.0");
  }

  @Test
  void testParameterizedConstructor() {
    Point3D point = new Point3D(1.5, 2.5, 3.5);
    assertEquals(1.5, point.getX(), "X coordinate should be set correctly");
    assertEquals(2.5, point.getY(), "Y coordinate should be set correctly");
    assertEquals(3.5, point.getZ(), "Z coordinate should be set correctly");
  }

  @Test
  void testConstructorWithNegativeValues() {
    Point3D point = new Point3D(-1.0, -2.0, -3.0);
    assertEquals(-1.0, point.getX(), "Negative x should be set correctly");
    assertEquals(-2.0, point.getY(), "Negative y should be set correctly");
    assertEquals(-3.0, point.getZ(), "Negative z should be set correctly");
  }

  @Test
  void testConstructorWithZeroValues() {
    Point3D point = new Point3D(0.0, 0.0, 0.0);
    assertEquals(0.0, point.getX(), "Zero x should be set correctly");
    assertEquals(0.0, point.getY(), "Zero y should be set correctly");
    assertEquals(0.0, point.getZ(), "Zero z should be set correctly");
  }

  // ========== Getter/Setter Tests ==========

  @Test
  void testGetZ() {
    assertEquals(3.0, point1.getZ(), "getZ() should return correct z value");
    assertEquals(6.0, point2.getZ(), "getZ() should return correct z value");
    assertEquals(0.0, point3.getZ(), "getZ() should return correct z value");
  }

  @Test
  void testSetLocationWithCoordinates() {
    point1.setLocation(10.0, 20.0, 30.0);
    assertEquals(10.0, point1.getX(), "setLocation should set x correctly");
    assertEquals(20.0, point1.getY(), "setLocation should set y correctly");
    assertEquals(30.0, point1.getZ(), "setLocation should set z correctly");
  }

  @Test
  void testSetLocationWithPoint3D() {
    Point3D newPoint = new Point3D(7.0, 8.0, 9.0);
    point1.setLocation(newPoint);
    assertEquals(7.0, point1.getX(), "setLocation with Point3D should set x correctly");
    assertEquals(8.0, point1.getY(), "setLocation with Point3D should set y correctly");
    assertEquals(9.0, point1.getZ(), "setLocation with Point3D should set z correctly");
  }

  @Test
  void testSetLocationWithNegativeValues() {
    point1.setLocation(-5.0, -10.0, -15.0);
    assertEquals(-5.0, point1.getX(), "setLocation should handle negative x");
    assertEquals(-10.0, point1.getY(), "setLocation should handle negative y");
    assertEquals(-15.0, point1.getZ(), "setLocation should handle negative z");
  }

  // ========== Distance Calculation Tests ==========

  @Test
  void testStaticDistanceSq() {
    double expected = (4.0 - 1.0) * (4.0 - 1.0) + (5.0 - 2.0) * (5.0 - 2.0) + (6.0 - 3.0) * (6.0 - 3.0);
    double actual = Point3D.distanceSq(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
    assertEquals(expected, actual, "Static distanceSq should calculate correctly");
  }

  @Test
  void testStaticDistanceSqWithSamePoints() {
    double actual = Point3D.distanceSq(1.0, 2.0, 3.0, 1.0, 2.0, 3.0);
    assertEquals(0.0, actual, "Distance between same points should be 0");
  }

  @Test
  void testStaticDistanceSqWithNegativeCoordinates() {
    double expected = (-1.0 - 1.0) * (-1.0 - 1.0) + (-2.0 - 2.0) * (-2.0 - 2.0) + (-3.0 - 3.0) * (-3.0 - 3.0);
    double actual = Point3D.distanceSq(1.0, 2.0, 3.0, -1.0, -2.0, -3.0);
    assertEquals(expected, actual, "Static distanceSq should handle negative coordinates");
  }

  @Test
  void testInstanceDistanceSqWithCoordinates() {
    double expected = (4.0 - 1.0) * (4.0 - 1.0) + (5.0 - 2.0) * (5.0 - 2.0) + (6.0 - 3.0) * (6.0 - 3.0);
    double actual = point1.distanceSq(4.0, 5.0, 6.0);
    assertEquals(expected, actual, "Instance distanceSq should calculate correctly");
  }

  @Test
  void testInstanceDistanceSqWithPoint3D() {
    double expected = (4.0 - 1.0) * (4.0 - 1.0) + (5.0 - 2.0) * (5.0 - 2.0) + (6.0 - 3.0) * (6.0 - 3.0);
    double actual = point1.distanceSq(point2);
    assertEquals(expected, actual, "Instance distanceSq with Point3D should calculate correctly");
  }

  @Test
  void testInstanceDistanceSqWithSelf() {
    double actual = point1.distanceSq(point1);
    assertEquals(0.0, actual, "Distance to self should be 0");
  }

  @Test
  void testStaticDistance() {
    double expected = Math.sqrt((4.0 - 1.0) * (4.0 - 1.0) + (5.0 - 2.0) * (5.0 - 2.0) + (6.0 - 3.0) * (6.0 - 3.0));
    double actual = Point3D.distance(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
    assertEquals(expected, actual, "Static distance should calculate correctly");
  }

  @Test
  void testStaticDistanceWithSamePoints() {
    double actual = Point3D.distance(1.0, 2.0, 3.0, 1.0, 2.0, 3.0);
    assertEquals(0.0, actual, "Distance between same points should be 0");
  }

  @Test
  void testInstanceDistanceWithCoordinates() {
    double expected = Math.sqrt((4.0 - 1.0) * (4.0 - 1.0) + (5.0 - 2.0) * (5.0 - 2.0) + (6.0 - 3.0) * (6.0 - 3.0));
    double actual = point1.distance(4.0, 5.0, 6.0);
    assertEquals(expected, actual, "Instance distance should calculate correctly");
  }

  @Test
  void testInstanceDistanceWithPoint3D() {
    double expected = Math.sqrt((4.0 - 1.0) * (4.0 - 1.0) + (5.0 - 2.0) * (5.0 - 2.0) + (6.0 - 3.0) * (6.0 - 3.0));
    double actual = point1.distance(point2);
    assertEquals(expected, actual, "Instance distance with Point3D should calculate correctly");
  }

  @Test
  void testInstanceDistanceWithSelf() {
    double actual = point1.distance(point1);
    assertEquals(0.0, actual, "Distance to self should be 0");
  }

  // ========== Equals and HashCode Tests ==========

  @Test
  void testEqualsWithSameObject() {
    assertTrue(point1.equals(point1), "Point should equal itself");
  }

  @Test
  void testEqualsWithEqualPoints() {
    Point3D point1Copy = new Point3D(1.0, 2.0, 3.0);
    assertTrue(point1.equals(point1Copy), "Equal points should be equal");
  }

  @Test
  void testEqualsWithDifferentPoints() {
    assertFalse(point1.equals(point2), "Different points should not be equal");
  }

  @Test
  void testEqualsWithNull() {
    assertFalse(point1.equals(null), "Point should not equal null");
  }

  @Test
  void testEqualsWithDifferentClass() {
    assertFalse(point1.equals("not a point"), "Point should not equal different class");
  }

  @Test
  void testEqualsWithFloatingPointPrecision() {
    Point3D point1Approx = new Point3D(1.0000000001, 2.0000000001, 3.0000000001);
    assertTrue(point1.equals(point1Approx), "Points within epsilon should be equal");
  }

  @Test
  void testEqualsWithDifferentZ() {
    Point3D point1DifferentZ = new Point3D(1.0, 2.0, 3.1);
    assertFalse(point1.equals(point1DifferentZ), "Points with different z should not be equal");
  }

  @Test
  void testHashCodeConsistency() {
    int hash1 = point1.hashCode();
    int hash2 = point1.hashCode();
    assertEquals(hash1, hash2, "Hash code should be consistent");
  }

  @Test
  void testHashCodeEquality() {
    Point3D point1Copy = new Point3D(1.0, 2.0, 3.0);
    assertEquals(point1.hashCode(), point1Copy.hashCode(), "Equal points should have same hash code");
  }

  @Test
  void testHashCodeWithDifferentPoints() {
    // Hash codes might be the same by chance, but different points should generally have different hash codes
    // This test just ensures the method doesn't throw an exception
    assertDoesNotThrow(() -> point1.hashCode(), "Hash code should not throw exception");
    assertDoesNotThrow(() -> point2.hashCode(), "Hash code should not throw exception");
  }

  // ========== ToString Tests ==========

  @Test
  void testToString() {
    String expected = "xyz:[1.0, 2.0, 3.0]";
    String actual = point1.toString();
    assertEquals(expected, actual, "toString should format correctly");
  }

  @Test
  void testToStringWithNegativeValues() {
    Point3D negativePoint = new Point3D(-1.0, -2.0, -3.0);
    String expected = "xyz:[-1.0, -2.0, -3.0]";
    String actual = negativePoint.toString();
    assertEquals(expected, actual, "toString should handle negative values");
  }

  @Test
  void testToStringWithZeroValues() {
    String expected = "xyz:[0.0, 0.0, 0.0]";
    String actual = point3.toString();
    assertEquals(expected, actual, "toString should handle zero values");
  }

  @Test
  void testToStringWithDecimalValues() {
    Point3D decimalPoint = new Point3D(1.5, 2.5, 3.5);
    String expected = "xyz:[1.5, 2.5, 3.5]";
    String actual = decimalPoint.toString();
    assertEquals(expected, actual, "toString should handle decimal values");
  }

  // ========== Edge Cases and Boundary Tests ==========

  @Test
  void testVeryLargeValues() {
    Point3D largePoint = new Point3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
    assertEquals(Double.MAX_VALUE, largePoint.getX(), "Should handle very large x values");
    assertEquals(Double.MAX_VALUE, largePoint.getY(), "Should handle very large y values");
    assertEquals(Double.MAX_VALUE, largePoint.getZ(), "Should handle very large z values");
  }

  @Test
  void testVerySmallValues() {
    Point3D smallPoint = new Point3D(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    assertEquals(Double.MIN_VALUE, smallPoint.getX(), "Should handle very small x values");
    assertEquals(Double.MIN_VALUE, smallPoint.getY(), "Should handle very small y values");
    assertEquals(Double.MIN_VALUE, smallPoint.getZ(), "Should handle very small z values");
  }

  @Test
  void testDistanceWithVeryLargeValues() {
    Point3D largePoint1 = new Point3D(Double.MAX_VALUE, 0.0, 0.0);
    Point3D largePoint2 = new Point3D(0.0, 0.0, 0.0);
    assertDoesNotThrow(() -> largePoint1.distance(largePoint2), "Distance calculation should handle large values");
  }

  @Test
  void testDistanceWithVerySmallValues() {
    Point3D smallPoint1 = new Point3D(Double.MIN_VALUE, 0.0, 0.0);
    Point3D smallPoint2 = new Point3D(0.0, 0.0, 0.0);
    assertDoesNotThrow(() -> smallPoint1.distance(smallPoint2), "Distance calculation should handle small values");
  }

  @Test
  void testSetLocationWithNullPoint() {
    assertThrows(NullPointerException.class, () -> point1.setLocation((Point3D) null),
        "setLocation with null should throw NullPointerException");
  }

  @Test
  void testDistanceWithNullPoint() {
    assertThrows(NullPointerException.class, () -> point1.distance((Point3D) null),
        "distance with null should throw NullPointerException");
  }

  @Test
  void testDistanceSqWithNullPoint() {
    assertThrows(NullPointerException.class, () -> point1.distanceSq((Point3D) null),
        "distanceSq with null should throw NullPointerException");
  }

  // ========== Mathematical Properties Tests ==========

  @Test
  void testDistanceSymmetricProperty() {
    double distance1 = point1.distance(point2);
    double distance2 = point2.distance(point1);
    assertEquals(distance1, distance2, "Distance should be symmetric");
  }

  @Test
  void testDistanceTriangleInequality() {
    Point3D point4 = new Point3D(7.0, 8.0, 9.0);
    double dist12 = point1.distance(point2);
    double dist23 = point2.distance(point4);
    double dist13 = point1.distance(point4);

    assertTrue(dist13 <= dist12 + dist23, "Triangle inequality should hold");
  }

  @Test
  void testDistanceSqVsDistanceConsistency() {
    double distSq = point1.distanceSq(point2);
    double dist = point1.distance(point2);
    double expectedDist = Math.sqrt(distSq);
    assertEquals(expectedDist, dist, "Distance should equal sqrt of distanceSq");
  }
}
