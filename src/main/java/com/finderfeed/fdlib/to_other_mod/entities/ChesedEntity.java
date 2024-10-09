package com.finderfeed.fdlib.to_other_mod.entities;

import com.finderfeed.fdlib.FDHelpers;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDLivingEntity;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackChain;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackInstance;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackOptions;
import com.finderfeed.fdlib.to_other_mod.BossUtil;
import com.finderfeed.fdlib.to_other_mod.FDAnims;
import com.finderfeed.fdlib.to_other_mod.BossEntities;
import com.finderfeed.fdlib.to_other_mod.FDModels;
import com.finderfeed.fdlib.to_other_mod.client.BossParticles;
import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningOptions;
import com.finderfeed.fdlib.to_other_mod.client.particles.sonic_particle.SonicParticleOptions;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterSettings;
import com.finderfeed.fdlib.to_other_mod.entities.radial_earthquake.RadialEarthquakeEntity;
import com.finderfeed.fdlib.to_other_mod.projectiles.ChesedBlockProjectile;
import com.finderfeed.fdlib.util.ProjectileMovementPath;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static com.finderfeed.fdlib.to_other_mod.FDAnims.*;

public class ChesedEntity extends FDLivingEntity {

    public static EntityDataAccessor<Boolean> IS_ROLLING = SynchedEntityData.defineId(ChesedEntity.class, EntityDataSerializers.BOOLEAN);


    public AttackChain chain;
    private static FDModel serverModel;
    private static FDModel clientModel;
    private Vec3 oldRollPos;
    private boolean playIdle = true;


    public ChesedEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        if (serverModel == null) {
            serverModel = new FDModel(FDModels.CHESED.get());
        }
        if (clientModel == null){
            clientModel = new FDModel(FDModels.CHESED.get());
        }
        if (!level.isClientSide) {
            chain = new AttackChain(level.random)
                    .addAttack(0, AttackOptions.builder()
                            .addAttack("nothing1",this::doNothing)
                            .build())
                    .addAttack(1, AttackOptions.builder()
                            .addAttack("earthquake",this::earthquakeAttack)
                            .build())
                    .addAttack(2, AttackOptions.builder()
                            .addAttack("block",this::blockAttack)
                            .build())
                    .addAttack(3, AttackOptions.builder()
                            .addAttack("roll",this::roll)
                            .build())
                    .addAttack(4, AttackOptions.builder()
                            .addAttack("nothing",this::doNothing)
                            .build())
            ;
        }
    }


    @Override
    public void tick() {
        super.tick();
        AnimationSystem system = this.getSystem();
        system.setVariable("variable.radius",600);
        system.setVariable("variable.angle",270);
        this.lookAt(EntityAnchorArgument.Anchor.FEET,new Vec3(-18,106,544));
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
            }else{
                this.idleParticles();
            }
        }
    }

    private void idleParticles(){
        if (this.level().getGameTime() % 2 == 0){
            var pos = this.getModelPartPosition(this,"core", clientModel).add((float) this.getX(), (float) this.getY(), (float) this.getZ());
            float baseAngle = -(float)Math.toRadians(this.yBodyRot) + FDMathUtil.FPI / 4;
            float randomRange = FDMathUtil.FPI * 2 - FDMathUtil.FPI / 2;
            for (int i = 0; i < 2;i++) {
                var end = pos.add(new Vector3f(0, 0, 2).rotateY(baseAngle + randomRange * random.nextFloat()), new Vector3f()).add(0, -(pos.y - (float) this.getY()), 0);
                level().addParticle(ArcLightningOptions.builder(BossParticles.ARC_LIGHTNING.get())
                                .end(end.x, end.y, end.z)
                                .lifetime(2)
                                .color(1 + random.nextInt(40), 183 + random.nextInt(60), 165 + random.nextInt(60))
                                .lightningSpread(0.25f)
                                .width(0.1f)
                                .segments(6)
                                .circleOffset(random.nextFloat() * 2 - 2)
                                .build(),
                        true, pos.x, pos.y, pos.z, 0, 0, 0
                );
            }
        }
    }

    public boolean doNothing(AttackInstance instance){

        if (instance.tick % 20 == 0) {
            System.out.println("Idling...");
        }
        return instance.tick >= 20;
    }

    public boolean earthquakeAttack(AttackInstance instance){
//        if (true) return true;
        int t = instance.tick;
        int radius = 40;
        if (t < 6) {
            if (t == 0){
                this.getSystem().startAnimation("earthquake", AnimationTicker.builder(CHESED_EARTHQUAKE_CAST)
                                .setToNullTransitionTime(0)
                        .build());
            }
            if (t % 2 == 0) {
                float p =1 -  t / 5f;
                SonicParticleOptions options = SonicParticleOptions.builder()
                        .facing(0, 1,0)
                        .color(41 , 133 + (int)(p * 60), 175 + (int)(p * 60))

                        .startSize(radius)
                        .endSize(0)
                        .resizeSpeed(-0.3f)
                        .resizeAcceleration(-1f)
                        .build();
                for (Player player : this.level().getNearbyPlayers(BossUtil.ALL,this,this.getBoundingBox().inflate(100))) {
                    ((ServerLevel) level()).sendParticles((ServerPlayer) player, options, true, this.position().x, this.position().y + 0.01, this.position().z, 1, 0, 0, 0, 0);
                }
            }
        }else{

            if (t == 18){
                RadialEarthquakeEntity radialEarthquakeEntity = RadialEarthquakeEntity.summon(level(),this.getOnPos(),1,radius,1f,10f);
                for (int i = 0; i < 6;i++) {
                    float p = 1 - i / 6f;
                    SonicParticleOptions options = SonicParticleOptions.builder()
                            .facing(0, 1, 0)
                            .color(41 , 133 + (int)(p * 60), 175 + (int)(p * 60))

                            .startSize(2)
                            .endSize(radius)
                            .resizeSpeed(-i * 2)
                            .resizeAcceleration(0.75f + i)
                            .lifetime(60)
                            .build();
                    for (Player player : this.level().getNearbyPlayers(BossUtil.ALL,this,this.getBoundingBox().inflate(100))) {
                        ((ServerLevel) level()).sendParticles((ServerPlayer) player, options, true, this.position().x, this.position().y + 0.01, this.position().z, 1, 0, 0, 0, 0);
                    }
                }
            }else if (t > 50){
                instance.nextStage();
            }
        }
        return instance.stage > 4;
    }

    private List<ChesedBlockProjectile> blockAttackProjectiles = new ArrayList<>();

    public boolean blockAttack(AttackInstance attack){
        if (true) return true;
        float height = 8;
        int timeTillAttack = 60;
        if (blockAttackProjectiles.isEmpty()){
            if (!this.trySearchProjectiles()) {
                this.getSystem().startAnimation("blockAttack", AnimationTicker.builder(CHESED_CAST)
                                .setToNullTransitionTime(0)
                                .setSpeed(1.2f)
                        .build());
                attack.tick = 0;
                int count = 10;
                for (int i = 0; i < count; i++) {
                    float angle = this.getInitProjectileRotation(i, count);
                    ChesedBlockProjectile projectile = new ChesedBlockProjectile(BossEntities.BLOCK_PROJECTILE.get(), level());
                    projectile.setDropParticlesTime(timeTillAttack / 2);
                    var path = this.createRotationPath(angle, -2,height, 30, timeTillAttack / 2, false);
                    var next = this.createRotationPath(angle, height,height, 30, timeTillAttack / 2, true);

                    path.setNext(next);
                    projectile.noPhysics = true;
                    projectile.setPos(path.getPositions().getFirst());
                    projectile.movementPath = path;
                    blockAttackProjectiles.add(projectile);
                    level().addFreshEntity(projectile);
                }
            }
            return false;
        }else{
            if (attack.tick == 13){
                BossUtil.posEvent((ServerLevel) level(),this.position().add(0,0.05,0),BossUtil.CHESED_GET_BLOCKS_FROM_EARTH_EVENT,0,60);
            }
            Player player = level().getNearestPlayer(this,100);
            if (player == null) return false;
            if (blockAttackProjectiles.isEmpty()) return true;
            if (attack.tick >= timeTillAttack && attack.tick % 8 == 0){
                ChesedBlockProjectile next = this.blockAttackProjectiles.removeLast();
                next.noPhysics = false;
                next.movementPath = null;
                Vec3 tpos = this.targetGroundPosition(player);
                Vec3 b = tpos.subtract(next.position());
                Vec3 h = b.multiply(1,0,1);
                Vec3 targetPos = tpos.add(h.normalize().reverse().multiply(1.5,0,1.5));

                next.setRotationSpeed(10f);

                Vec3 flyTo = this.position().add(0,height + 1.5,0);
                ProjectileMovementPath path = new ProjectileMovementPath(8,false);
                path.addPos(next.position());
                path.addPos(flyTo);
                path.addPos(flyTo.add(0,1,0));
                path.setSpeedOnEnd(targetPos.subtract(flyTo).multiply(0.25,0.25,0.25));
                next.movementPath = path;
            }
            if (blockAttackProjectiles.isEmpty()) return true;
            return false;
        }
    }

    private Vec3 targetGroundPosition(LivingEntity target){
        Vec3 toReturn = target.position();
        BlockPos pos = new BlockPos(
                (int)Math.floor(toReturn.x),
                (int)Math.floor(toReturn.y),
                (int)Math.floor(toReturn.z)
        );
        for (int i = 0; i < 5;i++){
            if (level().getBlockState(pos.offset(0,-i,0)).isAir()){
                toReturn = toReturn.subtract(0,1,0);
            }else{
                return toReturn;
            }

        }
        return toReturn;
    }


    private boolean trySearchProjectiles(){
        List<ChesedBlockProjectile> projectiles = level().getEntitiesOfClass(ChesedBlockProjectile.class,this.getBoundingBox().inflate(10,20,10));
        if (!projectiles.isEmpty()){
            this.blockAttackProjectiles = projectiles;
            return true;
        }
        return false;
    }

    private ProjectileMovementPath createRotationPath(float angle,float yStart, float yEnd, int pathDetalization, int time, boolean cycle){
        ProjectileMovementPath path = new ProjectileMovementPath(time,cycle);
        float a1 = FDMathUtil.FPI * 2 / pathDetalization;
        for (int i = 0; i <= pathDetalization;i++){
            float p = i / (float) pathDetalization;
            float a = angle + i * a1;
            Vec3 v = new Vec3(5,0,0).yRot(a);
            Vec3 pos = this.position().add(v.x,FDMathUtil.lerp(yStart,yEnd, FDEasings.easeInOutBack(p)),v.z);
            path.addPos(pos);
        }
        return path;
    }

    private float getInitProjectileRotation(int id,int count){
        return 2 * FDMathUtil.FPI * (id / (float) count);
    }



    public boolean roll(AttackInstance instance){
        if (true) return true;
        int tick = instance.tick;
        if (tick == 0){
            this.oldRollPos = this.position();
        }
        Vector3f p = this.getModelPartPosition(this,"base", serverModel);
        Vec3 pos = this.position().add(p.x,p.y,p.z);
        var system = this.getSystem();


        if (tick < CHESED_ROLL_UP.get().getAnimTime()){

            this.playIdle = false;
            if (system.getTickerAnimation("ROLL_UP") != CHESED_ROLL_UP.get()) {
                system.startAnimation("ROLL_UP", AnimationTicker.builder(CHESED_ROLL_UP)
                        .startTime(tick)
                        .build());
            }
        }else if (tick >= CHESED_ROLL_UP.get().getAnimTime() && tick < CHESED_ROLL.get().getAnimTime() + CHESED_ROLL_UP.get().getAnimTime() - 20){
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
            this.setRolling(false);
            system.stopAnimation("ROLL_UP");
            system.startAnimation("ROLL_UP_END",AnimationTicker.builder(CHESED_ROLL_UP_JUST_END)
                    .setToNullTransitionTime(0)
                    .setLoopMode(Animation.LoopMode.HOLD_ON_LAST_FRAME)
                    .build());
        }


        if (tick > CHESED_ROLL_UP.get().getAnimTime() + CHESED_ROLL.get().getAnimTime() + CHESED_ROLL_UP_JUST_END.get().getAnimTime()){
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
        this.rollDamageEntities(oldRollPos,pos);
        oldRollPos = pos;


        int rollTime = FDAnims.CHESED_ROLL.get().getAnimTime() + FDAnims.CHESED_ROLL_UP.get().getAnimTime();

        if (attackTime >= rollTime){
            this.setRolling(false);
        }else{
            this.setRolling(true);
        }
    }

    private void rollDamageEntities(Vec3 oldPos,Vec3 pos){
        var entities = FDHelpers.traceEntities(level(),oldPos,pos,2.1,entity->{
            return entity instanceof LivingEntity living && entity != this;
        });

        //TODO: damage
        float damage = 10;

        for (Entity entity : entities){
            LivingEntity livingEntity = (LivingEntity) entity;
            Vec3 pushDirection = FDMathUtil.getNormalVectorFromLineToPoint(oldPos,pos,entity.position()).multiply(1,0,1)
                    .normalize()
                    .multiply(3,0,3)
                    .add(0,1.5,0);
            if (livingEntity instanceof Player player){
                FDHelpers.setServerPlayerSpeed((ServerPlayer) player,pushDirection);
            }else{
                livingEntity.setDeltaMovement(pushDirection);
            }
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
        Vector3f p = this.getModelPartPosition(this,"base", clientModel);
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


        Vec3 ppos = pos.add(
                random.nextFloat() * 2 - 1,
                0,
                random.nextFloat() * 2 - 1
        );

        float md = random.nextFloat() + 1;

        level().addParticle(
                ArcLightningOptions.builder(BossParticles.ARC_LIGHTNING.get())
                        .end(ppos.add(-nb.x * md,1 + 0.1f,-nb.z * md))
                        .lifetime(4)
                        .color(1 + random.nextInt(40), 183 + random.nextInt(60), 165 + random.nextInt(60))
                        .lightningSpread(0.25f)
                        .width(0.2f)
                        .segments(5)
                        .circleOffset(-3)
                        .build(),
                true,ppos.x,ppos.y + 0.5 + 0.1f,ppos.z,0,0,0
        );


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
            CompoundTag t = new CompoundTag();
            chain.save(t);
            tag.put("chain",t);
        }
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (chain != null){
            this.chain.load(tag.getCompound("chain"));
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
