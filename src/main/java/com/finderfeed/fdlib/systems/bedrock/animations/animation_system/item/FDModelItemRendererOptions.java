package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FDModelItemRendererOptions {

    protected List<FDItemModelOptions> fdItemModelOptions = new ArrayList<>();

    protected Function<ItemDisplayContext, Float> rotation = t -> {
        return 0f;
    };
    protected Function<ItemDisplayContext, Float> scale = (t)->{
        return t == ItemDisplayContext.GUI ? 0.6f : 0.35f;
    };
    protected Function<ItemDisplayContext, Vector3f> translation = t -> {
        return new Vector3f();
    };

    public FDModelItemRendererOptions addModel(Supplier<FDModelInfo> info, RenderType renderType){
        this.fdItemModelOptions.add(FDItemModelOptions.builder()
                        .modelInfo(info)
                        .renderType(renderType)
                .build());
        return this;
    }

    public FDModelItemRendererOptions addModel(FDItemModelOptions options){
        this.fdItemModelOptions.add(options);
        return this;
    }

    public FDModelItemRendererOptions addRotation(Function<ItemDisplayContext, Float> yRotation){
        this.rotation = yRotation;
        return this;
    }

    public FDModelItemRendererOptions setScale(Function<ItemDisplayContext, Float> scaling){
        this.scale = scaling;
        return this;
    }

    public FDModelItemRendererOptions addTranslation(Function<ItemDisplayContext, Vector3f> translation){
        this.translation = translation;
        return this;
    }

    public FDModelItemRendererOptions addRotation(float rotation){
        this.rotation = t->rotation;
        return this;
    }

    public FDModelItemRendererOptions setScale(float scale){
        this.scale = t->scale;
        return this;
    }

    public FDModelItemRendererOptions addTranslation(Vector3f translation){
        this.translation = (t)->translation;
        return this;
    }

    public static FDModelItemRendererOptions create(){
        return new FDModelItemRendererOptions();
    }

}
