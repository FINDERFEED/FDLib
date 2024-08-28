package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.FDLayers;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.ModelHaver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class FDTileEntity extends BlockEntity implements AnimatedObject, ModelHaver {

    private FDModel model;
    private AnimationSystem system;

    public FDTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.system = TileAnimationSystem.create(this);
        this.model = new FDModel(this.getModelBase());
    }

    @Override
    public AnimationSystem getSystem() {
        return system;
    }

    @Override
    public void tickAnimationSystem() {
        system.tick();
    }

    @Override
    public FDModel getModel() {
        return model;
    }

    @Override
    public FDLayers getLayerInfos() {
        throw new RuntimeException("Layers are not supported by tile entities.");
    }

    @Override
    public Map<String, FDModel> getLayers() {
        throw new RuntimeException("Layers are not supported by tile entities.");
    }
}
