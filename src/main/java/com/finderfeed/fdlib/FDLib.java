package com.finderfeed.fdlib;


import com.finderfeed.fdlib.init.FDEDataSerializers;
import com.finderfeed.fdlib.init.FDParticles;
import com.finderfeed.fdlib.init.FDCommandArgumentTypes;

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
//        FDItems.ITEMS.register(bus);
//        BossModels.INFOS.register(bus);
//        BossSounds.SOUNDS.register(bus);
//        BossAnims.ANIMATIONS.register(bus);
//        BossEntities.ENTITIES.register(bus);
        FDParticles.PARTICLES.register(bus);
//        BossEffects.MOB_EFFECTS.register(bus);
//        BossParticles.PARTICLES.register(bus);
        FDEDataSerializers.SERIALIZERS.register(bus);
        FDCommandArgumentTypes.ARGUMENT_TYPES.register(bus);

//        BossBars.BOSS_BARS.register(bus);

    }


}


