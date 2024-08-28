package com.finderfeed.fdlib.systems.bedrock.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FDModelPart {

    public float x;
    public float y;
    public float z;

    public float xRot;
    public float yRot;
    public float zRot;

    public Vector3f initRotation;
    public boolean isVisible = true;

    public float xScale = 1f;
    public float yScale = 1f;
    public float zScale = 1f;
    public final Vector3f pivot;


    public final List<FDCube> cubes;

    protected final Map<String,FDModelPart> children;

    public String name;

    public FDModelPart parent = null;

    public FDModelPart(String name,FDModelPart parent,List<FDCube> cubes, Vector3f pivot, Vector3f initRotation){
        this.pivot = new Vector3f(pivot);
        this.x = pivot.x;
        this.y = pivot.y;
        this.z = pivot.z;
        this.name = name;
        this.cubes = Collections.unmodifiableList(cubes);
        this.children = new HashMap<>();
        this.xRot = (float)initRotation.x;
        this.yRot = (float)initRotation.y;
        this.zRot = (float)initRotation.z;
        this.initRotation = new Vector3f(initRotation);
        this.parent = parent;
    }


    public void render(PoseStack matrices, VertexConsumer vertex, int light, int overlay,float r,float g,float b,float a){
        matrices.pushPose();

        if (isVisible){
            this.transform(matrices);

            for (FDCube cube : cubes){
                cube.render(matrices,vertex,light,overlay,r,g,b,a);
            }

            for (FDModelPart child : children.values()){
                child.render(matrices,vertex,light,overlay,r,g,b,a);
            }

        }

        matrices.popPose();
    }


    public void transform(Matrix4f matrix){
        float px = x / 16;
        float py = y / 16;
        float pz = z / 16;


        matrix.translate(px,py,pz);
        if (xRot != 0 || yRot != 0 || zRot != 0){

            Quaternionf q = new Quaternionf().rotationZYX(
                    (float)Math.toRadians(zRot),
                    (float)Math.toRadians(yRot),
                    (float)Math.toRadians(xRot));
            Matrix4f mat = q.get(new Matrix4f());
            matrix.mul(mat);
        }
        matrix.scale(xScale, yScale, zScale);
    }


    @OnlyIn(Dist.CLIENT)
    private void transform(PoseStack matrices){
        double px = x / 16;
        double py = y / 16;
        double pz = z / 16;

        matrices.translate(px,py,pz);
        if (xRot != 0 || yRot != 0 || zRot != 0){
            matrices.mulPose(new Quaternionf().rotationZYX(
                    (float)Math.toRadians(zRot),
                    (float)Math.toRadians(yRot),
                    (float)Math.toRadians(xRot)));
        }
        matrices.scale(xScale, yScale, zScale);
    }


    public void reset(){
        this.x = pivot.x;
        this.y = pivot.y;
        this.z = pivot.z;
        this.xRot = (float)initRotation.x;
        this.yRot = (float)initRotation.y;
        this.zRot = (float)initRotation.z;
        this.xScale = 1f;
        this.yScale = 1f;
        this.zScale = 1f;
        for (FDModelPart part : this.children.values()){
            part.reset();
        }
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Map<String, FDModelPart> getChildren() {
        return Collections.unmodifiableMap(children);
    }

    public FDModelPart getParent() {
        return parent;
    }
}
