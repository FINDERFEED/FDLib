package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.cutscenes.ClientCameraEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, FDLib.MOD_ID);

    public static final Supplier<EntityType<ClientCameraEntity>> CLIENT_CAMERA = ENTITY_TYPES.register("client_camera",()->
            EntityType.Builder.<ClientCameraEntity>of(ClientCameraEntity::new, MobCategory.MISC)
                    .updateInterval(1)
                    .sized(0.2f,0.2f)
                    .build("client_camera")

    );

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = FDLib.MOD_ID)
    public static class Attributes{

        @SubscribeEvent
        public static void addAttributes(EntityAttributeCreationEvent event){
            event.put(CLIENT_CAMERA.get(), LivingEntity.createLivingAttributes().build());
        }

    }

}
