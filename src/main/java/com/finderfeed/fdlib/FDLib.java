package com.finderfeed.fdlib;


import com.finderfeed.fdlib.init.FDAnimations;
import com.finderfeed.fdlib.init.FDCommandArgumentTypes;
import com.finderfeed.fdlib.init.FDEntities;
import com.finderfeed.fdlib.init.FDModels;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(FDLib.MOD_ID)
public class FDLib {

    public static final String MOD_ID = "fdlib";

    public static final Logger LOGGER = LogUtils.getLogger();


    public FDLib(IEventBus modEventBus, ModContainer modContainer) {
        FDModels.INFOS.register(modEventBus);
        FDAnimations.ANIMATIONS.register(modEventBus);
        FDEntities.ENTITIES.register(modEventBus);
        FDCommandArgumentTypes.ARGUMENT_TYPES.register(modEventBus);
    }


}


