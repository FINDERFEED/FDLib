package com.finderfeed.fdlib.to_other_mod.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BossModels {

    public static final DeferredRegister<FDModelInfo> INFOS = DeferredRegister.create(FDRegistries.MODELS, FDLib.MOD_ID);

    public static final Supplier<FDModelInfo> CHESED = INFOS.register("chesed",()->new FDModelInfo(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"),1f));
    public static final Supplier<FDModelInfo> CHESED_ELECTRIC_SPHERE = INFOS.register("chesed_electric_sphere",()->new FDModelInfo(ResourceLocation.tryBuild(FDLib.MOD_ID,"electric_orb"),1f));

}
