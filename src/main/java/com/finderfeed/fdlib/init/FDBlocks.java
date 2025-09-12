package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.multiblock.TestMultiBlock;
import com.finderfeed.fdlib.test.FDTestBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FDLib.MOD_ID);

    public static final Supplier<Block> TEST = BLOCKS.register("test",()->new FDTestBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));
    public static final Supplier<Block> TEST_MULTIBLOCK = BLOCKS.register("test_multiblock",()->new TestMultiBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));


}
