package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.RenderFunction;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityRenderLayerOptions;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class FDBlockEntityRendererBuilder<T extends BlockEntity & AnimatedObject> {

    private List<FDBlockRenderLayerOptions<T>> layers = new ArrayList<>();
    private IShouldBERender<T> shouldRender = (tile,v)->true;
    private IBERenderOffScreen<T> shouldRenderOffScreen = (tile)->false;
    private FDFreeBERenderer<T> freeRender = null;
    private RenderFunction<BlockEntity, AABB> renderBox = null;

    public static <E extends BlockEntity & AnimatedObject> FDBlockEntityRendererBuilder<E> builder(){
        return new FDBlockEntityRendererBuilder<E>();
    }

    public FDBlockEntityRendererBuilder<T> addLayer(FDBlockRenderLayerOptions<T> layer){
        this.layers.add(layer);
        return this;
    }

    public FDBlockEntityRendererBuilder<T> shouldRender(IShouldBERender<T> shouldRender){
        this.shouldRender = shouldRender;
        return this;
    }

    public FDBlockEntityRendererBuilder<T> shouldRenderOffScreen(IBERenderOffScreen<T> shouldRender){
        this.shouldRenderOffScreen = shouldRender;
        return this;
    }

    public FDBlockEntityRendererBuilder<T> freeRender(FDFreeBERenderer<T> freeRender){
        this.freeRender = freeRender;
        return this;
    }

    public FDBlockEntityRendererBuilder<T> renderBoundingBox(RenderFunction<BlockEntity, AABB> renderBox){
        this.renderBox = renderBox;
        return this;
    }

    public FDBlockEntityRendererBuilder<T> renderBoundingBox(AABB box){
        return this.renderBoundingBox(((tile, partialTicks) -> {
            BlockPos pos = tile.getBlockPos();
            return box.move(pos.getX() + 0.5f,pos.getY(),pos.getZ() + 0.5f);
        }));
    }

    public BlockEntityRendererProvider<T> build(){
        return (context -> {
            return new FDBlockEntityRenderer<>(context,shouldRender,shouldRenderOffScreen,layers,freeRender,renderBox);
        });
    }


}
