package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.entity_types;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.FDLayers;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.EntityAnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.EntityAnimationsSyncPacket;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.ModelHaver;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

public abstract class FDLivingEntity extends LivingEntity implements AnimatedObject, ModelHaver {

    private Map<String,FDModel> layers;
    private AnimationSystem system;
    private FDModel model;

    public FDLivingEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        this.system = EntityAnimationSystem.create(this);
        this.model = new FDModel(this.getModelBase());
        this.layers = this.getLayerInfos().convert();
    }


    @Override
    public void tick() {
        super.tick();
        this.tickAnimationSystem();
    }

    @Override
    public void tickAnimationSystem(){
        this.getSystem().tick();
    }

    @Override
    public FDModel getModel() {
        return model;
    }

    @Override
    public AnimationSystem getSystem() {
        return system;
    }

    @Override
    public Map<String, FDModel> getLayers() {
        return layers;
    }

    @Override
    public FDLayers getLayerInfos() {
        return new FDLayers();
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer,new EntityAnimationsSyncPacket(this.getId(),this.getSystem().getTickers()));
    }

}
