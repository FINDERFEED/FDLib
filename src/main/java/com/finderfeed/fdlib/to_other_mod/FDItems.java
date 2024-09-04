package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.to_other_mod.debug.DebugStick;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(FDLib.MOD_ID);

    public static final Supplier<Item> DEBUG_STICK = ITEMS.register("debug",()->new DebugStick(new Item.Properties()));

}
