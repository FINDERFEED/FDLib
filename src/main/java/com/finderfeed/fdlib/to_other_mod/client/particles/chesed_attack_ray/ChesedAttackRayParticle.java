package com.finderfeed.fdlib.to_other_mod.client.particles.chesed_attack_ray;

import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningParticle;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class ChesedAttackRayParticle extends Particle {

    public Vec3 rayEnd = new Vec3(0,0,0);

    public ChesedAttackRayParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);


        this.x = x;
        this.y = y;
        this.z = z;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.rayEnd = new Vec3(
                x + 0,
                y + 10,
                z + 0
        );
        this.setBoundingBox(new AABB(
                x,y,z,
                rayEnd.x,rayEnd.y,rayEnd.z
        ));


        this.lifetime = 60;
    }

    @Override
    public void render(VertexConsumer vertex, Camera camera, float pticks) {
        Vec3 pos = new Vec3(
                Mth.lerp(pticks,this.xo,this.x),
                Mth.lerp(pticks,this.yo,this.y),
                Mth.lerp(pticks,this.zo,this.z)
        ).subtract(camera.getPosition());
        Vec3 b = rayEnd.subtract(x,y,z);
        double len = b.length();

        Matrix4f mat = new Matrix4f();
        mat.translate((float)pos.x,(float)pos.y,(float)pos.z);
        FDRenderUtil.applyMovementMatrixRotations(mat,b);





        Vec3 n = FDMathUtil.getNormalVectorFromLineToPoint(pos,b.add(pos),Vec3.ZERO);


        Matrix4f mt2 = new Matrix4f();
        FDRenderUtil.applyMovementMatrixRotations(mt2,b);
        Vector4f up = new Vector4f(0,0,1,1); mt2.transform(up);
        Vector4f left = new Vector4f(1,0,0,1); mt2.transform(left);
        Vec3 vup = new Vec3(up.x / up.w,up.y / up.w,up.z / up.w);
        Vec3 vleft = new Vec3(left.x,left.y,left.z);


        float angle = (float) FDMathUtil.angleBetweenVectors(n,vup);

        if (vleft.dot(n) > 0) {
            mat.rotateY(angle);
        }else{
            mat.rotateY(-angle);
        }



        float w = 1;
        vertex.addVertex(mat,0,0,0).setColor(1f,1f,1f,1f);
        vertex.addVertex(mat,w,0,0).setColor(1,1,1,0f);
        vertex.addVertex(mat,w,(float)len,0).setColor(1,1,1,0f);
        vertex.addVertex(mat,0,(float)len,0).setColor(1f,1f,1f,1f);

        vertex.addVertex(mat,0,0,0).setColor(1f,1f,1f,1f);
        vertex.addVertex(mat,-w,0,0).setColor(1,1,1,0f);
        vertex.addVertex(mat,-w,(float)len,0).setColor(1,1,1,0f);
        vertex.addVertex(mat,0,(float)len,0).setColor(1f,1f,1f,1f);



        var positions = List.of(
                Vec3.ZERO,new Vec3(0,len,0)
        );
        var path = ArcLightningParticle.buildPath(level,w * 2,34234234,Math.max((int)Math.round(len / 2),2), positions);
        mat.translate(0,0,0.001f);
        ArcLightningParticle.drawLightning(mat,vertex,path,positions,0.25f,1f,0,0);

        mat.translate(0,0,0.001f);
        ArcLightningParticle.drawLightning(mat,vertex,path,positions,0.25f * 0.15f,1f,1,1f);
        mat.translate(0,0,-0.003f);
        ArcLightningParticle.drawLightning(mat,vertex,path,positions,0.25f * 0.15f,1f,1,1f);



    }

    @Override
    public ParticleRenderType getRenderType() {
        return ArcLightningParticle.RENDER_TYPE;
    }


    public static class Factory implements ParticleProvider<SimpleParticleType>{

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new ChesedAttackRayParticle(level,x,y,z,xd,yd,zd);
        }
    }

}
