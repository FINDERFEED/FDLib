package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.test.FDTestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(FDLib.MOD_ID);

    public static final Supplier<Block> TEST = BLOCKS.register("test",()->new FDTestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));


}
