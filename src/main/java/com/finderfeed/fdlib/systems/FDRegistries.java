package com.finderfeed.fdlib.systems;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarFactory;
import com.finderfeed.fdlib.systems.render_types.FDRenderType;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectType;
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
    public static final ResourceKey<Registry<FDBossBarFactory<?>>> FD_BOSS_BARS = key("boss_bars");
    public static final ResourceKey<Registry<ScreenEffectType<?,?>>> SCREEN_EFFECTS_KEY = key("screen_effect");
    public static final ResourceKey<Registry<ModelAttachmentType<?,?>>> MODEL_ATTACHMENT_TYPE_KEY = key("model_attachment_type");
    public static final ResourceKey<Registry<FDRenderType>> RENDER_TYPE_KEY = key("render_type");

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.tryBuild(FDLib.MOD_ID, name));
    }

    public static Registry<FDModelInfo> MODELS = new RegistryBuilder<>(BEDROCK_MODEL_INFOS_KEY).sync(true).create();
    public static Registry<Animation> ANIMATIONS = new RegistryBuilder<>(ANIMATIONS_KEY).sync(true).create();
    public static Registry<JsonConfig> CONFIGS = new RegistryBuilder<>(CONFIGS_KEY).sync(true).create();
    public static Registry<FDBossBarFactory<?>> BOSS_BARS = new RegistryBuilder<>(FD_BOSS_BARS).sync(true).create();
    public static Registry<ScreenEffectType<?,?>> SCREEN_EFFECTS = new RegistryBuilder<>(SCREEN_EFFECTS_KEY).sync(true).create();
    public static Registry<ModelAttachmentType<?,?>> MODEL_ATTACHMENT_TYPES = new RegistryBuilder<>(MODEL_ATTACHMENT_TYPE_KEY).sync(true).create();
    public static Registry<FDRenderType> RENDER_TYPE = new RegistryBuilder<>(RENDER_TYPE_KEY).sync(true).create();

    @SubscribeEvent
    public static void createRegistries(NewRegistryEvent event){
        event.register(MODELS);
        event.register(CONFIGS);
        event.register(BOSS_BARS);
        event.register(ANIMATIONS);
        event.register(RENDER_TYPE);
        event.register(SCREEN_EFFECTS);
        event.register(MODEL_ATTACHMENT_TYPES);
    }

}
