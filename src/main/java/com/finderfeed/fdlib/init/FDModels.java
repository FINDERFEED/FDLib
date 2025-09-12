package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDModels {

    public static final DeferredRegister<FDModelInfo> MODELS = DeferredRegister.create(FDRegistries.BEDROCK_MODEL_INFOS_KEY, FDLib.MOD_ID);

    public static final Supplier<FDModelInfo> TEST = MODELS.register("test",()->new FDModelInfo(FDLib.location("test"),1));
    public static final Supplier<FDModelInfo> TEST2 = MODELS.register("test2",()->new FDModelInfo(FDLib.location("test"),1.5f));

}
