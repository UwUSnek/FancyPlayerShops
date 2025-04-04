package com.snek.framework.utils;

import org.joml.Vector3f;








/**
 * A utility class providing functions for 2D and 3D Euclidean geometry calculations.
 */
public abstract class SpaceUtils {


    /**
     * Checks whether a line intersects a sphere.
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineStart The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param targetCenter The center of the target sphere.
     * @param targetRadius The radius of the target sphere. Must be positive.
     */
    public static boolean checkLineSphereIntersection(Vector3f lineStart, Vector3f lineDirection, Vector3f targetCenter, float targetRadius) {

        // Calculate the direction vector from lineStart to targetCenter
        Vector3f toCenter = new Vector3f(targetCenter).sub(lineStart);

        // Find the point on the line that is closest to targetCenter
        float t = toCenter.dot(lineDirection);
        Vector3f closestPoint = new Vector3f(lineDirection).mul(t).add(lineStart);

        // Calculate its distance from targetCenter and return true if it's smaller than the radiud
        return targetCenter.distanceSquared(closestPoint) <= targetRadius * targetRadius;
    }
}
