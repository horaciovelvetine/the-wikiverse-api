package edu.velvet.Wikiverse.api.models.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.awt.geom.Point2D;

/**
 * Represents a point in 3D space with x, y, and z coordinates.
 * This class extends Point2D.Double to provide 3D functionality while
 * maintaining compatibility with 2D operations.
 *
 * <p>
 * This class provides methods to manage 3D point properties such as:
 * <ul>
 * <li>Coordinate access and modification</li>
 * <li>Distance calculations in 3D space</li>
 * <li>Location setting and copying</li>
 * <li>String representation and equality comparison</li>
 * </ul>
 *
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization
 * with field visibility set to ANY for automatic property mapping.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see java.awt.geom.Point2D.Double
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Point3D extends Point2D.Double {

	public double z;

	/**
	 * Constructs a new Point3D object with default coordinates (0, 0, 0).
	 * This constructor initializes all three coordinates to zero, creating
	 * a point at the origin of the 3D coordinate system.
	 */
	public Point3D() {
		super(0.0, 0.0);
		z = 0.0;
	}

	/**
	 * Constructs a new Point3D object with the specified coordinates.
	 * This constructor allows you to create a point at any location in 3D space
	 * by providing the x, y, and z coordinate values.
	 *
	 * @param x the x-coordinate of the point in 3D space
	 * @param y the y-coordinate of the point in 3D space
	 * @param z the z-coordinate of the point in 3D space
	 */
	public Point3D(double x, double y, double z) {
		super(x, y);
		this.z = z;
	}

	/**
	 * Gets the z-coordinate of this point.
	 *
	 * @return the z-coordinate value of this point
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Sets the location of this point to the specified coordinates.
	 * This method updates all three coordinates of the point to the provided
	 * values,
	 * effectively moving the point to a new location in 3D space.
	 *
	 * @param x the new x-coordinate for this point
	 * @param y the new y-coordinate for this point
	 * @param z the new z-coordinate for this point
	 */
	public void setLocation(double x, double y, double z) {
		super.setLocation(x, y);
		this.z = z;
	}

	/**
	 * Sets the location of this point to the coordinates of the specified Point3D
	 * object.
	 * This method copies the coordinates from another Point3D object, effectively
	 * moving this point to the same location as the source point.
	 *
	 * @param p the Point3D object whose coordinates will be copied to this point
	 */
	public void setLocation(Point3D p) {
		this.setLocation(p.getX(), p.getY(), p.getZ());
	}

	/**
	 * Calculates the square of the Euclidean distance between two points in 3D
	 * space.
	 * This static method computes the squared distance without taking the square
	 * root,
	 * which is useful for performance optimization when only comparing distances.
	 *
	 * @param x1 the x-coordinate of the first point
	 * @param y1 the y-coordinate of the first point
	 * @param z1 the z-coordinate of the first point
	 * @param x2 the x-coordinate of the second point
	 * @param y2 the y-coordinate of the second point
	 * @param z2 the z-coordinate of the second point
	 * @return the square of the Euclidean distance between the two points
	 */
	public static double distanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double dz = z2 - z1;
		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Calculates the square of the Euclidean distance between this point and the
	 * specified point in 3D space.
	 * This method computes the squared distance without taking the square root,
	 * which is useful for performance optimization when only comparing distances.
	 *
	 * @param x the x-coordinate of the target point
	 * @param y the y-coordinate of the target point
	 * @param z the z-coordinate of the target point
	 * @return the square of the Euclidean distance between this point and the
	 *         specified point
	 */
	public double distanceSq(double x, double y, double z) {
		return distanceSq(getX(), getY(), this.z, x, y, z);
	}

	/**
	 * Calculates the square of the Euclidean distance between this point and the
	 * specified Point3D object in 3D space.
	 * This method computes the squared distance without taking the square root,
	 * which is useful for performance optimization when only comparing distances.
	 *
	 * @param pt the Point3D object to calculate the distance to
	 * @return the square of the Euclidean distance between this point and the
	 *         specified Point3D object
	 */
	public double distanceSq(Point3D pt) {
		return distanceSq(x, y, z, pt.getX(), pt.getY(), pt.getZ());
	}

	/**
	 * Calculates the Euclidean distance between two points in 3D space.
	 * This static method computes the actual distance by taking the square root
	 * of the squared distance calculation.
	 *
	 * @param x1 the x-coordinate of the first point
	 * @param y1 the y-coordinate of the first point
	 * @param z1 the z-coordinate of the first point
	 * @param x2 the x-coordinate of the second point
	 * @param y2 the y-coordinate of the second point
	 * @param z2 the z-coordinate of the second point
	 * @return the Euclidean distance between the two points
	 */
	public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(distanceSq(x1, y1, z1, x2, y2, z2));
	}

	/**
	 * Calculates the Euclidean distance between this point and the specified point
	 * in 3D space.
	 * This method computes the actual distance by taking the square root
	 * of the squared distance calculation.
	 *
	 * @param x the x-coordinate of the target point
	 * @param y the y-coordinate of the target point
	 * @param z the z-coordinate of the target point
	 * @return the Euclidean distance between this point and the specified point
	 */
	public double distance(double x, double y, double z) {
		return Math.sqrt(distanceSq(x, y, z));
	}

	/**
	 * Calculates the Euclidean distance between this point and the specified
	 * Point3D object in 3D space.
	 * This method computes the actual distance by taking the square root
	 * of the squared distance calculation.
	 *
	 * @param pt the Point3D object to calculate the distance to
	 * @return the Euclidean distance between this point and the specified Point3D
	 *         object
	 */
	public double distance(Point3D pt) {
		return Math.sqrt(distanceSq(pt));
	}

	/**
	 * Resets the coordinates of this Point3D object to the origin (0, 0, 0).
	 * <p>
	 * If any of the coordinates (x, y, or z) are not already zero, this method sets
	 * all three to zero. This is useful for reusing the same Point3D instance
	 * without
	 * creating a new object, such as in iterative computations or geometry
	 * manipulations.
	 * </p>
	 */
	public void reset() {
		if (x != 0.0 || y != 0.0 || z != 0.0) {
			x = 0.0;
			y = 0.0;
			z = 0.0;
		}
	}

	/**
	 * Returns a string representation of this Point3D object.
	 * The string format is "xyz:[x, y, z]" where x, y, and z are the coordinate
	 * values.
	 *
	 * @return a string representation of this Point3D object in the format "xyz:[x,
	 *         y, z]"
	 */
	@Override
	public String toString() {
		String c = ", ";
		return "xyz:[" + getX() + c + getY() + c + z + "]";
	}

	/**
	 * Checks if this Point3D object is equal to the specified object.
	 * Two Point3D objects are considered equal if their coordinates are within
	 * a small tolerance (1e-9) to account for floating-point precision issues.
	 *
	 * @param obj the object to compare with this Point3D
	 * @return true if the objects are equal within tolerance, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Point3D p = (Point3D) obj;
		final double EPSILON = 1e-9; // Tolerance for floating-point comparison
		return (
			Math.abs(getX() - p.getX()) < EPSILON &&
			Math.abs(getY() - p.getY()) < EPSILON &&
			Math.abs(getZ() - p.getZ()) < EPSILON
		);
	}

	/**
	 * Returns the hash code value for this Point3D object.
	 * The hash code is computed using the coordinate values to ensure that
	 * equal objects have equal hash codes.
	 *
	 * @return the hash code value for this Point3D object
	 */
	@Override
	public int hashCode() {
		long bits = java.lang.Double.doubleToLongBits(x);
		int hash = (int) (bits ^ (bits >>> 32));
		hash = 31 * hash + (int) (java.lang.Double.doubleToLongBits(y) ^ (java.lang.Double.doubleToLongBits(y) >>> 32));
		hash = 31 * hash + (int) (java.lang.Double.doubleToLongBits(z) ^ (java.lang.Double.doubleToLongBits(z) >>> 32));
		return hash;
	}
}
