package com.finderfeed.fdlib.util.math;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.data_structures.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class FDMathUtil {

    public static final float FPI = (float) Math.PI;

    public static Vector3f vec3ToVector3f(Vec3 v){
        return new Vector3f((float) v.x,(float) v.y,(float) v.z);
    }

    public static Vec3 vector3fToVec3(Vector3f v){
        return new Vec3(v.x,v.y,v.z);
    }

    public static float lerp(float v1,float v2,float p){
        return v1 + (v2 - v1) * p;
    }

    public static double lerp(double v1,double v2,double p){
        return v1 + (v2 - v1) * p;
    }

    public static Vector3f vectorBetweenVectors(Vector3f v1,Vector3f v2){
        return v1.add(v2,new Vector3f()).normalize();
    }

    public static List<Vector3f> scalePointsInDirection(List<Vector2f> basePoints, Vector2f direction,float scale){


        float angle = (float) Math.atan2(direction.x,direction.y);

        Matrix4f scale1 = new Matrix4f().scale(1,scale,1);

        List<Vector3f> list = basePoints.stream()
                .map(vec2->{

                    Vector3f v = new Vector3f(vec2.x,vec2.y,0);
                    v.rotateZ(angle);
                    scale1.transformPosition(v);
                    v.rotateZ(-angle);

                    return v;
                }).toList();

        return list;
    }


    public static float yRotFromVector(Vec3 v){
        if (v.x == 0 && v.z == 0){
            return 0;
        }

        float value = -(float) Math.atan2(v.x,v.z);

        return (float) Math.toDegrees(value);
    }

    public static float xRotFromVector(Vec3 v){
        Vec3 g = new Vec3(v.x,0,v.z);

        float value = -(float)Math.atan2(v.y,g.length());

        return (float) Math.toDegrees(value);
    }



    //from -infinite - infinite to -180 - 180
    public static float convertMCYRotationToNormal(float rot){

        float d = rot % 360;

        if (rot > 0) {
            if (d > 180){
                return -180 + (d - 180);
            }else{
                return d;
            }
        }else{
            if (d < -180){
                return 180 - (-180 - d);
            }else{
                return d;
            }
        }

    }

    public static float lerpAround(float v1,float v2,float min,float max,float p){
        if (v2 < v1){
            float v = v2;
            v2 = v1;
            v1 = v;
            p = 1 - p;
        }

        float d1 = v2 - v1;

        float v1tomin = v1 - min;
        float v2tomax = max - v2;

        float d2 = v1tomin + v2tomax;

        if (d1 < d2){
            return lerp(v1,v2,p);
        }else{
            float d = d2 * p;
            if (d < v1tomin){
                return v1 - d;
            }else{
                float remain = d - v1tomin;
                return max - remain;
            }
        }
    }

    public static Vec3 projectVectorOnVector(Vec3 v,Vec3 on){
        double dot = v.dot(on);
        double v2 = on.dot(on);
        double md = dot / v2;
        return on.multiply(md,md,md);
    }

    public static Vec3 projectVectorOntoPlane(Vec3 v,Vec3 normal){
        Vec3 proj = projectVectorOnVector(v,normal);
        return v.subtract(proj);
    }

    public static double angleBetweenVectors(Vec3 v1,Vec3 v2){
        return Math.acos(v1.dot(v2) / (v1.length() * v2.length()));
    }

    public static BlockPos vec3ToBlockPos(Vec3 v){
        return new BlockPos(
                (int)Math.floor(v.x),
                (int)Math.floor(v.y),
                (int)Math.floor(v.z)
        );
    }

    public static Vec3 getNormalVectorFromLineToPoint(Vec3 begin,Vec3 end,Vec3 point){
        Vec3 between = end.subtract(begin);
        Vec3 topr = point.subtract(begin);
        double d = topr.dot(between);
        double w = between.length(); w *= w;
        double mod = d / w;
        return point.subtract(begin.add(between.multiply(mod,mod,mod)));
    }

    public static Vector3f interpolateVectors(Vector3f v1,Vector3f v2,float p){
        return new Vector3f(
                lerp(v1.x,v2.x,p),
                lerp(v1.y,v2.y,p),
                lerp(v1.z,v2.z,p)
        );
    }

    public static Vec3 interpolateVectors(Vec3 v1,Vec3 v2,float p){
        return new Vec3(
                lerp(v1.x,v2.x,p),
                lerp(v1.y,v2.y,p),
                lerp(v1.z,v2.z,p)
        );
    }



    public static Pair<Float, List<Float>> approximateCatmullromLength(List<Vector3f> catmullromPoints, int stepsCount){

        float fullLength = 0;
        List<Float> segmentLengths = new ArrayList<>();

        for (int i = 0; i < catmullromPoints.size() - 1; i++){
            Vector3f p1 = FDLibCalls.getListValueSafe(i - 1, catmullromPoints);
            Vector3f p2 = FDLibCalls.getListValueSafe(i, catmullromPoints);
            Vector3f p3 = FDLibCalls.getListValueSafe(i + 1, catmullromPoints);
            Vector3f p4 = FDLibCalls.getListValueSafe(i + 2, catmullromPoints);
            float step = 1f / stepsCount;
            float l = 0;
            for (float p = 0; p < 1; p += step){
                Vector3f point1 = FDMathUtil.catmullrom(p1,p2,p3,p4,p);
                Vector3f point2 = FDMathUtil.catmullrom(p1,p2,p3,p4,p + step);
                Vector3f b = point2.sub(point1);
                l += b.length();
            }
            segmentLengths.add(l);
            fullLength += l;
        }
        return new Pair<>(fullLength, segmentLengths);
    }

    public static Vector3f catmullRom(List<Vector3f> points, float p){

        var lengths = approximateCatmullromLength(points,1);

        int segmentId = 0;
        float segmentPercent = 0;

        float fullLength = lengths.first;
        var segmentLengths = lengths.second;

        float accumulatedPercent = 0;

        for (float segmentLength : segmentLengths){
            float lengthPercent = segmentLength / fullLength;
            if (p > accumulatedPercent && p <= accumulatedPercent + lengthPercent){
                float local = p - accumulatedPercent;
                segmentPercent = local / lengthPercent;
                break;
            }else{
                accumulatedPercent += lengthPercent;
                segmentId++;
            }
        }

        Vector3f p1 = FDLibCalls.getListValueSafe(segmentId - 1, points);
        Vector3f p2 = FDLibCalls.getListValueSafe(segmentId, points);
        Vector3f p3 = FDLibCalls.getListValueSafe(segmentId + 1, points);
        Vector3f p4 = FDLibCalls.getListValueSafe(segmentId + 2, points);

        return FDMathUtil.catmullrom(p1,p2,p3,p4,segmentPercent);
    }

    public static Vector3f catmullRomDerivative(List<Vector3f> points, float p){

        var lengths = approximateCatmullromLength(points,1);

        int segmentId = 0;
        float segmentPercent = 0;

        float fullLength = lengths.first;
        var segmentLengths = lengths.second;

        float accumulatedPercent = 0;

        for (float segmentLength : segmentLengths){
            float lengthPercent = segmentLength / fullLength;
            if (p > accumulatedPercent && p <= accumulatedPercent + lengthPercent){
                float local = p - accumulatedPercent;
                segmentPercent = local / lengthPercent;
                break;
            }else{
                accumulatedPercent += lengthPercent;
                segmentId++;
            }
        }

        Vector3f p1 = FDLibCalls.getListValueSafe(segmentId - 1, points);
        Vector3f p2 = FDLibCalls.getListValueSafe(segmentId, points);
        Vector3f p3 = FDLibCalls.getListValueSafe(segmentId + 1, points);
        Vector3f p4 = FDLibCalls.getListValueSafe(segmentId + 2, points);

        return FDMathUtil.catmullromDerivative(p1,p2,p3,p4,segmentPercent);
    }

    public static Vector3f catmullRomPrecomputedLengths(List<Vector3f> points, float p, Pair<Float, List<Float>> lengths){

        int segmentId = 0;
        float segmentPercent = 0;

        float fullLength = lengths.first;
        var segmentLengths = lengths.second;

        float accumulatedPercent = 0;

        for (float segmentLength : segmentLengths){
            float lengthPercent = segmentLength / fullLength;
            if (p > accumulatedPercent && p <= accumulatedPercent + lengthPercent){
                float local = p - accumulatedPercent;
                segmentPercent = local / lengthPercent;
                break;
            }else{
                accumulatedPercent += lengthPercent;
                segmentId++;
            }
        }

        Vector3f p1 = FDLibCalls.getListValueSafe(segmentId - 1, points);
        Vector3f p2 = FDLibCalls.getListValueSafe(segmentId, points);
        Vector3f p3 = FDLibCalls.getListValueSafe(segmentId + 1, points);
        Vector3f p4 = FDLibCalls.getListValueSafe(segmentId + 2, points);

        return FDMathUtil.catmullrom(p1,p2,p3,p4,segmentPercent);
    }

    public static Vector3f catmullRomDerivativePrecomputedLengths(List<Vector3f> points, float p, Pair<Float, List<Float>> lengths){

        int segmentId = 0;
        float segmentPercent = 0;

        float fullLength = lengths.first;
        var segmentLengths = lengths.second;

        float accumulatedPercent = 0;

        for (float segmentLength : segmentLengths){
            float lengthPercent = segmentLength / fullLength;
            if (p > accumulatedPercent && p <= accumulatedPercent + lengthPercent){
                float local = p - accumulatedPercent;
                segmentPercent = local / lengthPercent;
                break;
            }else{
                accumulatedPercent += lengthPercent;
                segmentId++;
            }
        }

        Vector3f p1 = FDLibCalls.getListValueSafe(segmentId - 1, points);
        Vector3f p2 = FDLibCalls.getListValueSafe(segmentId, points);
        Vector3f p3 = FDLibCalls.getListValueSafe(segmentId + 1, points);
        Vector3f p4 = FDLibCalls.getListValueSafe(segmentId + 2, points);

        return FDMathUtil.catmullromDerivative(p1,p2,p3,p4,segmentPercent);
    }


    public static Vec3 linear(List<Vec3> points,float p){
        if (p < 0){
            return points.getFirst();
        }else if (p >= 1){
            return points.getLast();
        }
        float glP = p * (points.size() - 1);
        int id1 = (int)glP;
        float lp = glP - id1;
        Vec3 cur = points.get(id1);
        Vec3 next = points.get(id1 + 1);
        return interpolateVectors(cur,next,lp);
    }

    //"Safe" version of catmullrom that computes previous and "after" next points if they are null.
    public static float catmullrom(Float previous,Float current,Float next,Float next2,float p) {
        if (next == null){return current;}
        if (previous == null){previous = current + (current - next);}
        if (next2 == null){next2 = next + (next - current);}
        float xvc = (next - previous) / 6;
        float xvn = (current - next2) / 6;
        return bernstein(current,current + xvc,next + xvn,next,p);
    }

    //"Safe" version of catmullrom that computes previous and "after" next points if they are null.
    public static float catmullromDerivative(Float previous,Float current,Float next,Float next2,float p) {
        if (next == null){return current;}
        if (previous == null){previous = current + (current - next);}
        if (next2 == null){next2 = next + (next - current);}
        float xvc = (next - previous) / 6;
        float xvn = (current - next2) / 6;
        return bernsteinDerivative(current,current + xvc,next + xvn,next,p);
    }

    public static double catmullrom(Double previous,Double current,Double next,Double next2,float p) {
        if (next == null){return current;}
        if (previous == null){previous = current + (current - next);}
        if (next2 == null){next2 = next + (next - current);}
        double xvc = (next - previous) / 6;
        double xvn = (current - next2) / 6;
        return bernstein(current,current + xvc,next + xvn,next,p);
    }

    public static double catmullromDerivative(Double previous,Double current,Double next,Double next2,float p) {
        if (next == null){return current;}
        if (previous == null){previous = current + (current - next);}
        if (next2 == null){next2 = next + (next - current);}
        double xvc = (next - previous) / 6;
        double xvn = (current - next2) / 6;
        return bernsteinDerivative(current,current + xvc,next + xvn,next,p);
    }

    //"Safe" version of catmullrom that computes previous and "after" next points if they are null.
    public static Vector3f catmullrom(Vector3f previous,Vector3f current,Vector3f next,Vector3f next2,float p) {
        if (next == null){return current;}
        if (previous == null) {previous = current.add(current.sub(next,new Vector3f()),new Vector3f());}
        if (next2 == null){next2 = next.add(next.sub(current,new Vector3f()),new Vector3f());}
        return new Vector3f(
                catmullrom(previous.x,current.x,next.x,next2.x,p),
                catmullrom(previous.y,current.y,next.y,next2.y,p),
                catmullrom(previous.z,current.z,next.z,next2.z,p)
        );
    }

    //"Safe" version of catmullrom that computes previous and "after" next points if they are null.
    public static Vector3f catmullromDerivative(Vector3f previous,Vector3f current,Vector3f next,Vector3f next2,float p) {
        if (next == null){return current;}
        if (previous == null) {previous = current.add(current.sub(next,new Vector3f()),new Vector3f());}
        if (next2 == null){next2 = next.add(next.sub(current,new Vector3f()),new Vector3f());}
        return new Vector3f(
                catmullromDerivative(previous.x,current.x,next.x,next2.x,p),
                catmullromDerivative(previous.y,current.y,next.y,next2.y,p),
                catmullromDerivative(previous.z,current.z,next.z,next2.z,p)
        );
    }

    //"Safe" version of catmullrom that computes previous and "after" next points if they are null.
    public static Vec3 catmullrom(Vec3 previous,Vec3 current,Vec3 next,Vec3 next2,float p) {
        if (next == null){return current;}
        if (previous == null) {previous = current.add(current.subtract(next));}
        if (next2 == null){next2 = next.add(next.subtract(current));}
        return new Vec3(
                catmullrom(previous.x,current.x,next.x,next2.x,p),
                catmullrom(previous.y,current.y,next.y,next2.y,p),
                catmullrom(previous.z,current.z,next.z,next2.z,p)
        );
    }

    //"Safe" version of catmullrom that computes previous and "after" next points if they are null.
    public static Vec3 catmullromDerivative(Vec3 previous,Vec3 current,Vec3 next,Vec3 next2,float p) {
        if (next == null){return current;}
        if (previous == null) {previous = current.add(current.subtract(next));}
        if (next2 == null){next2 = next.add(next.subtract(current));}
        return new Vec3(
                catmullromDerivative(previous.x,current.x,next.x,next2.x,p),
                catmullromDerivative(previous.y,current.y,next.y,next2.y,p),
                catmullromDerivative(previous.z,current.z,next.z,next2.z,p)
        );
    }

    public static float bernstein(float x1,float x2,float x3,float x4,float t){
        float t3 = (float) Math.pow(t,3);
        float t2 = (float) Math.pow(t,2);
        return x1 * (-t3 + 3*t2 - 3*t  + 1) +
                x2 * (3*t3 - 6*t2 + 3*t) +
                x3 * (-3*t3 + 3*t2) +
                x4 * t3;
    }

    public static float bernsteinDerivative(float x1,float x2,float x3,float x4,float t){
        float t2 = (float) Math.pow(t,2);
        return x1 * (-3*t2 + 6*t - 3) +
                x2 * (9*t2 - 12*t + 3) +
                x3 * (-9*t2 + 6*t) +
                x4 * 3 * t2;
    }

    public static double bernstein(double x1,double x2,double x3,double x4,double t){
        double t3 = Math.pow(t,3);
        double t2 = Math.pow(t,2);
        return x1 * (-t3 + 3*t2 - 3*t  + 1) +
                x2 * (3*t3 - 6*t2 + 3*t) +
                x3 * (-3*t3 + 3*t2) +
                x4 * t3;
    }

    public static double bernsteinDerivative(double x1,double x2,double x3,double x4,double t){
        float t2 = (float) Math.pow(t,2);
        return x1 * (-3*t2 + 6*t - 3) +
                x2 * (9*t2 - 12*t + 3) +
                x3 * (-9*t2 + 6*t) +
                x4 * 3 * t2;
    }

    // the one that peak goes on y = 1 is o = 0, m = ~0.399
    public static float normalDistribution(float x,float m,float o){
        float fc = 1 / org.joml.Math.sqrt(o * o * FDMathUtil.FPI * 2);
        float st = (float)Math.pow(x - m,2) / (2 * o * o);
        float mt = (float)Math.exp(-st);
        return fc * mt;

    }
}
