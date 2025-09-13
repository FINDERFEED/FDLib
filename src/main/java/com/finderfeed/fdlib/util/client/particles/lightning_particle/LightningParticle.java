package com.finderfeed.fdlib.util.client.particles.lightning_particle;

import com.finderfeed.fdlib.systems.particle.FDParticleRenderType;
import com.finderfeed.fdlib.util.FDUtil;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Random;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LightningParticle extends Particle {

    private LightningParticleOptions options;

    private float particleRoll;

    private int seed;

    public LightningParticle(LightningParticleOptions options,ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
        this.x = x;
        this.y = y;
        this.z = z;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.options = options;
        this.lifetime = options.getLifetime();
        this.setSize(options.getQuadSize(), options.getQuadSize());
        if (level != null){
            this.particleRoll = level.random.nextFloat() * 360;
            if (options.getSeed() == -1){
                seed = level.random.nextInt();
            }
        }else{
            this.particleRoll = 0;
        }
        this.hasPhysics = options.hasPhysics();
        options.getProcessor().init(this);
    }

    @Override
    public void tick() {
        options.getProcessor().processParticle(this);
        super.tick();
    }

    @Override
    public void render(VertexConsumer vertex, Camera camera, float pticks) {

        Vec3 lpos = new Vec3(
                FDMathUtil.lerp(this.xo,this.x,pticks),
                FDMathUtil.lerp(this.yo,this.y,pticks),
                FDMathUtil.lerp(this.zo,this.z,pticks)
        );

        Matrix4fStack mat = new Matrix4fStack();

        long time = level.getGameTime();

        Random r = new Random(time + seed);

        Matrix4f rotMat = new Matrix4f();
        var q = camera.rotation();
        q.get(rotMat);

        Vec3 pos = lpos.subtract(camera.getPosition());

        mat.translate((float)pos.x,(float)pos.y,(float)pos.z);
        mat.mul(rotMat);
        mat.rotateZ((float)Math.toRadians(particleRoll + (options.isRandomRoll() ? r.nextInt(360) : 0) ));


        float size = options.getQuadSize();

        Vector3f startPos = new Vector3f(-size/2,0,0);


        int remainingParts = options.getMaxLightningSegments();
        float length = size / remainingParts;

        float width = size / 8f;

        LightningNode main = constructLightningNodes(null, r, startPos, new Vector3f(length, 0, 0), remainingParts, r.nextInt(3) + 2);
        if (main != null) {
            renderLightningNode(mat, main, vertex, width, options.getR()/255f,options.getG()/255f,options.getB()/255f,1f,0);
            renderLightningNode(mat, main, vertex, width / 4, 1f,1f,1f,1f,0.0001f);
        }
    }

    private void renderLightningNode(Matrix4f transform, LightningNode node, VertexConsumer vertex, float lightningWidth, float r, float g, float b, float a, float zOffset){

        Vector3f direction = node.end.sub(node.start,new Vector3f()).normalize();

        Vector3f prev = this.vectorBetweenCurrentAndPrevious(node);
        if (prev.dot(new Vector3f(0,1,0)) < 0)  prev = prev.mul(-1);



        Vector3f cr = direction.cross(prev,new Vector3f());
        float sin1 = cr.length();


        Vector3f currentBetween = this.vectorBetweenCurrentAndNext(node,0);
        if (currentBetween.dot(new Vector3f(0,1,0)) < 0)  currentBetween = currentBetween.mul(-1);

        Vector3f cr2 = direction.cross(currentBetween,new Vector3f());
        float sin2 = cr2.length();

        float whiteWidthMult = 0.25f;
        float widthStart = lightningWidth / sin1;
        float widthEnd = lightningWidth / sin2;

        vertex.vertex(transform, node.start.x - prev.x * widthStart * whiteWidthMult, node.start.y - prev.y * widthStart * whiteWidthMult,zOffset).color(r,g,b,a).endVertex();
        vertex.vertex(transform, node.end.x - currentBetween.x * widthEnd * whiteWidthMult, node.end.y - currentBetween.y * widthEnd * whiteWidthMult,zOffset).color(r,g,b,a).endVertex();
        vertex.vertex(transform, node.end.x + currentBetween.x * widthEnd * whiteWidthMult, node.end.y + currentBetween.y * widthEnd * whiteWidthMult,zOffset).color(r,g,b,a).endVertex();
        vertex.vertex(transform, node.start.x + prev.x * widthStart * whiteWidthMult, node.start.y + prev.y * widthStart * whiteWidthMult,zOffset).color(r,g,b,a).endVertex();

        for (int  i = 0; i < node.branches.size();i++){
            this.renderLightningNode(transform, node.branches.get(i), vertex, lightningWidth,r,g,b,a,zOffset);
        }
    }

    private Vector3f vectorBetweenCurrentAndPrevious(LightningNode node){
        Vector3f currentDir = node.end.sub(node.start,new Vector3f());
        Vector3f lastDir;
        if (node.previous == null){
            return new Vector3f(0,1,0);
        }else{
            lastDir = node.previous.start.sub(node.previous.end, new Vector3f());
        }
        return FDMathUtil.vectorBetweenVectors(currentDir,lastDir);
    }

    private Vector3f vectorBetweenCurrentAndNext(LightningNode node, int id){
        Vector3f currentDir = node.start.sub(node.end,new Vector3f());
        Vector3f lastDir;
        if (node.branches.isEmpty()){
            lastDir = currentDir.normalize(new Vector3f()).rotateZ(FDMathUtil.FPI / 2);
        }else{
            LightningNode nd = node.branches.get(id);
            lastDir = nd.end.sub(nd.start, new Vector3f());
        }
        return FDMathUtil.vectorBetweenVectors(currentDir,lastDir);
    }


    private LightningNode constructLightningNodes(LightningNode previous, Random random, Vector3f startPos, Vector3f direction, int remainingParts, int branches){
        Vector3f endPos = startPos.add(direction,new Vector3f());

        List<LightningNode> b = new ArrayList<>();

        LightningNode node = new LightningNode(previous, startPos,endPos, b);

        if (remainingParts <= 0) return null;

        for (int i = 0; i < branches;i++){

            Vector3f newDir = direction.rotateZ(random.nextFloat() * FDMathUtil.FPI / 1.5f - FDMathUtil.FPI / 3,new Vector3f());

            LightningNode node1 = constructLightningNodes(node, random, endPos, newDir, remainingParts - random.nextInt(1) - 1, random.nextInt(2) + 1);

            if (node1 != null){
                b.add(node1);
            }
        }

        return node;
    }


    @Override
    public ParticleRenderType getRenderType() {
        return RENDER_TYPE;
    }


    public static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {

        @Nullable
        @Override
        public void begin(BufferBuilder tesselator, TextureManager manager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getRendertypeLightningShader);
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        @Override
        public String toString() {
            return "LIGHTNING_RENDER_TYPE";
        }
    };


    public static class Factory implements ParticleProvider<LightningParticleOptions> {
        @Nullable
        @Override
        public Particle createParticle(LightningParticleOptions options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new LightningParticle(options,level,x,y,z,xd,yd,zd);
        }
    }


    public record LightningNode(LightningNode previous, Vector3f start, Vector3f end, List<LightningNode> branches){}

}
