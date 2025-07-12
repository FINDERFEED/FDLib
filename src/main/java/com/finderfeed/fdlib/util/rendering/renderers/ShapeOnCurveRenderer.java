package com.finderfeed.fdlib.util.rendering.renderers;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.systems.shapes.FD2DShape;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.AxisAngle4d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ShapeOnCurveRenderer {

    private List<Vector3f> splinePoints;
    private FD2DShape shape;
    private int light = LightTexture.FULL_BRIGHT;
    private int overlay = OverlayTexture.NO_OVERLAY;
    private int lod = 20;
    private VertexConsumer vertexConsumer;

    private Function<Float, Float> scaleCoefficient = v->1f;

    private float startPercent = 0;
    private float endPercent = 1;

    private FDColor startColor = new FDColor(1,1,1,1);
    private FDColor endColor = new FDColor(1,1,1,1);

    private PoseStack matrices;

    private float uModifier = 1f;
    private float vModifier = 2f;

    public static ShapeOnCurveRenderer start(VertexConsumer vertexConsumer){
        return new ShapeOnCurveRenderer(vertexConsumer);
    }

    public ShapeOnCurveRenderer(VertexConsumer vertexConsumer){
        this.vertexConsumer = vertexConsumer;
    }

    public ShapeOnCurveRenderer lod(int lod){
        this.lod = lod;
        return this;
    }

    public ShapeOnCurveRenderer color(float r, float g, float b, float a){
        this.startColor = new FDColor(r,g,b,a);
        this.endColor = new FDColor(r,g,b,a);
        return this;
    }

    public ShapeOnCurveRenderer color(FDColor color){
        this.startColor = color;
        this.endColor = color;
        return this;
    }


    public ShapeOnCurveRenderer startColor(FDColor color){
        this.startColor = color;
        return this;
    }

    public ShapeOnCurveRenderer endColor(FDColor color){
        this.endColor = color;
        return this;
    }



    public ShapeOnCurveRenderer pose(PoseStack matrices){
        this.matrices = matrices;
        return this;
    }

    public ShapeOnCurveRenderer curvePositions(Vector3f... positions){
        this.splinePoints = List.of(positions);
        return this;
    }

    public ShapeOnCurveRenderer curvePositions(List<Vector3f> curvePositions){
        this.splinePoints = curvePositions;
        return this;
    }

    public ShapeOnCurveRenderer shape(FD2DShape shape){
        this.shape = shape;
        return this;
    }

    public ShapeOnCurveRenderer light(int light){
        this.light = light;
        return this;
    }

    public ShapeOnCurveRenderer overlay(int overlay){
        this.overlay = overlay;
        return this;
    }

    public ShapeOnCurveRenderer startPercent(float startPercent){
        this.startPercent = startPercent;
        return this;
    }

    public ShapeOnCurveRenderer endPercent(float endPercent){
        this.endPercent = endPercent;
        return this;
    }

    public ShapeOnCurveRenderer scalingFunction(Function<Float, Float> scaleCoefficient){
        this.scaleCoefficient = scaleCoefficient;
        return this;
    }

    public ShapeOnCurveRenderer trailScalingFunction(){
        this.scaleCoefficient = v->{
            return v;
        };
        return this;
    }

    public ShapeOnCurveRenderer uModifier(float mod){
        this.uModifier = mod;
        return this;
    }

    public ShapeOnCurveRenderer vModifier(float mod){
        this.vModifier = mod;
        return this;
    }

    public void render(){

        if (startPercent >= endPercent){
            return;
        }

        matrices.pushPose();

        var precomputedLengths = approximateCatmullromLength(splinePoints, 2);

        Vector3f between = catmullRomDerivativePrecomputedLengths(splinePoints, 0, precomputedLengths);

        Vector3f oldPoint = splinePoints.getFirst();

        Vector3f directionPrev = between;

        Quaternionf oldRot = new Quaternionf().rotationTo(new Vector3f(0,1,0),between);

        double angle = -Math.atan2(between.z,between.x);

        float initScale = this.scaleCoefficient.apply(0f);

        List<Vector3f> rotatedShape = scaleRotateAndTranslatePoints(new Quaternionf(new AxisAngle4d(angle, 0, 1, 0)), new Vector3f(), shape.getPoints(),1f);

        List<Vector3f> previousPoints = scaleRotateAndTranslatePoints(oldRot, splinePoints.getFirst(), rotatedShape,initScale);

        int totalShapePoints = shape.getPoints().size();

        Matrix4f mt = matrices.last().pose();


        List<Vector3f> shapeCache = new ArrayList<>();
        List<Vector3f> shapeCache2 = new ArrayList<>();


        boolean passedStartPercent = startPercent <= 0;

        for (int i = 1; i < lod; i++) {

            float p2 = (float) i / (lod - 1);
            float p2prev = (float) (i - 1) / (lod - 1);

            float p2c = p2;

            boolean passedEnd = false;
            float endPercentU = p2;
            if (endPercent > p2prev && endPercent < p2){
                endPercentU = endPercent;
                p2 = endPercent;
                passedEnd = true;
            }


            float scaleCurrent = this.scaleCoefficient.apply(p2);
            float scalePrev = this.scaleCoefficient.apply(p2prev);


            var pair = catmullRomAndDerivativePrecomputedLengths(splinePoints, p2, precomputedLengths);

            Vector3f point2 = pair.first;
            Vector3f directionNew = pair.second;



            float startPercentU = p2prev;
            if (!passedStartPercent && startPercent > p2prev && startPercent < p2c){
                Vector3f b = point2.sub(oldPoint, new Vector3f());
                float pdist = p2c - p2prev;
                float pcdist = startPercent - p2prev;
                float pl = pcdist / pdist;


                float scaleCurrentc = this.scaleCoefficient.apply(p2c);

                float currentScale = FDMathUtil.lerp(scalePrev,scaleCurrentc,pl);

                rescalePoints(previousPoints, oldPoint, scalePrev, currentScale);

                startPercentU = startPercent;
                b.mul(pl);
                for (Vector3f ppoint : previousPoints){
                    ppoint.add(b);
                }

                passedStartPercent = true;
            }





            Quaternionf rotationTowards = new Quaternionf().rotationTo(directionPrev, directionNew);

            Quaternionf newRot = rotationTowards.mul(oldRot);

            if (!newRot.isFinite()){
                newRot = oldRot;
            }



            List<Vector3f> nextPoints = scaleRotateAndTranslatePointsToCache(newRot, point2, rotatedShape, scaleCurrent, i % 2 == 0 ? shapeCache : shapeCache2);

            if (passedStartPercent) {
                for (int g = 0; g < totalShapePoints; g++) {

                    Vector3f sp1 = previousPoints.get(g);
                    Vector3f sp2 = nextPoints.get(g);
                    Vector3f sp3 = nextPoints.get((g + 1) % totalShapePoints);
                    Vector3f sp4 = previousPoints.get((g + 1) % totalShapePoints);

                    Vector3f r1 = sp2.sub(sp1, new Vector3f());
                    Vector3f r2 = sp4.sub(sp1, new Vector3f());
                    Vector3f normal = r1.cross(r2).mul(-1);

                    float v1 = (float) g / (totalShapePoints);
                    float v2 = (g + 1f) / (totalShapePoints);

                    float rc1 = FDMathUtil.lerp(startColor.r,endColor.r,p2prev);
                    float rc2 = FDMathUtil.lerp(startColor.r,endColor.r,p2);
                    float gc1 = FDMathUtil.lerp(startColor.g,endColor.g,p2prev);
                    float gc2 = FDMathUtil.lerp(startColor.g,endColor.g,p2);
                    float bc1 = FDMathUtil.lerp(startColor.b,endColor.b,p2prev);
                    float bc2 = FDMathUtil.lerp(startColor.b,endColor.b,p2);
                    float ac1 = FDMathUtil.lerp(startColor.a,endColor.a,p2prev);
                    float ac2 = FDMathUtil.lerp(startColor.a,endColor.a,p2);

                    vertexConsumer.addVertex(mt, (float) sp4.x, (float) sp4.y, (float) sp4.z).setColor(rc1,gc1,bc1,ac1).setUv(startPercentU * uModifier, v2 * vModifier).setOverlay(overlay).setLight(light).setNormal(normal.x, normal.y, normal.z);
                    vertexConsumer.addVertex(mt, (float) sp3.x, (float) sp3.y, (float) sp3.z).setColor(rc2,gc2,bc2,ac2).setUv(endPercentU * uModifier, v2 * vModifier).setOverlay(overlay).setLight(light).setNormal(normal.x, normal.y, normal.z);
                    vertexConsumer.addVertex(mt, (float) sp2.x, (float) sp2.y, (float) sp2.z).setColor(rc2,gc2,bc2,ac2).setUv(endPercentU * uModifier, v1 * vModifier).setOverlay(overlay).setLight(light).setNormal(normal.x, normal.y, normal.z);
                    vertexConsumer.addVertex(mt, (float) sp1.x, (float) sp1.y, (float) sp1.z).setColor(rc1,gc1,bc1,ac1).setUv(startPercentU * uModifier, v1 * vModifier).setOverlay(overlay).setLight(light).setNormal(normal.x, normal.y, normal.z);

                }
            }

            if (passedEnd){
                break;
            }

            previousPoints = nextPoints;

            directionPrev = directionNew;

            oldRot = newRot;

            oldPoint = point2;

        }


        matrices.popPose();
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

    private static Vector3f catmullRom(List<Vector3f> points, float p){

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

    private static Vector3f catmullRomDerivative(List<Vector3f> points, float p){

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

    private static Vector3f catmullRomPrecomputedLengths(List<Vector3f> points, float p, Pair<Float, List<Float>> lengths){

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

    private static Vector3f catmullRomDerivativePrecomputedLengths(List<Vector3f> points, float p, Pair<Float, List<Float>> lengths){

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

    private static Pair<Vector3f, Vector3f> catmullRomAndDerivativePrecomputedLengths(List<Vector3f> points, float p, Pair<Float, List<Float>> lengths){

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

        return new Pair<>(FDMathUtil.catmullrom(p1,p2,p3,p4,segmentPercent), FDMathUtil.catmullromDerivative(p1,p2,p3,p4,segmentPercent));
    }

    private static void rescalePoints(List<Vector3f> points, Vector3f translation, float oldScale, float newScale){
        for (Vector3f point : points){
            point.sub(translation).mul(newScale/oldScale).add(translation);
        }
    }

    private static List<Vector3f> scaleRotateAndTranslatePoints(Quaternionf quaternionf, Vector3f translatePoint, List<Vector3f> points, float scale){
        List<Vector3f> newList = new ArrayList<>();
        for (Vector3f point : points){
            newList.add(rotatePoint(quaternionf, point).mul(scale,scale,scale).add(translatePoint));
        }
        return newList;
    }

    private static List<Vector3f> scaleRotateAndTranslatePointsToCache(Quaternionf quaternionf, Vector3f translatePoint, List<Vector3f> points, float scale, List<Vector3f> newList){
        newList.clear();
        for (Vector3f point : points){
            newList.add(rotatePoint(quaternionf, point).mul(scale,scale,scale).add(translatePoint));
        }
        return newList;
    }

    private static Vector3f rotatePoint(Quaternionf quaternionf, Vector3f v){
        return quaternionf.transform(v, new Vector3f());
    }

}
