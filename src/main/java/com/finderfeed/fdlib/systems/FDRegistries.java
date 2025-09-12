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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,modid = FDLib.MOD_ID)
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

    public static Supplier<IForgeRegistry<FDModelInfo>> MODELS;
    public static Supplier<IForgeRegistry<Animation>> ANIMATIONS;
    public static Supplier<IForgeRegistry<JsonConfig>> CONFIGS;
    public static Supplier<IForgeRegistry<FDBossBarFactory<?>>> BOSS_BARS;
    public static Supplier<IForgeRegistry<ScreenEffectType<?,?>>> SCREEN_EFFECTS;
    public static Supplier<IForgeRegistry<ModelAttachmentType<?,?>>> MODEL_ATTACHMENT_TYPES;
    public static Supplier<IForgeRegistry<FDRenderType>> RENDER_TYPE;

    @SubscribeEvent
    public static void createRegistries(NewRegistryEvent event){
        MODELS = event.create(new RegistryBuilder<FDModelInfo>().setName(BEDROCK_MODEL_INFOS_KEY.location()));
        CONFIGS = event.create(new RegistryBuilder<JsonConfig>().setName(CONFIGS_KEY.location()));
        BOSS_BARS = event.create(new RegistryBuilder<FDBossBarFactory<?>>().setName(FD_BOSS_BARS.location()));
        ANIMATIONS = event.create(new RegistryBuilder<Animation>().setName(ANIMATIONS_KEY.location()));
        RENDER_TYPE = event.create(new RegistryBuilder<FDRenderType>().setName(RENDER_TYPE_KEY.location()));
        SCREEN_EFFECTS = event.create(new RegistryBuilder<ScreenEffectType<?,?>>().setName(SCREEN_EFFECTS_KEY.location()));
        MODEL_ATTACHMENT_TYPES = event.create(new RegistryBuilder<ModelAttachmentType<?,?>>().setName(MODEL_ATTACHMENT_TYPE_KEY.location()));
    }

}
