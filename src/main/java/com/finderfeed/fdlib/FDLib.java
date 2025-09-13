package com.finderfeed.fdlib;


import com.finderfeed.fdlib.init.*;


import com.finderfeed.fdlib.network.FDPacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;

@Mod(FDLib.MOD_ID)
public class FDLib {

    public static final String MOD_ID = "fdlib";

    public static final Logger LOGGER = LogUtils.getLogger();


    public static ResourceLocation location(String loc){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID,loc);
    }

    public FDLib() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();;
        FDItems.ITEMS.register(bus);
        FDBlocks.BLOCKS.register(bus);
        FDModels.MODELS.register(bus);
        FDConfigs.CONFIGS.register(bus);
        FDAnims.ANIMATIONS.register(bus);
        FDParticles.PARTICLES.register(bus);
        FDEntities.ENTITY_TYPES.register(bus);
        FDRenderTypes.RENDER_TYPES.register(bus);
        FDBlockEntities.BLOCK_ENTITIES.register(bus);
        FDEDataSerializers.SERIALIZERS.register(bus);
        FDScreenEffects.SCREEN_EFFECT_TYPES.register(bus);
        FDCommandArgumentTypes.ARGUMENT_TYPES.register(bus);
        FDModelAttachmentTypes.MODEL_ATTACHMENT_TYPES.register(bus);
        FDPacketHandler.registerMessages();
    }


}


