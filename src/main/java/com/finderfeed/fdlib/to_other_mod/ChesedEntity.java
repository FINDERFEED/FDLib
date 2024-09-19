package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDLivingEntity;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackChain;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackInstance;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackOptions;
import com.finderfeed.fdlib.to_other_mod.client.FDParticles;
import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningOptions;
import com.finderfeed.fdlib.to_other_mod.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.earthshatter_entity.EarthShatterSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.finderfeed.fdlib.to_other_mod.FDAnims.*;

public class ChesedEntity extends FDLivingEntity {

    public static EntityDataAccessor<Boolean> IS_ROLLING = SynchedEntityData.defineId(ChesedEntity.class, EntityDataSerializers.BOOLEAN);


    public AttackChain chain;
    private static FDModel model;
    private Vec3 oldRollPos;
    private boolean playIdle = true;


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
        this.lookAt(EntityAnchorArgument.Anchor.FEET,new Vec3(0,0,0));

        if (level().isClientSide && level().getGameTime() % 60 == 0) {
            this.level().addParticle(new ArcLightningOptions(FDParticles.ARC_LIGHTNING.get(),new Vec3(
                    this.getX() + 10,this.getY(),this.getZ() + 10),60,0.1f,1f,0f,0f),
                    this.getX(), this.getY() + 10, this.getZ(), 0, 0, 0
            );
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Spawned particle"));
        }

        system.setVariable("variable.radius",400);
        system.setVariable("variable.angle",180);
        if (!this.level().isClientSide){
            this.chain.tick();
            if (playIdle) {
                system.startAnimation("IDLE", AnimationTicker.builder(CHESED_IDLE).build());
            }else{
                system.stopAnimation("IDLE");
            }
        }else{
            if (this.isRolling()){
                this.handleClientRolling();
            }
        }
    }







    public boolean doNothing(AttackInstance instance){

        if (instance.tick % 20 == 0) {
            System.out.println("Idling...");
        }
        return instance.tick >= 200000;
    }


    public boolean roll(AttackInstance instance){
        int tick = instance.tick;
        if (tick == 0){
            this.oldRollPos = this.position();
        }
        Vector3f p = this.getModelPartPosition(this,"base",model);
        Vec3 pos = this.position().add(p.x,p.y,p.z);
        var system = this.getSystem();


        if (tick < CHESED_ROLL_UP.get().getAnimTime()){

            this.playIdle = false;
            if (system.getTickerAnimation("ROLL_UP") != CHESED_ROLL_UP.get()) {
                system.startAnimation("ROLL_UP", AnimationTicker.builder(CHESED_ROLL_UP)
                        .startTime(tick)
                        .build());
            }
        }else if (tick >= CHESED_ROLL_UP.get().getAnimTime() && tick < CHESED_ROLL.get().getAnimTime() + CHESED_ROLL_UP.get().getAnimTime() - 50){
            if (tick < 11 * 20 + CHESED_ROLL_UP.get().getAnimTime()) {
                this.handleRollEarthShatters(tick, pos);
            }

            this.playIdle = false;
            if (system.getTickerAnimation("ROLL_AROUND") != CHESED_ROLL.get()){
                system.startAnimation("ROLL_AROUND",AnimationTicker.builder(CHESED_ROLL)
                                .startTime(tick - CHESED_ROLL_UP.get().getAnimTime())
                                .setToNullTransitionTime(0)
                        .build());
            }
            if (system.getTickerAnimation("ROLLING") != CHESED_ROLL_ROLL.get() && tick < 13 * 20 + CHESED_ROLL_UP.get().getAnimTime()){
                system.startAnimation("ROLLING",AnimationTicker.builder(CHESED_ROLL_ROLL)
                                .setToNullTransitionTime(0)
                                .startTime(tick - CHESED_ROLL_UP.get().getAnimTime())
                        .build());
            }
        }else{
//            this.playIdle = true;
            system.stopAnimation("ROLL_UP");
//            system.startAnimation("ROLL_UP_END",AnimationTicker.builder(CHESED_ROLL_UP_END)
//                    .setToNullTransitionTime(0)
//                    .setLoopMode(Animation.LoopMode.HOLD_ON_LAST_FRAME)
//                    .build());
        }


        if (tick > CHESED_ROLL_UP.get().getAnimTime() + CHESED_ROLL.get().getAnimTime() + CHESED_ROLL_UP_END.get().getAnimTime() + 20 * 2){
            this.playIdle = true;
            this.setRolling(false);
            system.stopAnimation("ROLL_UP");
            system.stopAnimation("ROLL_UP_END");
            system.stopAnimation("ROLL_AROUND");
            system.stopAnimation("ROLLING");
            return true;
        }



        return false;
    }

    private void handleRollEarthShatters(int attackTime,Vec3 pos){
        if (attackTime == 0){
            this.oldRollPos = this.position();
        }

        if (oldRollPos == null){
            oldRollPos = pos;
        }
        this.summonRollEarthShatters(oldRollPos.add(0,-1,0),pos.add(0,-1,0));
        oldRollPos = pos;

        int rollTime = FDAnims.CHESED_ROLL.get().getAnimTime() + FDAnims.CHESED_ROLL_UP.get().getAnimTime();

        if (attackTime >= rollTime){
            this.setRolling(false);
        }else{
            this.setRolling(true);
        }
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
            Vec3 v = pos.add(nb.multiply(i,i,i).reverse());

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

    private void handleClientRolling(){
        Vector3f p = this.getModelPartPosition(this,"base",model);
        Vec3 pos = this.position().add(
                p.x,p.y - 1,p.z
        );
        if (oldRollPos == null){
            oldRollPos = pos;
        }

        Vec3 b = pos.subtract(oldRollPos);
        Vec3 nb = b.normalize();
        Vec3 r = nb.yRot((float)Math.PI / 2);
        Vec3 l = nb.yRot(-(float)Math.PI / 2);

        for (float i = 0; i < b.length();i++){
            Vec3 v = pos.add(nb.multiply(i,i,i).reverse());

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
            BlockState mainState = level().getBlockState(mainpos);
            BlockState leftState = level().getBlockState(lpos);
            BlockState rightState = level().getBlockState(rpos);

            float randomRadius = 1;

            if (!mainState.isAir()){
                for (int g = 0; g < 5;g++) {
                    level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, mainState),true,
                            v.x + level().random.nextFloat() * randomRadius - randomRadius/2,
                            v.y + 0.75,
                            v.z + level().random.nextFloat() * randomRadius - randomRadius/2,
                            0, 0, 0
                    );
                }
            }
            if (!leftState.isAir()){
                for (int g = 0; g < 5;g++) {
                    level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, leftState),true,
                            l1.x + level().random.nextFloat() * randomRadius - randomRadius / 2,
                            l1.y + 0.75,
                            l1.z + level().random.nextFloat() * randomRadius - randomRadius / 2,
                            0, 0, 0
                    );
                }
            }
            if (!rightState.isAir()){
                for (int g = 0; g < 5;g++) {
                    level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, rightState),true,
                            r1.x + level().random.nextFloat() * randomRadius - randomRadius / 2,
                            r1.y + 0.75,
                            r1.z + level().random.nextFloat() * randomRadius - randomRadius / 2,
                            0, 0, 0
                    );
                }
            }


        }
        oldRollPos = pos;
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_ROLLING,false);
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

    public void setRolling(boolean state){
        this.entityData.set(IS_ROLLING,state);
    }

    public boolean isRolling(){
        return this.entityData.get(IS_ROLLING);
    }

    @Override
    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_19883_) {
        return true;
    }
}
