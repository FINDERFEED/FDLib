package com.finderfeed.fdlib.to_other_mod.client.particles;

import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class ArcLightningParticle extends Particle {

    private float lightningWidth;
    private Vec3 endPos;

    public ArcLightningParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd,
                                float lightningWidth,Vec3 endPos) {
        super(level, x, y, z, xd, yd, zd);
        this.x = x;
        this.y = y;
        this.z = z;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lightningWidth = lightningWidth;
        this.endPos = endPos;
        this.lifetime = 60;
        this.setBoundingBox(this.getBoundingBox().inflate(1000));
    }

    @Override
    public void render(VertexConsumer vertex, Camera camera, float partialTicks) {

        Vec3 pos = new Vec3(
                Mth.lerp(partialTicks,this.xo,this.x),
                Mth.lerp(partialTicks,this.yo,this.y),
                Mth.lerp(partialTicks,this.zo,this.z)
        ).subtract(camera.getPosition());


        Vec3 between = endPos.subtract(camera.getPosition()).subtract(pos);

        double horLen = Math.sqrt(between.x * between.x + between.z * between.z);
        double verticalLength = between.y;

        Vec3[] positions = new Vec3[]{
                Vec3.ZERO,
                null,null,
                new Vec3(horLen,verticalLength,0)
        };
        if (horLen > Math.abs(verticalLength)){
            positions[1] = new Vec3(horLen / 2,horLen + verticalLength,0);
            positions[2] = new Vec3(horLen,horLen / 2 + verticalLength,0);
        }else{
            positions[1] = new Vec3(horLen / 2,0,0);
            positions[2] = new Vec3(horLen,verticalLength / 2,0);
        }

        int lightningCounts = 7;
        var path = this.buildPath(lightningCounts,positions);

        Matrix4f mat = new Matrix4f();
        mat.translate(
                (float)pos.x,
                (float)pos.y,
                (float)pos.z
        );
        mat.rotateY(-(float)Math.atan2(between.z,between.x));
        this.drawLightning(mat,vertex,Vec3.ZERO,path,positions,lightningWidth,1f,0,0f);

        mat.translate(0,0,0.001f);
        this.drawLightning(mat,vertex,Vec3.ZERO,path,positions,lightningWidth * 0.15f,1f,1,1f);
        mat.translate(0,0,-0.002f);
        this.drawLightning(mat,vertex,Vec3.ZERO,path,positions,lightningWidth * 0.15f,1f,1,1f);

    }


    private void drawLightning(Matrix4f transform,VertexConsumer vertex,Vec3 pos,List<Vec3> path,Vec3[] positions,float lightningWidth,float r,float g,float b){
        Vec3 previousCenteredVector = new Vec3(0,1,0);
        Vec3 prevPoint = null;
        double previousw = 0;

        for (int i = 1; i < path.size() - 1;i++){
            Vec3 p1 = path.get(i - 1);
            Vec3 p2 = path.get(i);
            Vec3 p3 = path.get(i + 1);
            Vec3 v1 = p2.subtract(p1);
            Vec3 v2 = p3.subtract(p2);
            double dot = v1.reverse().dot(v2);
            double angle = Math.acos(dot / (v1.length() * v2.length()));
            double sin;
            if (angle != 0){
                sin = Math.sin(angle / 2);
            }else{
                sin = 1;
            }


            Vec3 v = this.findCenteredVector(v1,v2);
            if (v.dot(previousCenteredVector) < 0){
                v = v.reverse();
            }

            double w = lightningWidth / sin;

            vertex.addVertex(transform,
                    (float) (p1.x),
                    (float) (p1.y),
                    0
            ).setColor(r,g,b,1f);
            vertex.addVertex(transform,
                    (float) (p1.x + previousCenteredVector.x * previousw),
                    (float) (p1.y + previousCenteredVector.y * previousw),
                    0
            ).setColor(r,g,b,0f);
            vertex.addVertex(transform,
                    (float) (p2.x + v.x * w),
                    (float) (p2.y + v.y * w),
                    0
            ).setColor(r,g,b,0f);
            vertex.addVertex(transform,
                    (float) (p2.x),
                    (float) (p2.y),
                    0
            ).setColor(r,g,b,1f);

            vertex.addVertex(transform,
                    (float) (p1.x),
                    (float) (p1.y),
                    0
            ).setColor(r,g,b,1f);
            vertex.addVertex(transform,
                    (float) (p1.x - previousCenteredVector.x * previousw),
                    (float) (p1.y - previousCenteredVector.y * previousw),
                    0
            ).setColor(r,g,b,0f);
            vertex.addVertex(transform,
                    (float) (p2.x - v.x * w),
                    (float) (p2.y - v.y * w),
                    0
            ).setColor(r,g,b,0f);
            vertex.addVertex(transform,
                    (float) (p2.x),
                    (float) (p2.y),
                    0
            ).setColor(r,g,b,1f);

            prevPoint = p2;
            previousw = w;
            previousCenteredVector = v;
        }


        Vec3 lastPos = positions[positions.length - 1];


        vertex.addVertex(transform,
                (float) (prevPoint.x),
                (float) (prevPoint.y),
                0
        ).setColor(r,g,b,1f);
        vertex.addVertex(transform,
                (float) (prevPoint.x + previousCenteredVector.x * previousw),
                (float) (prevPoint.y + previousCenteredVector.y * previousw),
                0
        ).setColor(r,g,b,0f);
        vertex.addVertex(transform,
                (float) (lastPos.x),
                (float) (lastPos.y),
                0
        ).setColor(r,g,b,0f);
        vertex.addVertex(transform,
                (float) (lastPos.x),
                (float) (lastPos.y),
                0
        ).setColor(r,g,b,1f);

        vertex.addVertex(transform,
                (float) (prevPoint.x),
                (float) (prevPoint.y),
                0
        ).setColor(r,g,b,1f);
        vertex.addVertex(transform,
                (float) (prevPoint.x - previousCenteredVector.x * previousw),
                (float) (prevPoint.y - previousCenteredVector.y * previousw),
                0
        ).setColor(r,g,b,0f);
        vertex.addVertex(transform,
                (float) (lastPos.x),
                (float) (lastPos.y),
                0
        ).setColor(r,g,b,0f);
        vertex.addVertex(transform,
                (float) (lastPos.x),
                (float) (lastPos.y),
                0
        ).setColor(r,g,b,1f);


    }

    private List<Vec3> buildPath(int lightningCounts,Vec3[] positions){

        float step = 1f / lightningCounts;

        Random r = new Random(level.getGameTime()/2 * 233232);

        List<Vec3> path = new ArrayList<>();
        path.add(Vec3.ZERO);
        float lightningSpread = .5f;

        for (float i = step; i <= 1 - step/2; i += step){
            float gl = i * (positions.length - 1);
            float lp = gl - (int) gl;
            Vec3 current = positions[(int)gl];
            Vec3 next = positions[(int)gl + 1];

            Vec3 b = next.subtract(current);
            Vec3 nb = b.normalize();
            float rmod = r.nextFloat() * lightningSpread * 2 - lightningSpread;
            Vec3 point = current.add(b.multiply(lp,lp,0))
                    .add(nb.zRot((float)Math.PI / 2).multiply(rmod,rmod,rmod));
            path.add(point);
        }
        path.add(positions[positions.length - 1]);
        return path;
    }


    private Vec3 getDirectionFromPositions(Vec3[] positions, int index){
        if (index >= positions.length - 1){
            return positions[positions.length - 1].subtract(positions[positions.length - 2]);
        }else if (index < 0){
            return positions[1].subtract(positions[0]);
        }
        return positions[index + 1].subtract(positions[index]);

    }

    private Vec3 findCenteredVector(Vec3 v1, Vec3 v2){
        v1 = v1.reverse().normalize();
        v2 = v2.normalize();
        return v1.add(v2).normalize();
    }




    public static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {
        @Nullable
        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager manager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getRendertypeLightningShader);
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        }

        @Override
        public boolean isTranslucent() {
            return true;
        }

        @Override
        public String toString() {
            return "ARC_LIGHTNING_RENDER_TYPE";
        }
    };




    @Override
    public ParticleRenderType getRenderType() {
        return RENDER_TYPE;
    }


    public static class Factory implements ParticleProvider<SimpleParticleType>{

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new ArcLightningParticle(level,x,y,z,xd,yd,zd,0.1f,new Vec3(
               x + 2,
               y - 5,
               z + 2
            ));
        }
    }

}

//    private Vec3[] placeVertices(VertexConsumer vertex,Matrix4f transform,Vec3 v1,Vec3 v2,Vec3 v3,Vec3 prevBelow,Vec3 prevAbove){
//        Vec3 b = v2.subtract(v1);
//        Vec3 b1 = v3.subtract(v2);
//        Vec3 rotated = b.normalize().multiply(lightningWidth,lightningWidth,0).zRot((float) Math.PI / 2);
//        Vec3 rrotated = rotated.reverse();
//        Vec3 rotated1 = b1.normalize().multiply(lightningWidth,lightningWidth,0).zRot((float) Math.PI / 2);
//        Vec3 rrotated1 = rotated1.reverse();
//        Vec3 pb1 = v1.add(rotated);
//        Vec3 pb2 = v2.add(rotated);
//
//        Vec3 pb3 = v2.add(rotated1);
//        Vec3 pb4 = v3.add(rotated1);
//
//        Vec3 pa1 = v1.add(rrotated);
//        Vec3 pa2 = v2.add(rrotated);
//
//        Vec3 pa3 = v2.add(rrotated1);
//        Vec3 pa4 = v3.add(rrotated1);
//        double kb1 = (pb2.y - pb1.y) / (pb2.x - pb1.x);
//        double bb1 = -(pb1.x * pb2.y - pb2.x * pb1.y) / (pb2.x - pb1.x);
//
//        double kb2 = (pb4.y - pb3.y) / (pb4.x - pb3.x);
//        double bb2 = -(pb3.x * pb4.y - pb4.x * pb3.y) / (pb4.x - pb3.x);
//
//        double xb = (bb2 - bb1) / (kb1 - kb2);
//
//        double ka1 = (pa2.y - pa1.y) / (pa2.x - pa1.x);
//        double ba1 = -(pa1.x * pa2.y - pa2.x * pa1.y) / (pa2.x - pa1.x);
//
//        double ka2 = (pa4.y - pa3.y) / (pa4.x - pa3.x);
//        double ba2 = -(pa3.x * pa4.y - pa4.x * pa3.y) / (pa4.x - pa3.x);
//
//        double xa = (ba2 - ba1) / (ka1 - ka2);
//        Vec3 pBelow = new Vec3(
//                xb,
//                kb1 * xb + bb1,
//                0
//        );
//        Vec3 pAbove = new Vec3(
//                xa,
//                ka1 * xa + ba1,
//                0
//        );
//        vertex.addVertex(transform,(float) prevBelow.x,(float) prevBelow.y,0).setColor(1f,1f,1f,1f);
//        vertex.addVertex(transform,(float) pBelow.x,(float) pBelow.y,0).setColor(1f,1f,1f,1f);
//        vertex.addVertex(transform,(float) pAbove.x,(float) pAbove.y,0).setColor(1f,1f,1f,1f);
//        vertex.addVertex(transform,(float) prevAbove.x,(float) prevAbove.y,0).setColor(1f,1f,1f,1f);
//        return new Vec3[]{pBelow,pAbove};
//    }

//        float step = 0.01f;
//
//        Vec3 v1 = catmullromPositions[0];
//        Vec3 v2 = FDMathUtil.catmullRom(catmullromPositions,step);
//        Vec3 v3 = FDMathUtil.catmullRom(catmullromPositions,step * 2);
//        Vec3 prevBelow = new Vec3(0,-lightningWidth,0);
//        Vec3 prevAbove = new Vec3(0,lightningWidth,0);
//        for (float i = step * 3; i <= 1;i += step){
//
//            vertex.addVertex(matrix4f,(float)v1.x,(float)v1.y,0).setColor(1f,0f,0f,1f);
//            vertex.addVertex(matrix4f,(float)v2.x,(float)v2.y,0).setColor(1f,0f,0f,1f);
//
//            vertex.addVertex(matrix4f,(float)v2.x,(float)v2.y,0.1f).setColor(1f,1f,0f,1f);
//            vertex.addVertex(matrix4f,(float)v3.x,(float)v3.y,0.1f).setColor(1f,1f,0f,1f);
//            var vecs = this.placeVertices(vertex,matrix4f,v1,v2,v3,prevBelow,prevAbove);
//            prevBelow = vecs[0];
//            prevAbove = vecs[1];
//            v1 = v2;
//            v2 = v3;
//            v3 = FDMathUtil.catmullRom(catmullromPositions,i);
//        }

//        this.placeVertices(vertex,matrix4f,v1,v2,v3,prevBelow,prevAbove);
