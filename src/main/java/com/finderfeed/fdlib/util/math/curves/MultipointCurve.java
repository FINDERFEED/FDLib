package com.finderfeed.fdlib.util.math.curves;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class MultipointCurve implements Curve {

    protected List<Vector3f> points;

    public MultipointCurve(Collection<Vector3f> points){
        if (points.size() < 2){
            throw new RuntimeException("Curve should consist of two or more points!");
        }
        this.points = new ArrayList<>();
        for (Vector3f p : points){
            this.points.add(new Vector3f(p));
        }
    }

    @Override
    public void addPoint(int index, Vector3f point) {
        points.add(index,point);
    }

    @Override
    public Vector3f getPoint(int index) {
        if (index < 0){
            return null;
        }else if (index >= points.size()){
            return null;
        }else{
            return points.get(index);
        }
    }

    @Override
    public Vector3f getCurvePoint(float percent) {
        var pair = this.globalPercentToPointAndLocalPercent(percent);
        return this.getLocalCurvePoint(pair.getFirst(),pair.getSecond());
    }

    @Override
    public int size() {
        return points.size();
    }
}
