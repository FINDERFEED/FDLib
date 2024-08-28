package com.finderfeed.fdlib.systems.bedrock.models;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.*;

import java.lang.Math;
import java.util.Arrays;
import java.util.List;

public class FDCube {

    public static final StreamCodec<FriendlyByteBuf,FDCube> CODEC = StreamCodec.composite(
            FDFace.CODEC,cube->cube.faces[0],
            FDFace.CODEC,cube->cube.faces[1],
            FDFace.CODEC,cube->cube.faces[2],
            FDFace.CODEC,cube->cube.faces[3],
            FDFace.CODEC,cube->cube.faces[4],
            FDFace.CODEC,cube->cube.faces[5],
            FDCube::new
    );

    private static final float POS_MULTIPLIER = 1/16f;
    private FDFace[] faces = new FDFace[6];

    private FDCube(FDFace... faces){
        this.faces[0] = faces[0];
        this.faces[1] = faces[1];
        this.faces[2] = faces[2];
        this.faces[3] = faces[3];
        this.faces[4] = faces[4];
        this.faces[5] = faces[5];
    }

    private FDCube(FDFace f1,FDFace f2,FDFace f3,FDFace f4,FDFace f5,FDFace f6){
        this.faces[0] = f1;
        this.faces[1] = f2;
        this.faces[2] = f3;
        this.faces[3] = f4;
        this.faces[4] = f5;
        this.faces[5] = f6;
    }



    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack matrices, VertexConsumer vertex, int light, int overlay,float r,float g,float b,float a){
        matrices.pushPose();
        Matrix4f m = matrices.last().pose();
        Matrix3f n = matrices.last().normal();
        for (FDFace fdFace : faces){
            Vector3f normal = n.transform(new Vector3f(
                    (float)fdFace.getNormal().x,
                    (float)fdFace.getNormal().y,
                    (float)fdFace.getNormal().z
            ));

            for (FDVertex v : fdFace.getVertices()){
                Vector3f vertexPos = v.getPosition();
                vertex.addVertex(m,(float)vertexPos.x * POS_MULTIPLIER,(float)vertexPos.y * POS_MULTIPLIER,(float)vertexPos.z * POS_MULTIPLIER);
                vertex.setColor(r,g,b,a);
                vertex.setUv(v.getU(),v.getV());
                vertex.setOverlay(overlay);
                vertex.setLight(light);
                vertex.setNormal(normal.x, normal.y, normal.z);
            }
        }

        matrices.popPose();
    }


    public FDFace[] getFaces() {
        return faces;
    }

    private static final Vec3[] normals = {
            new Vec3(0,0,-1),
            new Vec3(1,0,0),
            new Vec3(0,0,1),
            new Vec3(-1,0,0),
            new Vec3(0,1,0),
            new Vec3(0,-1,0),
    };


    public static FDCube fromJson(JsonObject scube,Vector3f bonePivot,int textureWidth,int textureHeight,float scale){
        if (scube.has("mirror")){
            throw new RuntimeException("Mirror UV is not supported!" + scube);
        }
        Vector3f origin = JsonHelper.parseVector3f(scube.getAsJsonArray("origin"));
        Vector3f size = JsonHelper.parseVector3f(scube.getAsJsonArray("size"));

        float inflate = JsonHelper.parseFloat(scube,"inflate");

        size = size.add(inflate*2,inflate*2,inflate*2);



        origin = new Vector3f(-origin.x - size.x - inflate,origin.y - inflate,origin.z - inflate);



        Vec3 pivot = JsonHelper.parseVec3(scube,"pivot").multiply(-1,1,1);
        Vec3 rotation = JsonHelper.parseVec3(scube,"rotation").multiply(-1,-1,1);

        Matrix4fStack matrices = new Matrix4fStack(4);
        matrices.pushMatrix();
        matrices.translate((float) pivot.x,(float) pivot.y,(float) pivot.z);
        boolean shouldRotate = rotation.x != 0 || rotation.y != 0 || rotation.z != 0;
        if (shouldRotate) {
            Quaternionf q = new Quaternionf().rotationZYX((float) Math.toRadians(rotation.z), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.x));
            Matrix4f mat = new Matrix4f();
            q.get(mat);
            matrices.mul(mat);
        }
        matrices.translate((float) -pivot.x,(float) -pivot.y,(float) -pivot.z);
        Matrix4f m = matrices;
        //center and size transformations
        Vector3f cubeCenter = origin.add(size.mul(0.5f,0.5f,0.5f,new Vector3f()),new Vector3f());
        Vector3f between = origin.sub(cubeCenter,new Vector3f()).mul(
                scale - 1,
                scale - 1,
                scale - 1);
        origin = origin.add(between);
        size = size.mul(scale,scale,scale,new Vector3f());

        Vector3f v1 = mul(new Vector3f(origin),m);
        Vector3f v2 = mul(origin.add(size.x,0,0,new Vector3f()),m);
        Vector3f v3 = mul(origin.add(0,size.y,0,new Vector3f()),m);
        Vector3f v4 = mul(origin.add(0,0,size.z,new Vector3f()),m);
        Vector3f v5 = mul(origin.add(size.x,size.y,0,new Vector3f()),m);
        Vector3f v6 = mul(origin.add(0,size.y,size.z,new Vector3f()),m);
        Vector3f v7 = mul(origin.add(size.x,0,size.z,new Vector3f()),m);
        Vector3f v8 = mul(origin.add(size,new Vector3f()),m);

        Matrix3f mn = matrices.normal(new Matrix3f());
        List<Vector3f> tnormals = Arrays.stream(normals).map(normal->mn.transform(
                        (float)normal.x, (float)normal.y,(float)normal.z,new Vector3f()))
                .map(vector3f -> new Vector3f(vector3f.x,vector3f.y,vector3f.z))
                .toList();
        matrices.popMatrix();

        JsonElement faceDatas = scube.get("uv");
        FDFace north= null;
        FDFace east = null;
        FDFace south = null;
        FDFace west = null;
        FDFace up = null;
        FDFace down = null;
        if (!faceDatas.isJsonArray()) {
            north = createFace(v1, v2, v5, v3, tnormals.get(0), faceDatas.getAsJsonObject().getAsJsonObject("north"), textureWidth, textureHeight);
            east = createFace(v2, v7, v8, v5, tnormals.get(1), faceDatas.getAsJsonObject().getAsJsonObject("east"), textureWidth, textureHeight);
            south = createFace(v7, v4, v6, v8, tnormals.get(2), faceDatas.getAsJsonObject().getAsJsonObject("south"), textureWidth, textureHeight);
            west = createFace(v4, v1, v3, v6, tnormals.get(3), faceDatas.getAsJsonObject().getAsJsonObject("west"), textureWidth, textureHeight);
            up = createFace(v3, v5, v8, v6, tnormals.get(4), faceDatas.getAsJsonObject().getAsJsonObject("up"), textureWidth, textureHeight);
            down = createFace(v4, v7, v2, v1, tnormals.get(5), faceDatas.getAsJsonObject().getAsJsonObject("down"), textureWidth, textureHeight);
        }else{
            Vec2 uv = JsonHelper.parseVec2(faceDatas);

            float x = (float)size.x/scale - inflate*2;
            float y = (float)size.y/scale - inflate*2;
            float z = (float)size.z/scale - inflate*2;
            north = createFace(v1, v2, v5, v3, tnormals.get(0),
                    new Vec2(uv.x + z + x,uv.y + z + y),
                    new Vec2(uv.x + z,uv.y + z + y),
                    new Vec2(uv.x + z,uv.y + z),
                    new Vec2(uv.x + z + x,uv.y + z)
                    , textureWidth, textureHeight);
            east = createFace(v2, v7, v8, v5, tnormals.get(1),
                    new Vec2(uv.x + z,uv.y + z + y),
                    new Vec2(uv.x ,uv.y + z + y),
                    new Vec2(uv.x ,uv.y + z),
                    new Vec2(uv.x + z,uv.y + z)
                    , textureWidth, textureHeight);
            south = createFace(v7, v4, v6, v8, tnormals.get(2),
                    new Vec2(uv.x + z + x + z + x,uv.y + z + y),
                    new Vec2(uv.x + z + x + z,uv.y + z + y),
                    new Vec2(uv.x + z + x + z,uv.y + z),
                    new Vec2(uv.x + z + x + z + x,uv.y + z)
                    , textureWidth, textureHeight);
            west = createFace(v4, v1, v3, v6, tnormals.get(3),
                    new Vec2(uv.x + z + x + z,uv.y + z + y),
                    new Vec2(uv.x + z + x,uv.y + z + y),
                    new Vec2(uv.x + z + x,uv.y + z),
                    new Vec2(uv.x + z + x + z,uv.y + z)
                    , textureWidth, textureHeight);
            up = createFace(v3, v5, v8, v6, tnormals.get(4),
                    new Vec2(uv.x + z + x,uv.y + z),
                    new Vec2(uv.x + z,uv.y + z),
                    new Vec2(uv.x + z,uv.y),
                    new Vec2(uv.x + z + x,uv.y)
                    , textureWidth, textureHeight);
            down = createFace(v4, v7, v2, v1, tnormals.get(5),
                                        new Vec2(uv.x + z + x + x,uv.y),
                                        new Vec2(uv.x + z + x,uv.y),
                                        new Vec2(uv.x + z + x,uv.y + z),
                                        new Vec2(uv.x + z + x + x,uv.y + z)
                    , textureWidth, textureHeight);
        }
        //translating to bone
        FDCube cube = new FDCube(north,east,south,west,up,down);
        for (FDFace fdFace : cube.faces){
            for (FDVertex vertex : fdFace.getVertices()){
                vertex.getPosition().sub(bonePivot);
            }
        }
        return cube;
    }

    private static FDFace createFace(Vector3f v1,Vector3f v2,Vector3f v3,Vector3f v4,Vector3f normal,Vec2 uv1,Vec2 uv2,Vec2 uv3,Vec2 uv4,int texWidth,int texHeight){
        Vec2 nuv1 = new Vec2(uv1.x / texWidth,uv1.y / texHeight);
        Vec2 nuv2 = new Vec2(uv2.x / texWidth,uv2.y / texHeight);
        Vec2 nuv3 = new Vec2(uv3.x / texWidth,uv3.y / texHeight);
        Vec2 nuv4 = new Vec2(uv4.x / texWidth,uv4.y / texHeight);

        FDVertex vertex1 = new FDVertex(nuv1.x ,nuv1.y ,v1);
        FDVertex vertex2 = new FDVertex(nuv2.x ,nuv2.y ,v2);
        FDVertex vertex3 = new FDVertex(nuv3.x ,nuv3.y,v3);
        FDVertex vertex4 = new FDVertex(nuv4.x ,nuv4.y,v4);
        return new FDFace(normal,vertex4,vertex3,vertex2,vertex1);
    }

    private static FDFace createFace(Vector3f v1,Vector3f v2,Vector3f v3,Vector3f v4,Vector3f normal,JsonObject faceData,int texWidth,int texHeight){
        Vec2 uv = JsonHelper.parseVec2(faceData.getAsJsonArray("uv"));
        Vec2 uv_size = JsonHelper.parseVec2(faceData.getAsJsonArray("uv_size"));
        uv = new Vec2(uv.x / texWidth,uv.y / texHeight);
        uv_size = new Vec2(uv_size.x / texWidth,uv_size.y / texHeight);

        FDVertex vertex1 = new FDVertex(uv.x + uv_size.x ,uv.y + uv_size.y ,v1);
        FDVertex vertex2 = new FDVertex(uv.x ,uv.y + uv_size.y ,v2);
        FDVertex vertex3 = new FDVertex(uv.x ,uv.y,v3);
        FDVertex vertex4 = new FDVertex(uv.x + uv_size.x,uv.y,v4);
        return new FDFace(normal,vertex4,vertex3,vertex2,vertex1);
    }



    private static Vector3f mul(Vector3f v,Matrix4f matrix4f){
        Vector4f vc = fromVec3ToVec4(v).mul(matrix4f);
        return new Vector3f(vc.x,vc.y,vc.z);
    }
    private static Vector4f fromVec3ToVec4(Vector3f v){
        return new Vector4f((float)v.x,(float)v.y,(float)v.z,1.0f);
    }

}
