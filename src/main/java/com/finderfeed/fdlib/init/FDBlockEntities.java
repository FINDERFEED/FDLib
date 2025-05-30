package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.test.FDTestBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDBlockEntities {


    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, FDLib.MOD_ID);

    public static final Supplier<BlockEntityType<FDTestBlockEntity>> TEST = BLOCK_ENTITIES.register("test",()->BlockEntityType.Builder.of(FDTestBlockEntity::new,FDBlocks.TEST.get())
            .build(null));


}
