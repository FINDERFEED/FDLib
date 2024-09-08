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
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ChesedEntity extends FDLivingEntity {


    public AttackChain chain;
    private static FDModel model;

    public ChesedEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        if (model == null) {
            model = new FDModel(FDModels.CHESED.get());
        }
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
        return instance.tick >= 10;
    }

    private Vec3 oldRollPos;

    public boolean roll(AttackInstance instance){
        if (instance.tick == 0){
            this.oldRollPos = this.position();
        }
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
        Vec3 pos = this.position().add(
                p.x,p.y,p.z
        );
        this.summonRollEarthShatters(oldRollPos.add(0,-2,0),pos.add(0,-2,0));
        oldRollPos = pos;
        return instance.tick >= FDAnimations.CHESED_ROLL.get().getAnimTime();
    }


    private void summonRollEarthShatters(Vec3 oldPos, Vec3 pos){

        Vec3 b = pos.subtract(oldPos);
        Vec3 nb = b.normalize();
        Vec3 r = nb.yRot((float)Math.PI / 2);
        Vec3 l = nb.yRot(-(float)Math.PI / 2);


        Matrix4fStack mat = new Matrix4fStack(3);
        float angle = (float) Math.atan2(nb.x,nb.z);
        mat.rotateY(angle);

        mat.pushMatrix();
        mat.rotateX((float)Math.toRadians(-65));
        Vector4f direction = mat.transform(0,0,1,1,new Vector4f());
        mat.popMatrix();

        mat.pushMatrix();
        mat.rotateY((float)Math.toRadians(45));
        mat.rotateX((float)Math.toRadians(-45));
        Vector4f directionLeft = mat.transform(0,0,1,1,new Vector4f());
        mat.popMatrix();

        mat.pushMatrix();
        mat.rotateY((float)Math.toRadians(-45));
        mat.rotateX((float)Math.toRadians(-45));
        Vector4f directionRight = mat.transform(0,0,1,1,new Vector4f());
        mat.popMatrix();

        EarthShatterSettings settingsMain = EarthShatterSettings.builder()
                .direction(
                        direction.x,
                        direction.y,
                        direction.z
                )
                .upTime(4)
                .upDistance(0.25f)
                .downTime(8)
                .stayTime(2)
                .build();

        EarthShatterSettings settingsLeft = EarthShatterSettings.builder()
                .direction(
                        directionLeft.x,
                        directionLeft.y,
                        directionLeft.z
                )
                .upTime(4)
                .upDistance(0.5f)
                .downTime(8)
                .stayTime(2)
                .build();

        EarthShatterSettings settingsRight = EarthShatterSettings.builder()
                .direction(
                        directionRight.x,
                        directionRight.y,
                        directionRight.z
                )
                .upTime(4)
                .upDistance(0.5f)
                .downTime(8)
                .stayTime(2)
                .build();

        for (float i = 0; i < b.length();i++){
            Vec3 v = oldPos.add(nb.multiply(i,i,i));

            Vec3 r1 = v.add(r);
            Vec3 l1 = v.add(l);

            BlockPos mainpos = new BlockPos(
                    (int)v.x,
                    (int)v.y,
                    (int)v.z
            );

            BlockPos rpos = new BlockPos(
                    (int)r1.x,
                    (int)r1.y,
                    (int)r1.z
            );

            BlockPos lpos = new BlockPos(
                    (int)l1.x,
                    (int)l1.y,
                    (int)l1.z
            );

            EarthShatterEntity.summon(level(),mainpos, settingsMain);

            EarthShatterEntity.summon(level(),lpos, settingsLeft);

            EarthShatterEntity.summon(level(),rpos, settingsRight);

        }

    }

}
