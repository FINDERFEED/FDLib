package com.finderfeed.fdlib.util.rendering;

import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.particle.FDParticleRenderType;
import com.finderfeed.fdlib.systems.shapes.FD2DShape;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.opengl.GL11;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class FDRenderUtil {

    public static void renderCenteredScaledItemStack(GuiGraphics graphics, float x, float y, float scale, ItemStack itemStack){
        Matrix4fStack stack = RenderSystem.getModelViewStack();

        stack.pushMatrix();

        float mfx = x / scale;
        float mfy = y / scale;

        stack.scale(scale,scale,1);
        stack.translate(mfx - 8,mfy - 8,0);

        RenderSystem.applyModelViewMatrix();
        graphics.renderItem(itemStack, 0,0);
        stack.popMatrix();
        RenderSystem.applyModelViewMatrix();
    }

    public static void renderScaledItemStack(GuiGraphics graphics, float x, float y, float scale, ItemStack itemStack){
        Matrix4fStack stack = RenderSystem.getModelViewStack();

        stack.pushMatrix();

        float mfx = x / scale;
        float mfy = y / scale;

        stack.scale(scale,scale,1);
        stack.translate(mfx,mfy,0);

        RenderSystem.applyModelViewMatrix();
        graphics.renderItem(itemStack, 0,0);
        stack.popMatrix();
        RenderSystem.applyModelViewMatrix();
    }

    public static float tryGetPartialTickIgnorePause(){
        DeltaTracker tracker = Minecraft.getInstance().getTimer();
        if (tracker instanceof DeltaTracker.Timer timer){
            return timer.deltaTickResidual;
        }else{
            return tracker.getGameTimeDeltaPartialTick(true);
        }
    }

    public static float getPartialTickWithPause(){
        DeltaTracker tracker = Minecraft.getInstance().getTimer();
        return tracker.getGameTimeDeltaPartialTick(true);
    }

    public static void renderFDModelInScreen(PoseStack matrices, FDModel model, float x, float y, float rotX, float rotY, float rotZ, float scale,int light, int overlay, float r,float g, float b,float a, RenderType renderType){
        matrices.pushPose();

        VertexConsumer builder = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType);

        matrices.translate(x,y,0);

        matrices.mulPose(new Quaternionf().rotationXYZ(rotX,rotY,rotZ));

        matrices.scale(-scale,-scale,-scale);

        Lighting.setupForEntityInInventory();

        model.render(matrices,builder,light, overlay,r,g,b,a);

        Minecraft.getInstance().renderBuffers().bufferSource().endLastBatch();

        Lighting.setupFor3DItems();

        matrices.popPose();
    }


    public static void renderFDModelInScreen(PoseStack matrices, FDModel model, float x, float y, float rotX, float rotY, float rotZ, float scale,int light, int overlay, RenderType renderType){
        renderFDModelInScreen(matrices,model,x,y,rotX,rotY,rotZ,scale,light,overlay,1f,1f,1f,1f,renderType);
    }

    public static void renderFDModelInScreen(PoseStack matrices, FDModel model, float x, float y, float rotX, float rotY, float rotZ, float scale, RenderType renderType){
        renderFDModelInScreen(matrices,model,x,y,rotX,rotY,rotZ,scale,LightTexture.FULL_BRIGHT,OverlayTexture.NO_OVERLAY,renderType);
    }

    public static void renderCenteredText(GuiGraphics graphics,float x,float y,float textScale,boolean drawShadow,String s,int color){
        Font font = Minecraft.getInstance().font;
        float w = font.width(s) * textScale;
        renderScaledText(graphics,s,x - w/2,y,textScale,drawShadow,color);
    }

    public static void renderFullyCenteredText(GuiGraphics graphics,float x,float y,int width,float textScale,boolean drawShadow,int color,Component component){
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> sequences = font.split(component,width);
        float ySize = sequences.size() * font.lineHeight * textScale;
        y -= ySize / 2;
        for (int i = 0; i < sequences.size();i++){
            var sequence = sequences.get(i);
            float xp = x - font.width(sequence) / 2f * textScale;
            float yp = y + i * font.lineHeight * textScale;
            renderScaledText(graphics,sequence,xp,yp,textScale,drawShadow,color);
        }
    }


    public static void applyMovementMatrixRotations(PoseStack matrices, Vec3 vec){
        double angleY = Math.toDegrees(Math.atan2(vec.x,vec.z));
        double angleX = Math.toDegrees(Math.atan2(Math.sqrt(vec.x*vec.x + vec.z*vec.z),vec.y));
        matrices.mulPose(rotationDegrees(YP(),(float)angleY));
        matrices.mulPose(rotationDegrees(XP(),(float)angleX));
    }

    public static void applyMovementMatrixRotations(Matrix4f matrices, Vec3 vec){
        double angleY = Math.atan2(vec.x,vec.z);
        double angleX = Math.atan2(Math.sqrt(vec.x*vec.x + vec.z*vec.z),vec.y);
        matrices.rotateY((float)angleY);
        matrices.rotateX((float)angleX);
    }


    public static void scissor(float x,float y, float width,float boxY){
        Window window = Minecraft.getInstance().getWindow();
        double scale = window.getGuiScale();
        int nx = (int) Math.round(x*scale);
        int ny = (int) Math.round(window.getHeight() - y*scale);
        int nBX = (int) Math.round(width * scale);
        int nBY = (int) Math.round(boxY * scale);
        RenderSystem.enableScissor(nx,ny - nBY,nBX,nBY);
    }

    /**
     * Doesn't remove the character.
     */
    public static List<String> splitStringByCharacter(String s,char c){
        List<String> strings = new ArrayList<>();
        int sBegin = 0;
        for (int i = 0; i < s.length();i++){
            char ch = s.charAt(i);
            if (ch == c){
                strings.add(s.substring(sBegin,i + 1));
                sBegin = i + 1;
            }
        }
        return strings;
    }

    public static void fill(PoseStack matrices,float x,float y,float xw,float yw,float r,float g,float b,float a){
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.POSITION_COLOR);
        Matrix4f m = matrices.last().pose();

        builder.addVertex(m,x,y + yw,0).setColor(r,g,b,a);
        builder.addVertex(m,x + xw,y + yw,0).setColor(r,g,b,a);
        builder.addVertex(m,x + xw,y,0).setColor(r,g,b,a);
        builder.addVertex(m,x,y,0).setColor(r,g,b,a);


        BufferUploader.drawWithShader(builder.build());
        RenderSystem.disableBlend();
    }

    public static void fill(PoseStack matrices,float x,float y,float xw,float yw,
                            float r1, float g1, float b1, float a1,
                            float r2, float g2, float b2, float a2,
                            float r3, float g3, float b3, float a3,
                            float r4, float g4, float b4, float a4
    ){
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.POSITION_COLOR);
        Matrix4f m = matrices.last().pose();

        builder.addVertex(m,x,y + yw,0).setColor(r4,g4,b4,a4);
        builder.addVertex(m,x + xw,y + yw,0).setColor(r3,g3,b3,a3);
        builder.addVertex(m,x + xw,y,0).setColor(r2,g2,b2,a2);
        builder.addVertex(m,x,y,0).setColor(r1,g1,b1,a1);


        BufferUploader.drawWithShader(builder.build());
        RenderSystem.disableBlend();
    }

    public static void renderShader(PoseStack matrices,float x,float y,float xw,float yw,float r,float g,float b,float a,float xuvStretch,float yuvStretch){
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        RenderSystem.enableBlend();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.POSITION_TEX_COLOR);
        Matrix4f m = matrices.last().pose();

        float xuvs = 0;
        float xuvend = xuvStretch;

        float yuvs = 0;
        float yuvend = yuvStretch;

        builder.addVertex(m,x,y + yw,0).setUv(xuvs,yuvend).setColor(r,g,b,a);
        builder.addVertex(m,x + xw,y + yw,0).setUv(xuvend,yuvend).setColor(r,g,b,a);
        builder.addVertex(m,x + xw,y,0).setUv(xuvend,yuvs).setColor(r,g,b,a);
        builder.addVertex(m,x,y,0).setUv(xuvs,yuvs).setColor(r,g,b,a);


        BufferUploader.drawWithShader(builder.build());

        RenderSystem.disableBlend();
    }



    public static void fill(RenderType type,PoseStack matrices, float x, float y, float xw, float yw, float r, float g, float b, float a){
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.enableBlend();
        VertexConsumer builder = source.getBuffer(type);
        Matrix4f m = matrices.last().pose();

        builder.addVertex(m,x,y + yw,0).setColor(r,g,b,a);
        builder.addVertex(m,x + xw,y + yw,0).setColor(r,g,b,a);
        builder.addVertex(m,x + xw,y,0).setColor(r,g,b,a);
        builder.addVertex(m,x,y,0).setColor(r,g,b,a);

        source.endBatch(type);

        RenderSystem.disableBlend();
    }


    public static void fill(RenderType type,PoseStack matrices, float x, float y, float xw, float yw,
                            float r1, float g1, float b1, float a1,
                            float r2, float g2, float b2, float a2,
                            float r3, float g3, float b3, float a3,
                            float r4, float g4, float b4, float a4
    ){
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.enableBlend();
        VertexConsumer builder = source.getBuffer(type);
        Matrix4f m = matrices.last().pose();

        builder.addVertex(m,x,y + yw,0).setColor(r4,g4,b4,a4);
        builder.addVertex(m,x + xw,y + yw,0).setColor(r3,g3,b3,a3);
        builder.addVertex(m,x + xw,y,0).setColor(r2,g2,b2,a2);
        builder.addVertex(m,x,y,0).setColor(r1,g1,b1,a1);

        source.endBatch(type);

        RenderSystem.disableBlend();
    }

    public static void renderScrollBar(PoseStack matrices,float x,float y,float scrollBarWidth,float scrollBarHeight,float scrollValue,float maxScrollValue,
                                       float r,float g,float b,float a, float scrollerR,float scrollerG,float scrollerB,float scrollerA){
        fill(matrices,x,y,scrollBarWidth,scrollBarHeight,r,g,b,a);

        float factor = (maxScrollValue + scrollBarHeight) / (float) scrollBarHeight;
        float scrollBarYSize = scrollBarHeight / factor;
        float scrollBarYPos = scrollValue / factor;

        fill(matrices,x,y + scrollBarYPos,scrollBarWidth,scrollBarYSize,scrollerR,scrollerG,scrollerB,scrollerA);

    }

    public static void renderScaledText(GuiGraphics graphics,String text, float x, float y, float scale,boolean drawShadow,int color){
        if (scale != 0) {
            Font font = Minecraft.getInstance().font;
            PoseStack matrices = graphics.pose();
            matrices.pushPose();

            matrices.scale(scale, scale, 1);
            x *= 1 / scale;
            y *= 1 / scale;
            graphics.drawString(font,text,x,y,color,drawShadow);

            matrices.popPose();
        }
    }

    public static void renderScaledText(GuiGraphics graphics, Component text, float x, float y, float scale, boolean drawShadow, int color){
        if (scale != 0) {
            Font font = Minecraft.getInstance().font;
            PoseStack matrices = graphics.pose();
            matrices.pushPose();

            matrices.scale(scale, scale, 1);
            x *= 1 / scale;
            y *= 1 / scale;
            matrices.translate(x,y,0);
            graphics.drawString(font,text,0,0,color,drawShadow);

            matrices.popPose();
        }
    }

    public static void renderScaledText(GuiGraphics graphics, FormattedCharSequence text, float x, float y, float scale, boolean drawShadow, int color){
        if (scale != 0) {
            Font font = Minecraft.getInstance().font;
            PoseStack matrices = graphics.pose();
            matrices.pushPose();

            matrices.scale(scale, scale, 1);
            x *= 1 / scale;
            y *= 1 / scale;
            matrices.translate(x,y,0);
            graphics.drawString(font,text,0,0,color,drawShadow);

            matrices.popPose();
        }
    }

    public static void blitWithBlend(PoseStack matrices, float x, float y,float width, float height, float texPosX, float texPosY,float renderAmountX,float renderAmountY, float texWidth, float texHeight, float zOffset, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        float u1 = texPosX / (float) texWidth;
        float u2 = (texPosX + renderAmountX) / (float) texWidth;
        float v1 = texPosY / (float) texHeight;
        float v2 = (texPosY + renderAmountY) / (float) texHeight;
        Matrix4f m = matrices.last().pose();
        BufferBuilder vertex = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        vertex.addVertex(m, x, y, zOffset).setUv(u1, v1).setColor(1, 1, 1, alpha);
        vertex.addVertex(m, x, y + height, zOffset).setUv(u1, v2).setColor(1, 1, 1, alpha);
        vertex.addVertex(m, x + width, y + height, zOffset).setUv(u2, v2).setColor(1, 1, 1, alpha);
        vertex.addVertex(m, x + width, y, zOffset).setUv(u2, v1).setColor(1, 1, 1, alpha);


        BufferUploader.drawWithShader(vertex.build());
        RenderSystem.disableBlend();
    }

    public static void blitWithBlendCentered(PoseStack matrices, float x, float y,float width, float height, float texPosX, float texPosY,float renderAmountX,float renderAmountY, float texWidth, float texHeight, float zOffset, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        float u1 = texPosX / (float) texWidth;
        float u2 = (texPosX + renderAmountX) / (float) texWidth;
        float v1 = texPosY / (float) texHeight;
        float v2 = (texPosY + renderAmountY) / (float) texHeight;
        Matrix4f m = matrices.last().pose();
        BufferBuilder vertex = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        vertex.addVertex(m, x - width / 2, y - height / 2, zOffset).setUv(u1, v1).setColor(1, 1, 1, alpha);
        vertex.addVertex(m, x - width / 2, y + height / 2, zOffset).setUv(u1, v2).setColor(1, 1, 1, alpha);
        vertex.addVertex(m, x + width / 2, y + height / 2, zOffset).setUv(u2, v2).setColor(1, 1, 1, alpha);
        vertex.addVertex(m, x + width / 2, y - height / 2, zOffset).setUv(u2, v1).setColor(1, 1, 1, alpha);


        BufferUploader.drawWithShader(vertex.build());
        RenderSystem.disableBlend();
    }

    public static void blitWithBlendRgb(PoseStack matrices, float x, float y,float width, float height, float texPosX, float texPosY,float renderAmountX,float renderAmountY, float texWidth, float texHeight, float zOffset, float alpha,float r,float g,float b) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        float u1 = texPosX / (float) texWidth;
        float u2 = (texPosX + renderAmountX) / (float) texWidth;
        float v1 = texPosY / (float) texHeight;
        float v2 = (texPosY + renderAmountY) / (float) texHeight;
        Matrix4f m = matrices.last().pose();
        BufferBuilder vertex = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        vertex.addVertex(m, x, y, zOffset).setUv(u1, v1).setColor(r, g, b, alpha);
        vertex.addVertex(m, x, y + height, zOffset).setUv(u1, v2).setColor(r, g, b, alpha);
        vertex.addVertex(m, x + width, y + height, zOffset).setUv(u2, v2).setColor(r,g ,b , alpha);
        vertex.addVertex(m, x + width, y, zOffset).setUv(u2, v1).setColor(r, g, b, alpha);


        BufferUploader.drawWithShader(vertex.build());
        RenderSystem.disableBlend();
    }

    public static void bindTexture(ResourceLocation location){
        RenderSystem.setShaderTexture(0,location);
    }

    public static boolean isMouseInBounds(float mx,float my, float x,float y,float wx,float wy){
        return mx >= x && mx <= x + wx
                &&
                my >= y && my <= y + wy;
    }


    public static Vector3f YP(){
        return new Vector3f(0,1,0);
    }
    public static Vector3f YN(){
        return new Vector3f(0,-1,0);
    }
    public static Vector3f XP(){
        return new Vector3f(1,0,0);
    }
    public static Vector3f XN(){
        return new Vector3f(-1,0,0);
    }
    public static Vector3f ZP(){
        return new Vector3f(0,0,1);
    }
    public static Vector3f ZN(){
        return new Vector3f(0,0,-1);
    }

    public static Quaternionf rotationDegrees(Vector3f rotation, float angle){
        return new Quaternionf(new AxisAngle4f((float)Math.toRadians(angle),rotation));
    }



    public record Scissor(float x, float y, float x2, float y2){

        private static Stack<Scissor> activeScissors = new Stack<>();

        public static void pushScissors(float x,float y,float wx,float wy){
            Scissor previnst;
            Window window = Minecraft.getInstance().getWindow();
            if (activeScissors.isEmpty()){
                previnst = new Scissor(0,0,window.getGuiScaledWidth(),window.getGuiScaledHeight());
            }else{
                previnst = activeScissors.peek();
            }
            Scissor instance = createScissors(previnst,x,y,wx,wy);
            activeScissors.push(instance);
            FDRenderUtil.scissor(instance.x,instance.y,instance.x2 - instance.x,instance.y2 - instance.y);
        }

        public static void pushScissors(PoseStack poseStack,float x,float y,float wx,float wy){
            Matrix4f m = poseStack.last().pose();
            Vector3f pos = m.transformPosition(x,y,0,new Vector3f());
            pushScissors(pos.x,pos.y,wx,wy);
        }

        public static void popScissors(){
            if (activeScissors.isEmpty()){
                RenderSystem.disableScissor();
            }else{
                activeScissors.pop();
                if (activeScissors.isEmpty()){
                    RenderSystem.disableScissor();
                }else{
                    Scissor instance = activeScissors.peek();
                    FDRenderUtil.scissor(instance.x,instance.y,instance.x2 - instance.x,instance.y2 - instance.y);
                }
            }
        }

        private static Scissor createScissors(Scissor previousScissors, float x, float y, float wx, float wy){
            float px = previousScissors.x;
            float py = previousScissors.y;
            float pxn = previousScissors.x2;
            float pyn = previousScissors.y2;
            float x2 = x + wx;
            float y2 = y + wy;
            x = Mth.clamp(x,px,pxn);
            y = Mth.clamp(y,py,pyn);
            x2 = Mth.clamp(x2,px,pxn);
            y2 = Mth.clamp(y2,py,pyn);
            return new Scissor(x,y,x2,y2);
        }

    }

    public static class ParticleRenderTypes{
        public static final ParticleRenderType ADDITIVE_TRANSLUCENT = new FDParticleRenderType() {


            @Nullable
            @Override
            public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
                if (Minecraft.useShaderTransparency()){
                    Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
                }
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
                FDRenderUtil.bindTexture(TextureAtlas.LOCATION_PARTICLES);
                return tesselator.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.PARTICLE);
            }

            @Override
            public void end() {
                if (Minecraft.useShaderTransparency()){
                    Minecraft.getInstance().levelRenderer.getParticlesTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
                    Minecraft.getInstance().levelRenderer.getParticlesTarget().bindWrite(false);
                }
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
            }

            @Override
            public String toString() {
                return "solarcraft:additive";
            }
        };

        public static final ParticleRenderType NORMAL_TRANSLUCENT = new FDParticleRenderType() {
            @Override
            public void end() {
                if (Minecraft.useShaderTransparency()){
                    Minecraft.getInstance().levelRenderer.getParticlesTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
                    Minecraft.getInstance().levelRenderer.getParticlesTarget().bindWrite(false);
                }
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
            }

            @Nullable
            @Override
            public BufferBuilder begin(Tesselator tesselator, TextureManager p_107437_) {

                if (Minecraft.useShaderTransparency()){
                    Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
                }
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
                FDRenderUtil.bindTexture(TextureAtlas.LOCATION_PARTICLES);
                return tesselator.begin(VertexFormat.Mode.QUADS,DefaultVertexFormat.PARTICLE);
            }
        };
    }

    /**
     * ParticleRenderTypesScreen. Yeah, strange name but keeps it shorter.
     */
    public static class ParticleRenderTypesS {


        public static final Function<ResourceLocation, FDParticleRenderType> TEXTURES_BLUR_ADDITIVE = Util.memoize((location)->{
            return new FDParticleRenderType() {
                @Override
                public void end() {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                    Minecraft.getInstance().getTextureManager().getTexture(location).restoreLastBlurMipmap();
                }

                @Nullable
                @Override
                public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {


                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

                    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                    RenderSystem.setShaderTexture(0, location);
                    Minecraft.getInstance().getTextureManager().getTexture(location).setBlurMipmap(true,true);

                    return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                }
            };
        });

        public static final Function<ResourceLocation, FDParticleRenderType> TEXTURES_DEFAULT = Util.memoize((location)->{
            return new FDParticleRenderType() {
                @Override
                public void end() {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                }

                @Nullable
                @Override
                public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                    RenderSystem.setShaderTexture(0, location);
                    return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                }
            };
        });

    }

    public static class RenderTypes extends RenderType {

        public static final RenderType LIGHTNING_NO_CULL = create(
                "lightning_no_cull",
                DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setCullState(RenderStateShard.NO_CULL)
                        .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                        .setWriteMaskState(COLOR_DEPTH_WRITE)
                        .setTransparencyState(LIGHTNING_TRANSPARENCY)
                        .setOutputState(WEATHER_TARGET)
                        .createCompositeState(false)
        );

        public RenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
            super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
        }
    }

}
