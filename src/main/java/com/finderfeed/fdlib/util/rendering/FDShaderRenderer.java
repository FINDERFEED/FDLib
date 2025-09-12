package com.finderfeed.fdlib.util.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;

public class FDShaderRenderer {

    private ShaderInstance shaderInstance;
    private GuiGraphics graphics;
    private float x;
    private float y;
    private float z;
    private float xw;
    private float yw;
    private float[] color = {
            1,1,1,1,
            1,1,1,1,
            1,1,1,1,
            1,1,1,1
    };
    private boolean centered = false;
    private float xUvSpan = 1;
    private float yUvSpan = 1;

    private FDShaderRenderer(){}

    public static FDShaderRenderer start(GuiGraphics graphics,ShaderInstance shader){
        FDShaderRenderer shaderRenderer = new FDShaderRenderer();
        shaderRenderer.graphics = graphics;
        shaderRenderer.shaderInstance = shader;
        return shaderRenderer;
    }

    public FDShaderRenderer setShaderUniform(String name,float... values){
        this.shaderInstance.safeGetUniform(name).set(values);
        return this;
    }

    public FDShaderRenderer setCentered(boolean centered){
        this.centered = centered;
        return this;
    }

    public FDShaderRenderer setUVSpan(float xSpan,float ySpan){
        this.xUvSpan = xSpan;
        this.yUvSpan = ySpan;
        return this;
    }

    public FDShaderRenderer position(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public FDShaderRenderer setResolution(float xw,float yw){
        this.xw = xw;
        this.yw = yw;
        return this;
    }

    public FDShaderRenderer setColor(float r,float g,float b,float a){
        for (int i = 0; i < 16;i+=4){
            this.color[i] = r;
            this.color[i + 1] = g;
            this.color[i + 2] = b;
            this.color[i + 3] = a;
        }
        return this;
    }

    public FDShaderRenderer setColor(float r,float g,float b,float a,int index){ // index 0 - up left, 1 - up right, 2 - down right, 3 - down left
        this.color[index * 4] = r;
        this.color[1 + index * 4] = g;
        this.color[2 + index * 4] = b;
        this.color[3 + index * 4] = a;
        return this;
    }



    public FDShaderRenderer setDownColor(float r,float g,float b,float a){
        return this.setDownLeftColor(r,g,b,a).setDownRightColor(r,g,b,a);
    }
    public FDShaderRenderer setUpColor(float r,float g,float b,float a){
        return this.setUpLeftColor(r,g,b,a).setUpRightColor(r,g,b,a);
    }


    public FDShaderRenderer setRightColor(float r,float g,float b,float a){
        return this.setUpRightColor(r,g,b,a).setDownRightColor(r,g,b,a);
    }
    public FDShaderRenderer setLeftColor(float r,float g,float b,float a){
        return this.setUpLeftColor(r,g,b,a).setDownLeftColor(r,g,b,a);
    }

    public FDShaderRenderer setUpLeftColor(float r,float g,float b,float a){
        return this.setColor(r,g,b,a,0);
    }

    public FDShaderRenderer setUpRightColor(float r,float g,float b,float a){
        return this.setColor(r,g,b,a,1);
    }

    public FDShaderRenderer setDownRightColor(float r,float g,float b,float a){
        return this.setColor(r,g,b,a,2);
    }

    public FDShaderRenderer setDownLeftColor(float r,float g,float b,float a){
        return this.setColor(r,g,b,a,3);
    }


    public void end(){
        VertexFormat format = shaderInstance.getVertexFormat();

        RenderSystem.setShader(()->shaderInstance);

        Tesselator tesselator = Tesselator.getInstance();
        

        BufferBuilder vertex = tesselator.getBuilder();
        vertex.begin(VertexFormat.Mode.QUADS,format);

        float xs = centered ? x - xw / 2 : x;
        float ys = centered ? y - yw / 2 : y;

        float xe = centered ? x + xw / 2 : x + xw;
        float ye = centered ? y + yw / 2 : y + yw;

        PoseStack matrix = graphics.pose();

        Matrix4f matrix4f = matrix.last().pose();

        RenderSystem.enableBlend();

        if (format == DefaultVertexFormat.POSITION_TEX_COLOR){


            vertex.vertex(matrix4f,xs,ye,z).uv(0,yUvSpan).color(color[12],color[13],color[14],color[15]).endVertex();
            vertex.vertex(matrix4f,xe,ye,z).uv(xUvSpan,yUvSpan).color(color[8],color[9],color[10],color[11]).endVertex();
            vertex.vertex(matrix4f,xe,ys,z).uv(xUvSpan,0).color(color[4],color[5],color[6],color[7]).endVertex();
            vertex.vertex(matrix4f,xs,ys,z).uv(0,0).color(color[0],color[1],color[2],color[3]).endVertex();


        }else if (format == DefaultVertexFormat.POSITION_COLOR){

            vertex.vertex(matrix4f,xs,ye,z).color(color[12],color[13],color[14],color[15]).endVertex();
            vertex.vertex(matrix4f,xe,ye,z).color(color[8],color[9],color[10],color[11]).endVertex();
            vertex.vertex(matrix4f,xe,ys,z).color(color[4],color[5],color[6],color[7]).endVertex();
            vertex.vertex(matrix4f,xs,ys,z).color(color[0],color[1],color[2],color[3]).endVertex();

        }else if (format == DefaultVertexFormat.POSITION){

            vertex.vertex(matrix4f,xs,ye,z).endVertex();
            vertex.vertex(matrix4f,xe,ye,z).endVertex();
            vertex.vertex(matrix4f,xe,ys,z).endVertex();
            vertex.vertex(matrix4f,xs,ys,z).endVertex();

        }else{
            throw new RuntimeException("Shader vertex format is not supported by FDShaderRenderer");
        }

        BufferUploader.drawWithShader(vertex.end());

    }



}
