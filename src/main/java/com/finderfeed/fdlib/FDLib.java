package com.finderfeed.fdlib;


import com.finderfeed.fdlib.init.FDEDataSerializers;
import com.finderfeed.fdlib.to_other_mod.FDAnims;
import com.finderfeed.fdlib.init.FDCommandArgumentTypes;
import com.finderfeed.fdlib.to_other_mod.FDEntities;
import com.finderfeed.fdlib.to_other_mod.FDItems;
import com.finderfeed.fdlib.to_other_mod.FDModels;
import com.finderfeed.fdlib.to_other_mod.client.FDParticles;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(FDLib.MOD_ID)
public class FDLib {

    public static final String MOD_ID = "fdlib";

    public static final Logger LOGGER = LogUtils.getLogger();


    public static ResourceLocation location(String loc){
        return ResourceLocation.tryBuild(MOD_ID,loc);
    }

    public FDLib(IEventBus bus, ModContainer modContainer) {
        FDItems.ITEMS.register(bus);
        FDModels.INFOS.register(bus);
        FDAnims.ANIMATIONS.register(bus);
        FDEntities.ENTITIES.register(bus);
        FDParticles.PARTICLES.register(bus);
        FDEDataSerializers.SERIALIZERS.register(bus);
        FDCommandArgumentTypes.ARGUMENT_TYPES.register(bus);
    }


}


