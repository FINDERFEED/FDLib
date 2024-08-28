package com.finderfeed.fdlib.systems;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,modid = FDLib.MOD_ID)
public class FDRegistries {

    public static final ResourceKey<Registry<FDModelInfo>> BEDROCK_MODEL_INFOS_KEY = key("bedrock/models");
    public static final ResourceKey<Registry<Animation>> ANIMATIONS_KEY = key("bedrock/animations");
    public static final ResourceKey<Registry<JsonConfig>> CONFIGS_KEY = key("configs");

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.tryBuild(FDLib.MOD_ID, name));
    }

    public static Registry<FDModelInfo> MODELS = new RegistryBuilder<>(BEDROCK_MODEL_INFOS_KEY).sync(true).create();
    public static Registry<Animation> ANIMATIONS = new RegistryBuilder<>(ANIMATIONS_KEY).sync(true).create();
    public static Registry<JsonConfig> CONFIGS = new RegistryBuilder<>(CONFIGS_KEY).sync(true).create();


    @SubscribeEvent
    public static void createRegistries(NewRegistryEvent event){
        event.register(MODELS);
        event.register(ANIMATIONS);
        event.register(CONFIGS);
    }


}
