package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDLivingEntity;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackChain;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackInstance;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackOptions;
import com.finderfeed.fdlib.to_other_mod.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.earthshatter_entity.EarthShatterSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ChesedEntity extends FDLivingEntity {


    public AttackChain chain;
    private static FDModel model;

    public ChesedEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        model = new FDModel(FDModels.CHESED.get());
        if (!level.isClientSide) {
            chain = new AttackChain(level.random)
                    .addAttack(0, AttackOptions.builder()
                            .addAttack("nothing1",this::doNothing)
                            .build())
                    .addAttack(1, AttackOptions.builder()
                            .addAttack("roll",this::roll)
                            .build())
                    .addAttack(2, AttackOptions.builder()
                            .addAttack("nothing",this::doNothing)
                            .build())
            ;
        }
    }


    @Override
    public void tick() {
        super.tick();
        AnimationSystem system = this.getSystem();

        system.setVariable("variable.radius",200);
        system.setVariable("variable.angle",180);
        if (!this.level().isClientSide){
            this.chain.tick();
            system.startAnimation("IDLE", new AnimationTicker(FDAnimations.CHESED_IDLE.get()));
        }
    }


    @Override
    public boolean save(CompoundTag tag) {
        if (chain != null){
            chain.save(tag);
        }
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (chain != null){
            this.chain.load(tag);
        }
        super.load(tag);
    }

    public boolean doNothing(AttackInstance instance){

        if (instance.tick % 20 == 0) {
            System.out.println("Idling...");
        }
        return instance.tick >= 200;
    }

    public boolean roll(AttackInstance instance){
        var system = this.getSystem();
        AnimationTicker ticker = system.getTicker("ROLL");
        if (ticker == null){
            system.startAnimation("ROLL",
                    AnimationTicker.builder(FDAnimations.CHESED_ROLL.get())
                            .startTime(instance.tick)
                            .build()
                    );
        }
        Vector3f p = this.getModelPartPosition("base",model);
        Vector3f p1 = this.transformPoint(new Vector3f(0,0,1),"base",model);
        Vec3 b = new Vec3(
                p1.x - p.x,
                p1.y - p.y,
                p1.z - p.z
        );
        Vec3 pos = this.position().add(
                p.x,p.y,p.z
        );
        if (level() instanceof ServerLevel level){
            BlockPos blockPos = new BlockPos(
                    (int) pos.x,
                    (int) pos.y - 2,
                    (int) pos.z
            );
            EarthShatterEntity.summon(level,blockPos, EarthShatterSettings.builder()
                            .direction(b.reverse().add(0,1,0))
                            .upTime(4)
                            .upDistance(0.5f)
                            .downTime(4)
                            .stayTime(2)
                    .build());
        }
        return instance.tick >= FDAnimations.CHESED_ROLL.get().getAnimTime();
    }

}
