package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDClientConfig;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.test.TestConfig;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDConfigs {

    public static final DeferredRegister<JsonConfig> CONFIGS = DeferredRegister.create(FDRegistries.CONFIGS_KEY, FDLib.MOD_ID);

    public static final Supplier<TestConfig> TEST = CONFIGS.register("test", TestConfig::new);
    public static final Supplier<FDClientConfig> CLIENTSIDE_CONFIG = CONFIGS.register("client_config", FDClientConfig::new);

}
