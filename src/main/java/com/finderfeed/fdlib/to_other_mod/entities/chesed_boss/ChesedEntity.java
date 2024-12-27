package com.finderfeed.fdlib.to_other_mod.entities.chesed_boss;

import com.finderfeed.fdlib.FDHelpers;
import com.finderfeed.fdlib.network.lib_packets.PlaySoundInEarsPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDLivingEntity;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackChain;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackInstance;
import com.finderfeed.fdlib.systems.entity.action_chain.AttackOptions;
import com.finderfeed.fdlib.systems.hud.bossbars.FDServerBossBar;
import com.finderfeed.fdlib.systems.particle.CircleParticleProcessor;
import com.finderfeed.fdlib.systems.particle.CompositeParticleProcessor;
import com.finderfeed.fdlib.systems.particle.SetParticleSpeedProcessor;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterData;
import com.finderfeed.fdlib.systems.particle.particle_emitter.processors.BoundToEntityProcessor;
import com.finderfeed.fdlib.systems.particle.particle_emitter.processors.CircleSpawnProcessor;
import com.finderfeed.fdlib.systems.shake.DefaultShakePacket;
import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.finderfeed.fdlib.systems.shake.PositionedScreenShakePacket;
import com.finderfeed.fdlib.to_other_mod.*;
import com.finderfeed.fdlib.to_other_mod.client.BossParticles;
import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningOptions;
import com.finderfeed.fdlib.to_other_mod.client.particles.ball_particle.BallParticleOptions;
import com.finderfeed.fdlib.to_other_mod.client.particles.chesed_attack_ray.ChesedRayOptions;
import com.finderfeed.fdlib.to_other_mod.client.particles.smoke_particle.BigSmokeParticleOptions;
import com.finderfeed.fdlib.to_other_mod.client.particles.sonic_particle.SonicParticleOptions;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.earthshatter_entity.EarthShatterSettings;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.electric_sphere.ChesedElectricSphereEntity;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.falling_block.ChesedFallingBlock;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.radial_earthquake.RadialEarthquakeEntity;
import com.finderfeed.fdlib.to_other_mod.init.*;
import com.finderfeed.fdlib.to_other_mod.projectiles.ChesedBlockProjectile;
import com.finderfeed.fdlib.util.FDUtil;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Math;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.finderfeed.fdlib.to_other_mod.init.BossAnims.*;

public class ChesedEntity extends FDLivingEntity {

    public static EntityDataAccessor<Boolean> IS_ROLLING = SynchedEntityData.defineId(ChesedEntity.class, EntityDataSerializers.BOOLEAN);
    public static EntityDataAccessor<Boolean> IS_LAUNCHING_ORBS = SynchedEntityData.defineId(ChesedEntity.class, EntityDataSerializers.BOOLEAN);

    public FDServerBossBar bossBar = new FDServerBossBar(BossBars.CHESED_BOSS_BAR,this);

    public AttackChain chain;
    private static FDModel serverModel;
    private static FDModel clientModel;
    private Vec3 oldRollPos;
    private boolean playIdle = true;
    private boolean lookingAtTarget = true;
    private LivingEntity target;
    private boolean doBlinding = true;


    public ChesedEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        if (serverModel == null) {
            serverModel = new FDModel(BossModels.CHESED.get());
        }
        if (clientModel == null){
            clientModel = new FDModel(BossModels.CHESED.get());
        }
        if (!level.isClientSide) {
            chain = new AttackChain(level.random)
                    .addAttack(-5,AttackOptions.builder()
                            .addAttack("nothing",this::doNothing)
                            .build())
                    .addAttack(-3, AttackOptions.builder()
                            .addAttack("rayAttack",this::rayAttack)
                            .build())
                    .addAttack(-2, AttackOptions.builder()
                            .addAttack("electricSphereAttack",this::electricSphereAttack)
                            .build())
                    .addAttack(0,AttackOptions.builder()
                            .addAttack("nothing",this::doNothing)
                            .build())
                    .addAttack(0, AttackOptions.builder()
                            .addAttack("rockfall",this::rockfallAttack)
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
        this.bossBar.setPercentage(this.getHealth() / this.getMaxHealth());
        AnimationSystem system = this.getSystem();
        system.setVariable("variable.radius",600);
        system.setVariable("variable.angle",270);
        if (!this.level().isClientSide){
            this.chain.tick();
            if (playIdle) {
                system.startAnimation("IDLE", AnimationTicker.builder(CHESED_IDLE).build());
            }else{
                system.stopAnimation("IDLE");
            }
            if (level().getGameTime() % 10 == 0) {
                this.blindCombatants();
            }

            if (this.getTarget() != null) {
                this.checkTarget(this.getTarget());
                if (this.lookingAtTarget) {
                    this.lookAtTarget(this.getTarget());
                }else{
                    this.lookAt(EntityAnchorArgument.Anchor.EYES,this.getEyePosition().add(this.getLookAngle()));
                }
            }else{
                if (level().getGameTime() % 20 == 0){
                    this.changeTarget();
                }
            }
        }else{
            if (this.isRolling()){
                this.handleClientRolling();
            }else{
                this.idleParticles();
            }
        }
    }


    public List<Player> getCombatants(boolean includeCreativeAndSpectator){
        float radius = this.enrageRadius();
        List<Player> combatants = level().getEntitiesOfClass(Player.class,new AABB(-radius,-2,-radius,radius,40,radius).move(this.position()),(player)->{
            return player.position().distanceTo(this.position()) <= radius && (includeCreativeAndSpectator || !(player.isCreative() || player.isSpectator()));
        });
        return combatants;
    }

    private void checkTarget(LivingEntity target){

        if (target.isDeadOrDying()){
            this.changeTarget();
            return;
        }else if (target.position().distanceTo(this.position()) > this.enrageRadius()){
            this.changeTarget();
            return;
        }


        if (target instanceof Player player){
            if (player.isCreative() || player.isSpectator()){
                this.changeTarget();
                return;
            }
        }


    }

    private void changeTarget(){
        List<Player> combatants = this.getCombatants(false);
        if (combatants.isEmpty()){
            this.setTarget(null);
        }else{
            this.setTarget(combatants.get(random.nextInt(combatants.size())));
        }
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    private void blindCombatants(){
        var combatants = this.getCombatants(true);

        for (Player player : combatants){

            Vec3 lookAngle = player.getLookAngle();
            Vec3 b = player.position().subtract(this.position()).normalize();
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,400,0,true,true));
            if (!(player.isCreative() || player.isSpectator())) {
                if (doBlinding) {
                    if (lookAngle.dot(b) > 0.05) {
                        player.addEffect(new MobEffectInstance(BossEffects.CHESED_GAZE, 200, 0, true, true));
                    } else {
                        player.removeEffect(BossEffects.CHESED_GAZE);
                    }
                }
            }

        }

    }

    private void idleParticles(){
        if (this.level().getGameTime() % 2 == 0){
            float rad = 2;
            if (this.entityData.get(IS_LAUNCHING_ORBS)){
                rad = 5;
            }
            var pos = this.getModelPartPosition(this,"core", clientModel).add((float) this.getX(), (float) this.getY(), (float) this.getZ());
            float baseAngle = -(float)Math.toRadians(this.yBodyRot) + FDMathUtil.FPI / 4;
            float randomRange = FDMathUtil.FPI * 2 - FDMathUtil.FPI / 2;
            for (int i = 0; i < 2;i++) {
                var end = pos.add(new Vector3f(0, 0, rad).rotateY(baseAngle + randomRange * random.nextFloat()), new Vector3f()).add(0, -(pos.y - (float) this.getY()), 0);
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


        if (this.entityData.get(IS_LAUNCHING_ORBS)){

            for (int i = 0; i <= 30;i++){
                this.spawnOrbLaunchingParticle();
            }

        }

    }

    private void spawnOrbLaunchingParticle(){


        Vec3 direction = new Vec3(1,0,0).yRot(random.nextFloat() * FDMathUtil.FPI * 2);

        float rndHeight = random.nextFloat();
        float heightMd = FDEasings.easeOut(1 - Math.abs(rndHeight * 2 - 1)) * 0.5f + 0.5f;

        rndHeight *= 3;

        Vec3 basePos = this.position().add(
                0,
                rndHeight,
                0
        );

        Vec3 rotateFromPos = this.position().add(
                direction.x * heightMd * 3,
                rndHeight,
                direction.z * heightMd * 3
        );

        BallParticleOptions options = BallParticleOptions.builder()
                .particleProcessor(new CircleParticleProcessor(
                        basePos,true,true,2
                ))
                .color(1 + random.nextInt(40), 183 + random.nextInt(60), 165 + random.nextInt(60))
                .size(0.4f)
                .scalingOptions(2,20,10)
                .build();

        level().addParticle(options,true,rotateFromPos.x,rotateFromPos.y,rotateFromPos.z,0,0,0);

    }

    public boolean doNothing(AttackInstance instance){

        if (instance.tick % 20 == 0) {
            System.out.println("Idling... " + instance.tick);
        }
        return instance.tick >= 1000000;
    }

    public boolean rayAttack(AttackInstance instance){

        int tick = instance.tick;
        int stage = instance.stage;

        if (stage == 0){
            this.changeTarget();
        }

        int localStage = stage % 3;

        if (localStage == 1) {
            lookingAtTarget = false;

            int rayAttackTick = 35;
            if (tick == 0) {

                this.getSystem().startAnimation("ATTACK", AnimationTicker.builder(CHESED_ATTACK)
                                .setToNullTransitionTime(0)
                        .build());

            }else if (tick > 10 && tick < rayAttackTick) {
                var c = this.getModelPartPosition(this,"core",serverModel);
                Vec3 center = new Vec3(c.x,c.y,c.z).add(this.position());
                for (int i = 0; i < 10;i++) {
                    Vec3 pos = center.add(new Vec3(
                                    random.nextFloat() * 2 - 1,
                                    random.nextFloat() * 2 - 1,
                                    random.nextFloat() * 2 - 1
                            ).normalize().multiply(1.2,1.2,1.2));
                    Vec3 speed = center.subtract(pos).normalize().multiply(0.05,0.05,0.05);
                    BallParticleOptions options = BallParticleOptions.builder()
                            .friction(1.8f)
                            .size(0.3f)
                            .scalingOptions(0, 1, 4)
                            .color(100 + random.nextInt(50), 255, 255)
                            .particleProcessor(new SetParticleSpeedProcessor(speed))
                            .build();
                    FDUtil.sendParticles((ServerLevel) level(),options,pos,60);
                }
            } else if (tick == rayAttackTick) {

                Vec3 look = this.getLookAngle();
                Vec3 p = this.getCenter().add(look.reverse());
                Vec3 end = p.add(look.multiply(60,60,60));
                ClipContext clipContext = new ClipContext(p,end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty());
                var result = level().clip(clipContext);
                end = result.getLocation();

                ChesedRayOptions options = ChesedRayOptions.builder()
                        .time(0,15,5)
                        .lightningColor(90, 180, 255)
                        .color(100, 255, 255)
                        .end(end)
                        .width(0.8f)
                        .build();
                FDUtil.sendParticles((ServerLevel) level(),options,p,60);

                Vec3 reversedLook = look.reverse();



                BossUtil.chesedRaySmoke((ServerLevel) level(),end,reversedLook,120);
                ((ServerLevel)level()).playSound(null,end.x,end.y,end.z, BossSounds.CHESED_LIGHTNING_RAY.get(), SoundSource.HOSTILE,100f,0.8f);


                PositionedScreenShakePacket.send((ServerLevel) level(),FDShakeData.builder()
                        .frequency(5)
                        .amplitude(5f)
                        .inTime(0)
                        .stayTime(0)
                        .outTime(5)
                        .build(),end,120);


                Vector3f v = new Vector3f(0,1,0).cross((float)reversedLook.x,(float)reversedLook.y,(float)reversedLook.z);
                for (int i = 0; i < 30;i++){

                    BlockState state = random.nextFloat() > 0.5 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.SCULK.defaultBlockState();

                    Vector3f add = v.rotateAxis(FDMathUtil.FPI * 2 * random.nextFloat(),(float)reversedLook.x,(float)reversedLook.y,(float)reversedLook.z,new Vector3f());
                    float rd = random.nextFloat() * 0.5f;
                    ChesedFallingBlock block = ChesedFallingBlock.summon(level(),state,end,reversedLook.add(
                            add.x * rd * 2,
                            add.y * rd,
                            add.z * rd * 2
                    ).normalize().multiply(0.5,2.4 - rd,0.5));

                    float rnd = random.nextFloat() * 0.05f;

                    FDHelpers.addParticleEmitter(level(), 120, ParticleEmitterData.builder(BigSmokeParticleOptions.builder()
                                    .color(0.35f - rnd, 0.35f - rnd, 0.35f - rnd)
                                    .lifetime(0, 0, 25)
                                    .size(1.5f)
                                    .build())
                            .lifetime(200)
                            .processor(new BoundToEntityProcessor(block.getId(), Vec3.ZERO))
                            .position(this.position())
                            .build());

                }
                BossUtil.chesedRayExplosion((ServerLevel) level(),end,reversedLook,100,15,0.75f);
            }else if (tick > rayAttackTick){
                if (tick > rayAttackTick + 30){
                    instance.nextStage();
                    return false;
                }
            }
        }else if (localStage == 0) {
            lookingAtTarget = true;
            if (this.getTarget() != null) {
                if (isLookingStraightAtEntity(this.getTarget(), 0.05)) {
                    instance.nextStage();
                    lookingAtTarget = false;
                }
            }
        }else if (localStage == 2){
            float rnd = random.nextFloat();
            float p = stage / (6f * 3);
            float ch = FDMathUtil.normalDistribution(p,0,0.43f);
            if (rnd > ch){
                lookingAtTarget = true;
                return true;
            }
            instance.nextStage();
        }

        return false;
    }

    public boolean electricSphereAttack(AttackInstance instance){
        lookingAtTarget = true;
//        if (true) return true;
        var tick = instance.tick;
        var stage = instance.stage;
        if (stage == 0) {

            if (tick == 0) {
                this.getSystem().startAnimation("CAST", AnimationTicker.builder(CHESED_CAST)
                        .setToNullTransitionTime(0)
                        .build());
            }else if (tick > 15) {
                this.entityData.set(IS_LAUNCHING_ORBS, true);
                instance.nextStage();
            }
        }else if (stage == 1) {

            if (tick % 10 == 0) {
                for (int i = 0; i < 4; i++) {
                    Vec3 v = new Vec3(1, 0, 0).yRot(i * FDMathUtil.FPI / 2 + tick / 2f * FDMathUtil.FPI / 8);
                    this.shootSphere(v);
                }
            }

            if (tick > 200){
                instance.nextStage();
            }

        }else if (stage == 2) {

            if (tick % 15 == 0){

                int amountOnArc = 5;

                float rndOffs = random.nextFloat();

                for (int i = -amountOnArc;i <= amountOnArc;i++){

                    Vec3 v = new Vec3(1,0,0)
                            .yRot(i / (float) amountOnArc * FDMathUtil.FPI / 8 + rndOffs * FDMathUtil.FPI);

                    this.shootSphere(v,80);
                    this.shootSphere(v.reverse(),80);

                }


            }

            if (tick > 200){
                instance.nextStage();
            }
        }else{

            if (tick < 200) {
                if (tick % 10 == 0) {
                    this.spawnSpheresAround(25, tick / 10f * FDMathUtil.FPI / 9);
                }


            }else if (tick < 350){
                this.entityData.set(IS_LAUNCHING_ORBS,false);
            }else{
                return true;
            }
            return false;
        }

        return false;
    }


    private void spawnSpheresAround(int count,float angleOffset){

        float angle = FDMathUtil.FPI * 2 / count;

        for (int i = 0; i < count;i++){

            Vec3 dir = new Vec3(1,0,0).yRot(i * angle + angleOffset);

            this.shootSphere(dir,150);
        }
    }

    private void shootSphere(Vec3 direction){
        this.shootSphere(direction,100);
    }

    private void shootSphere(Vec3 direction,int time){
        direction = direction.normalize();
        Vec3 sppos = this.position().add(0,1,0).add(direction);
        Vec3 endPos = sppos.add(direction.multiply(37,37,37));
        ProjectileMovementPath path = new ProjectileMovementPath(time,false)
                .addPos(sppos)
                .addPos(endPos);
        ChesedElectricSphereEntity sphereEntity = ChesedElectricSphereEntity.summon(level(),10,path);
    }


    public boolean rockfallAttack(AttackInstance instance){
        lookingAtTarget = true;
//        if (true) return true;
        int stage = instance.stage;
        int tick = instance.tick;
        int height = 32;
        int rad = 36;
        if (stage == 0){
            this.getSystem().startAnimation("ROCKFALL", AnimationTicker.builder(CHESED_ROCKFALL_CAST)
                            .setToNullTransitionTime(0)
                    .build());

            if (tick > 10 && tick < 30) {
                int count = 3;

                float angle = FDMathUtil.FPI * 2 / count;

                Vec3 center = this.position();

                for (int i = 0; i < count; i++) {
                    float a = angle * i;
                    Vec3 v = new Vec3(2,0,0).yRot(a);
                    Vec3 ppos = this.position().add(v);
                    FDUtil.sendParticles(((ServerLevel) level()),BallParticleOptions.builder()
                            .size(0.5f)
                            .color(100 + random.nextInt(50), 255, 255)
                            .particleProcessor(new CompositeParticleProcessor(
                                    new CircleParticleProcessor(center,true,true,2),
                                    new SetParticleSpeedProcessor(new Vec3(0,0.2,0))
                            ))
                            .scalingOptions(10,10,0)
                            .build(),ppos,height * 2);


                }
            }else if (tick == 45){
                Vec3 pos = this.position();

                FDUtil.sendParticles((ServerLevel) level(),ChesedRayOptions.builder()
                                .width(1f)
                                .end(pos.add(0,height,0))
                                .lightningColor(90, 180, 255)
                                .color(100, 255, 255)
                                .stay(8)
                                .in(2)
                                .out(10)
                        .build(), pos.add(0,1,0),60);
                PositionedScreenShakePacket.send((ServerLevel) level(),FDShakeData.builder()
                        .frequency(10)
                        .amplitude(10f)
                        .inTime(0)
                        .stayTime(0)
                        .outTime(10)
                        .build(),this.position().add(0,height,0),height * 2);
                DefaultShakePacket.send((ServerLevel) level(),this.position(),60,FDShakeData.builder()
                                .frequency(15)
                                .amplitude(0.15f)
                                .inTime(0)
                                .stayTime(50)
                                .outTime(150)
                        .build());





                BossUtil.chesedRayExplosion((ServerLevel) level(),this.position().add(0,height,0),new Vec3(0,-1,0),120,10,1);
                ((ServerLevel)level()).playSound(null,this.getX(),this.getY() + height,this.getZ(), BossSounds.CHESED_RAY.get(), SoundSource.HOSTILE,100f,1f);
                PacketDistributor.sendToPlayersTrackingEntity(this,new PlaySoundInEarsPacket(BossSounds.ROCKFALL.get()));
                PacketDistributor.sendToPlayersTrackingEntity(this,new PlaySoundInEarsPacket(BossSounds.RUMBLING.get()));

                int count = 10 + random.nextInt(10);
                float angle = FDMathUtil.FPI * 2 / count;

                for (int i = 0; i < count;i++) {


                    BlockState state = random.nextFloat() > 0.5f ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.SCULK.defaultBlockState();

                    ChesedFallingBlock block = ChesedFallingBlock.summon(level(), state, this.position().add(0, height, 0));

                    Vec3 v = new Vec3(random.nextFloat() * 0.025 + 0.2,0,0).yRot(angle * i + (random.nextFloat() * 2 - 1) * angle);

                    block.setDeltaMovement(v.add(0,-random.nextFloat() * 0.5 - 0.25,0));

                    float rnd = random.nextFloat() * 0.05f;

                    FDHelpers.addParticleEmitter(level(), height * 2, ParticleEmitterData.builder(BigSmokeParticleOptions.builder()
                                    .color(0.35f - rnd, 0.35f - rnd, 0.35f - rnd)
                                    .lifetime(0, 0, 50)
                                    .size(2f)
                                    .build())
                            .lifetime(200)
                            .processor(new BoundToEntityProcessor(block.getId(), Vec3.ZERO))
                            .position(this.position())
                            .build());


                }

                FDHelpers.addParticleEmitter(level(), height * 2, ParticleEmitterData.builder(BigSmokeParticleOptions.builder()
                                .color(0.25f, 0.25f, 0.25f)
                                .lifetime(0, 0, 100)
                                .size(1f)
                                .build())
                        .lifetime(400)
                        .particlesPerTick(20)
                        .processor(new CircleSpawnProcessor(new Vec3(0,-1,0),0.05f,0.1f,36))
                        .position(this.position().add(0,height + 2,0))
                        .build());

            }else if (tick >= 46){

                instance.nextStage();
            }
        }else if (stage == 1){

            float duration = 400;

            this.spawnStonesAround(4,rad, this.position().add(0,height,0),true,false,FDEasings::easeOut);

            if (tick % 2 == 0) {
                AABB box = new AABB(-rad, -2, -rad, rad, height, rad).move(this.position());
                for (Player player : this.level().getNearbyPlayers(BossUtil.ALL, this, box)) {

                    Vec3 p = player.position();

                    if (p.subtract(this.position()).multiply(1, 0, 1).length() > rad) continue;

                    this.spawnStonesAround(1, 5, p.multiply(1, 0, 1).add(0, this.position().y + height, 0), true, false, FDEasings::easeOut);

                }
            }

            if (tick >= duration) {
                instance.nextStage();
            }
        }else {
            return true;
        }


        return false;
    }

    private void spawnStonesAround(int count, int rad, Vec3 center, boolean useEasing, boolean reverseEasing, Function<Float,Float> func){
        for (int i = 0; i < count;i++){

            float rnd = random.nextFloat();

            if (useEasing){
                if (!reverseEasing) {
                    rnd = func.apply(rnd);
                }else{
                    rnd = 1 - func.apply(rnd);
                }
            }

            Vec3 v = new Vec3(rad * rnd,0,0).yRot(random.nextFloat() * FDMathUtil.FPI * 2);

            Vec3 p = center.add(v);

            BlockState state;
            if (level().random.nextFloat() > 0.5){
                state = Blocks.SCULK.defaultBlockState();
            }else{
                state = Blocks.DEEPSLATE.defaultBlockState();
            }

            ChesedFallingBlock.summon(level(), state,p);
        }
    }


    public boolean earthquakeAttack(AttackInstance instance){
        lookingAtTarget = false;
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
            }else if (t == 21) {
                PositionedScreenShakePacket.send((ServerLevel) level(), FDShakeData.builder()
                        .frequency(10f)
                        .stayTime(0)
                        .inTime(5)
                        .outTime(5)
                        .amplitude(3.f)
                        .build(),this.position(),50);
            }else if (t > 50){
                instance.nextStage();
            }
        }
        return instance.stage > 4;
    }

    private List<ChesedBlockProjectile> blockAttackProjectiles = new ArrayList<>();

    public boolean blockAttack(AttackInstance attack){
        lookingAtTarget = true;
//        if (true) return true;
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
                    projectile.setBlockState(random.nextFloat() > 0.5 ? Blocks.SCULK.defaultBlockState() : Blocks.DEEPSLATE.defaultBlockState());
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
            LivingEntity player = this.getTarget();

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
        lookingAtTarget = false;
        doBlinding = false;
//        if (true) return true;
        int tick = instance.tick;
        var stage = instance.stage;
        if (tick == 0 && stage == 0){
            this.oldRollPos = this.position();
        }
        Vector3f p = this.getModelPartPosition(this,"base", serverModel);
        Vec3 pos = this.position().add(p.x,p.y,p.z);
        var system = this.getSystem();

        if (stage == 0){
            this.playIdle = false;
            if (system.getTickerAnimation("ROLL_UP") != CHESED_ROLL_UP.get()) {
                system.startAnimation("ROLL_UP", AnimationTicker.builder(CHESED_ROLL_UP)
                        .startTime(tick)
                        .build());
            }
            if (tick >= CHESED_ROLL_UP.get().getAnimTime()){
                instance.nextStage();
                return false;
            }
        }else if (stage == 1){
            this.playIdle = false;
            PositionedScreenShakePacket.send((ServerLevel) level(), FDShakeData.builder()
                    .frequency(5f)
                    .stayTime(0)
                    .inTime(2)
                    .outTime(5)
                    .amplitude(3.5f)
                    .build(),pos,10);
            if (system.getTickerAnimation("ROLL_AROUND") != CHESED_ROLL.get()){
                system.startAnimation("ROLL_AROUND",AnimationTicker.builder(CHESED_ROLL)
                        .startTime(tick)
                        .setToNullTransitionTime(0)
                        .build());
            }
            if (system.getTickerAnimation("ROLLING") != CHESED_ROLL_ROLL.get()){
                system.startAnimation("ROLLING",AnimationTicker.builder(CHESED_ROLL_ROLL)
                        .setToNullTransitionTime(0)
                        .startTime(tick)
                        .build());
            }
            if (tick >= CHESED_ROLL.get().getAnimTime() - 20){
                instance.nextStage();
                return false;
            }else{
                this.handleRollEarthShatters(tick, pos);
            }
        }else if (stage == 2){
            doBlinding = true;
            this.setRolling(false);
            system.stopAnimation("ROLL_UP");
            system.startAnimation("ROLL_UP_END",AnimationTicker.builder(CHESED_ROLL_UP_JUST_END)
                    .setToNullTransitionTime(0)
                    .setLoopMode(Animation.LoopMode.HOLD_ON_LAST_FRAME)
                    .build());
            if (tick >= CHESED_ROLL_UP_JUST_END.get().getAnimTime() + 20){
                instance.nextStage();
                this.playIdle = true;
                return false;
            }
        }else{
            this.playIdle = true;
            this.setRolling(false);
            system.stopAnimation("ROLL_UP");
            system.stopAnimation("ROLL_UP_END");
            system.stopAnimation("ROLL_AROUND");
            system.stopAnimation("ROLLING");
            doBlinding = true;
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


        int rollTime = BossAnims.CHESED_ROLL.get().getAnimTime() + BossAnims.CHESED_ROLL_UP.get().getAnimTime();

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

    private void lookAtTarget(LivingEntity target){
        Vec3 pos = this.getLookAtPos(target);
        this.lookAt(EntityAnchorArgument.Anchor.EYES,pos);
    }

    private Vec3 getLookAtPos(LivingEntity target){
        Vec3 pos = target.position().add(0,target.getBbHeight() / 2,0);
        return pos;
    }

    private boolean isLookingStraightAtEntity(LivingEntity entity,double accuracy){
        Vec3 look = this.getLookAngle();
        Vec3 lookAt = this.getLookAtPos(entity);
        Vec3 center = this.getEyePosition();
        Vec3 b = lookAt.subtract(center).normalize();
        double v = look.dot(b);
        return v >= (1 - accuracy);
    }

    private Vec3 getCenter(){
        return this.position().add(0,1.3,0);
    }

    private float enrageRadius(){
        return 39;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_ROLLING,false);
        builder.define(IS_LAUNCHING_ORBS,false);
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

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
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
