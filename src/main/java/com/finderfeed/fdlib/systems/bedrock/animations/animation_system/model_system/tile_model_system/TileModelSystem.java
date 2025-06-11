package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.tile_model_system;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.TileAnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class TileModelSystem extends ModelSystem {

    private BlockEntity blockEntity;

    public TileModelSystem(BlockEntity block) {
        super(TileAnimationSystem.create(block));
        this.blockEntity = block;
    }

    @Override
    public void onAttachment(int layer, String bone, UUID modelUUID, FDModelInfo attachedModel) {
        throw new RuntimeException("Not yet supported");
    }

    @Override
    public void onAttachmentRemoved(UUID modelUUID) {
        throw new RuntimeException("Not yet supported");
    }

}
