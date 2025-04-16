package com.snek.framework.utils;

import org.joml.Quaternionf;
import org.joml.Vector3f;








/**
 * A utility class providing functions for 2D and 3D Euclidean geometry calculations.
 */
public abstract class SpaceUtils {
    private SpaceUtils(){}


    /**
     * Checks whether a line intersects a sphere.
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineOrigin The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param targetCenter The center of the target sphere.
     * @param targetRadius The radius of the target sphere. Must be positive.
     * @return True if the line intersects the sphere, false otherwise.
     */
    public static boolean checkLineSphereIntersection(Vector3f lineOrigin, Vector3f lineDirection, Vector3f targetCenter, float targetRadius) {

        // Calculate the direction vector from lineOrigin to targetCenter
        Vector3f toCenter = new Vector3f(targetCenter).sub(lineOrigin);

        // Find the point on the line that is closest to targetCenter
        float t = toCenter.dot(lineDirection);
        Vector3f closestPoint = new Vector3f(lineDirection).mul(t).add(lineOrigin);

        // Calculate its distance from targetCenter and return true if it's smaller than the radiud
        return targetCenter.distanceSquared(closestPoint) <= targetRadius * targetRadius;
    }




    /**
     * Checks whether a line intersects a rectangle in a 3D space.
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineOrigin The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param corner1 One of the four corners of the rectangle.
     * @param corner2 The corner opposite to corner1.
     * @return True if the line intersects the rectangle, false otherwise.
     */
    public static boolean checkLineRectangleIntersection(Vector3f lineOrigin, Vector3f lineDirection, Vector3f corner1, Vector3f corner2) {

        // Calculate corner coordinates when projected onto the screen's plane. Use lineOrigin as coordinates origin and lineDirection
        Quaternionf negativeDir = new Quaternionf().rotateTo(lineDirection, new Vector3f(0, 0, -1));
        Vector3f c1 = new Vector3f(corner1).sub(lineOrigin).rotate(negativeDir);
        Vector3f c2 = new Vector3f(corner2).sub(lineOrigin).rotate(negativeDir);


        // Check intersection on the X and Y axes, return true if none fail
        for(int i = 0; i < 2; ++i) {
            float min = c1.get(i);
            float max = c2.get(i);
            if(min > max) { float tmp = min; min = max; max = tmp; }
            if(min > 0 || max < 0) return false;
        }
        return true;
    }
}
