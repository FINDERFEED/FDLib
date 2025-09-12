package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.commands.AnimationArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDCommandArgumentTypes {

    public static final DeferredRegister<ArgumentTypeInfo<?,?>> ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, FDLib.MOD_ID);

    public static final Supplier<ArgumentTypeInfo<?,?>> ANIMATION_ARGUMENT = ARGUMENT_TYPES.register("animation_argument", ()->
            ArgumentTypeInfos.registerByClass(AnimationArgument.class,SingletonArgumentInfo.contextFree(AnimationArgument::new)));

}
