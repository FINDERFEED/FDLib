package com.finderfeed.fdlib.util.math;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

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

    public static Vec3 catmullRom(Vec3[] points,float p){
        if (p < 0){
            return points[0];
        }else if (p >= 1){
            return points[points.length - 1];
        }

        float glP = p * (points.length - 1);
        int id1 = (int)glP;
        float lp = glP - id1;
        Vec3 prev = id1 > 0 ? points[id1] : null;
        Vec3 cur = points[id1];
        Vec3 next = points[id1 + 1];
        Vec3 next2 = id1 < points.length - 2 ? points[id1 + 2] : null;
        return catmullrom(prev,cur,next,next2,lp);
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

    public static Vec3 catmullRom(List<Vec3> points, float p){
        if (p < 0){
            return points.getFirst();
        }else if (p >= 1){
            return points.getLast();
        }

        float glP = p * (points.size() - 1);
        int id1 = (int)glP;
        float lp = glP - id1;
        Vec3 prev = id1 > 0 ? points.get(id1) : null;
        Vec3 cur = points.get(id1);
        Vec3 next = points.get(id1 + 1);
        Vec3 next2 = id1 < points.size() - 2 ? points.get(id1 + 2) : null;
        return catmullrom(prev,cur,next,next2,lp);
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

    public static double catmullrom(Double previous,Double current,Double next,Double next2,float p) {
        if (next == null){return current;}
        if (previous == null){previous = current + (current - next);}
        if (next2 == null){next2 = next + (next - current);}
        double xvc = (next - previous) / 6;
        double xvn = (current - next2) / 6;
        return bernstein(current,current + xvc,next + xvn,next,p);
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

    private static float bernstein(float x1,float x2,float x3,float x4,float t){
        float t3 = (float) Math.pow(t,3);
        float t2 = (float) Math.pow(t,2);
        return x1 * (-t3 + 3*t2 - 3*t  + 1) +
                x2 * (3*t3 - 6*t2 + 3*t) +
                x3 * (-3*t3 + 3*t2) +
                x4 * t3;
    }

    private static double bernstein(double x1,double x2,double x3,double x4,double t){
        double t3 = Math.pow(t,3);
        double t2 = Math.pow(t,2);
        return x1 * (-t3 + 3*t2 - 3*t  + 1) +
                x2 * (3*t3 - 6*t2 + 3*t) +
                x3 * (-3*t3 + 3*t2) +
                x4 * t3;
    }

    // the one that peak goes on y = 1 is o = 0, m = ~0.399
    public static float normalDistribution(float x,float m,float o){
        float fc = 1 / org.joml.Math.sqrt(o * o * FDMathUtil.FPI * 2);
        float st = (float)Math.pow(x - m,2) / (2 * o * o);
        float mt = (float)Math.exp(-st);
        return fc * mt;

    }
}
