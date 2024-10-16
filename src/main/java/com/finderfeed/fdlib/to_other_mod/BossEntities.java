package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.to_other_mod.entities.ChesedEntity;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.falling_block.ChesedFallingBlock;
import com.finderfeed.fdlib.to_other_mod.entities.flying_block_entity.FlyingBlockEntity;
import com.finderfeed.fdlib.to_other_mod.entities.radial_earthquake.RadialEarthquakeEntity;
import com.finderfeed.fdlib.to_other_mod.projectiles.ChesedBlockProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD)
public class BossEntities {


    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, FDLib.MOD_ID);

    public static final Supplier<EntityType<ChesedEntity>> CHESED = ENTITIES.register("chesed",()->EntityType.Builder.<ChesedEntity>of(
            ChesedEntity::new, MobCategory.CREATURE
    )
            .sized(1f,1f)
            .build("chesed"));

    public static final Supplier<EntityType<EarthShatterEntity>> EARTH_SHATTER = ENTITIES.register("earth_shatter",()->EntityType.Builder.<EarthShatterEntity>of(
            EarthShatterEntity::new, MobCategory.MISC
    )
            .sized(1f,1f)
            .build("earth_shatter"));

    public static final Supplier<EntityType<ChesedBlockProjectile>> BLOCK_PROJECTILE = ENTITIES.register("block_projectile",()->EntityType.Builder.<ChesedBlockProjectile>of(
            ChesedBlockProjectile::new, MobCategory.MISC
    )
            .sized(1f,1f)
            .updateInterval(1)
            .build("block_projectile"));

    public static final Supplier<EntityType<ChesedFallingBlock>> CHESED_FALLING_BLOCK = ENTITIES.register("chesed_falling_block",()->EntityType.Builder.<ChesedFallingBlock>of(
            ChesedFallingBlock::new, MobCategory.MISC
    )
            .sized(0.2f,0.2f)
            .build("chesed_falling_block"));

    public static final Supplier<EntityType<FlyingBlockEntity>> FLYING_BLOCK = ENTITIES.register("flying_block",()->EntityType.Builder.of(
            FlyingBlockEntity::new, MobCategory.MISC
    )
            .sized(1f,1f)
            .build("flying_block"));

    public static final Supplier<EntityType<RadialEarthquakeEntity>> RADIAL_EARTHQUAKE = ENTITIES.register("radial_earthquake",()->EntityType.Builder.of(
            RadialEarthquakeEntity::new, MobCategory.MISC
    )
            .sized(0.2f,0.2f)
            .build("radial_earthquake"));


    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(CHESED.get(), LivingEntity.createLivingAttributes().build());
    }



}
