package com.finderfeed.fdlib.util.math.curves;

import com.mojang.datafixers.util.Pair;
import org.joml.Vector3f;

/**
 * Curve should contain at least two points - begin and endpoint
 */
public interface Curve {

    /**
     * @param percent - 0 -> 1
     * @return a point on a curve.
     */
    Vector3f getCurvePoint(float percent);

    /**
     * Is only useful when curve has 2 or more points
     * @param pointIndex - the index of point (resulting point will be computed from this point and next point)
     * @param percent - local percent between two points
     * @return
     */
    Vector3f getLocalCurvePoint(int pointIndex, float percent);

    /**
     * @return size of curve (point amount)
     */
    int size();

    /**
     * Add new point at index
     * @param index
     * @param point
     */
    void addPoint(int index,Vector3f point);

    Vector3f getPoint(int index);
    /**
     * @param globalPercent 0 -> 1
     * Is only useful when curve has 2 or more points
     * @return Pair of two values for getLocalPoint function
     */
    default Pair<Integer,Float> globalPercentToPointAndLocalPercent(float globalPercent){
        int size = this.size();
        float val = globalPercent * size;
        if (val >= size) return new Pair<>(size - 2,1f);
        int index = (int) val;
        return new Pair<>(index, val - index);
    }
}
