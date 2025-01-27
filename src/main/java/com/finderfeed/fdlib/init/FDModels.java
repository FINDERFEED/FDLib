package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDModels {

    public static final DeferredRegister<FDModelInfo> MODELS = DeferredRegister.create(FDRegistries.MODELS, FDLib.MOD_ID);

    public static final Supplier<FDModelInfo> TEST = MODELS.register("test",()->new FDModelInfo(FDLib.location("test"),1));

}
