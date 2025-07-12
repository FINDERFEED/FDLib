package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(FDLib.MOD_ID);

    public static final Supplier<Item> TEST_MULTIBLOCK = ITEMS.register("test_multiblock",()->new BlockItem(FDBlocks.TEST_MULTIBLOCK.get(), new Item.Properties()));

}
