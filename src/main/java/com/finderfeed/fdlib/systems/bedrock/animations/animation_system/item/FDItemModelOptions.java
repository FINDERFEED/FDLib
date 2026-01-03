package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.interfaces.FDItemColor;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.interfaces.FDItemRenderType;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.client.renderer.RenderType;

import java.util.function.Supplier;

public class FDItemModelOptions {

    private Supplier<FDModelInfo> modelInfo;
    private FDItemRenderType renderType;
    private FDItemColor fdItemColor;

    private FDItemModelOptions(Builder builder) {
        this.modelInfo = builder.modelInfo;
        this.renderType = builder.renderType;
        this.fdItemColor = builder.fdItemColor;
    }

    public Supplier<FDModelInfo> getModelInfo() {
        return modelInfo;
    }

    public FDItemRenderType getRenderType() {
        return renderType;
    }

    public FDItemColor getFdItemColor() {
        return fdItemColor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Supplier<FDModelInfo> modelInfo;
        private FDItemRenderType renderType;
        private FDItemColor fdItemColor = ((ctx, itemStack) -> new FDColor(1,1,1,1));

        private Builder() {}

        public Builder modelInfo(Supplier<FDModelInfo> modelInfo) {
            this.modelInfo = modelInfo;
            return this;
        }

        public Builder renderType(FDItemRenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public Builder renderType(RenderType renderType) {
            this.renderType = ((context, itemStack) -> renderType);
            return this;
        }

        public Builder itemColor(FDItemColor fdItemColor) {
            this.fdItemColor = fdItemColor;
            return this;
        }

        public Builder itemColor(FDColor color) {
            this.fdItemColor = ((ctx, itemStack) -> color);
            return this;
        }

        public FDItemModelOptions build() {
            return new FDItemModelOptions(this);
        }
    }
}
