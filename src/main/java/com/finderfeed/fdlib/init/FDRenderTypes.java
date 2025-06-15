package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.render_types.FDRenderType;
import com.finderfeed.fdlib.systems.render_types.instances.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDRenderTypes {

    public static final DeferredRegister<FDRenderType> RENDER_TYPES = DeferredRegister.create(FDRegistries.RENDER_TYPE, FDLib.MOD_ID);

    public static final Supplier<FDRTEntityCutout> ENTITY_CUTOUT = RENDER_TYPES.register("entity_cutout", FDRTEntityCutout::new);

    public static final Supplier<FDRTEntityCutoutNoCull>  ENTITY_CUTOUT_NO_CULL = RENDER_TYPES.register("entity_cutout_no_cull", FDRTEntityCutoutNoCull::new);
    public static final Supplier<FDRTEntityTranslucent>  ENTITY_TRANSLUCENT = RENDER_TYPES.register("entity_translucent", FDRTEntityTranslucent::new);
    public static final Supplier<FDRTEntityTranslucentCull>  ENTITY_TRANSLUCENT_CULL = RENDER_TYPES.register("entity_translucent_cull", FDRTEntityTranslucentCull::new);
    public static final Supplier<FDRTEntityTranslucentEmissive>  ENTITY_TRANSLUCENT_EMISSIVE = RENDER_TYPES.register("entity_translucent_emissive", FDRTEntityTranslucentEmissive::new);
    public static final Supplier<FDRTEyes>  EYES = RENDER_TYPES.register("eyes", FDRTEyes::new);
    public static final Supplier<FDRTLightning>  LIGHTNING = RENDER_TYPES.register("lightning", FDRTLightning::new);

}
