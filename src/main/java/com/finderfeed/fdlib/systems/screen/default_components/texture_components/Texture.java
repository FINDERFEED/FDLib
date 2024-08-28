package com.finderfeed.fdlib.systems.screen.default_components.texture_components;

import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class Texture {

    public ResourceLocation location;
    public float uvStartX;
    public float uvStartY;
    public float renderAmountX;
    public float renderAmountY;
    public float alpha;
    public float red;
    public float green;
    public float blue;


    public void render(GuiGraphics graphics, float x, float y, float width,float height) {
        PoseStack matrices = graphics.pose();
        RenderSystem.setShaderTexture(0,location);
        FDRenderUtil.blitWithBlendRgb(matrices,x,y,
                width,
                height,
                uvStartX,
                uvStartY,
                renderAmountX,
                renderAmountY,
                1,
                1,
                0,
                alpha,
                red,
                green,
                blue
        );
    }
}
