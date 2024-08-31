package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDModels {

    public static final DeferredRegister<FDModelInfo> INFOS = DeferredRegister.create(FDRegistries.MODELS, FDLib.MOD_ID);


    public static final Supplier<FDModelInfo> TEST = INFOS.register("test",()->new FDModelInfo(ResourceLocation.tryBuild(FDLib.MOD_ID,"uldera_crystal"),1));
    public static final Supplier<FDModelInfo> TEST2 = INFOS.register("test2",()->new FDModelInfo(ResourceLocation.tryBuild(FDLib.MOD_ID,"uldera_crystal"),2f));
    public static final Supplier<FDModelInfo> CHESED = INFOS.register("chesed",()->new FDModelInfo(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"),1f));
}
