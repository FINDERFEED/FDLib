package com.finderfeed.fdlib.systems.trails;

import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class FDTrailDataGenerator<T> {

    private Vec3 previouslyDeletedPoint = null;

    private BiFunction<T, Float, Vec3> positionExtractor;

    private int maxPointsAmount;

    private List<FDTrailPoint> points = new ArrayList<>();

    private float minimumDistanceDeltaForNewPoint;

    public FDTrailDataGenerator(BiFunction<T, Float, Vec3> positionExtractor, int maxPointsInTrail, float minimumDistanceDeltaForNewPoint){
        this.maxPointsAmount = Mth.clamp(maxPointsInTrail,3,20);
        this.positionExtractor = positionExtractor;
        this.minimumDistanceDeltaForNewPoint = minimumDistanceDeltaForNewPoint;
    }

    public void tick(T object){
        previouslyDeletedPoint = null;
        this.tickNewPointsCreation(object);
        this.tickPoints();
    }

    private void tickNewPointsCreation(T object){

        Vec3 currentPos = this.extractPosition(object, 0f);

        if (this.points.isEmpty()){
            this.points.add(new FDTrailPoint(currentPos, this.calculatePointAge()));
        }

        FDTrailPoint lastPoint = points.getLast();

        Vec3 last = lastPoint.getPos();

        Vec3 delta = currentPos.subtract(last);

        double length = delta.length();

        if (length >= minimumDistanceDeltaForNewPoint){
            this.points.add(new FDTrailPoint(currentPos, this.calculatePointAge()));
        }

    }

    private int calculatePointAge(){
        return maxPointsAmount - 1;
    }

    private void tickPoints(){
        var iterator = points.iterator();
        while (iterator.hasNext()){
            var point = iterator.next();
            if (point.shouldBeRemoved()){
                previouslyDeletedPoint = point.getPos();
                iterator.remove();
            }
            point.tick();
        }
    }

    public Vec3 extractPosition(T object, float partialTicks){
        return this.positionExtractor.apply(object, partialTicks);
    }

    public List<FDTrailPoint> getPoints() {
        return points;
    }

    public List<Vector3f> getConvertedPointsForRendering(Vec3 currentObjectWorldPos, float pticks){


        List<Vector3f> list = new ArrayList<>();

        if (previouslyDeletedPoint != null){
            var first = this.getPoints().getFirst();
            Vec3 firstPos = first.getPos();

            Vec3 interpolated = FDMathUtil.interpolateVectors(previouslyDeletedPoint,firstPos,pticks);
            list.add(interpolated.subtract(currentObjectWorldPos).toVector3f());
        }

        for (var point : this.getPoints()){
            Vector3f newPoint = point.getPos().subtract(currentObjectWorldPos).toVector3f();;
            list.add(newPoint);
        }

        list.add(new Vector3f(0.0001f,0,0.0001f));


        return list;
    }

    public int getMaxPointsAmount() {
        return maxPointsAmount;
    }

}
