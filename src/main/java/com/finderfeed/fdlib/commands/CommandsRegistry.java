package com.finderfeed.fdlib.commands;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.init.FDModEvents;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.TriggerAnimationReloadPacket;
import com.finderfeed.fdlib.systems.bedrock.TriggerModelReloadPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.packets.JsonConfigSyncPacket;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.GAME)
public class CommandsRegistry {


    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        event.getDispatcher()
                .register(
                        Commands.literal("fdlib")
                                .then(Commands.literal("animation_test")
                                        .then(Commands.argument("animation",new AnimationArgument())
                                                .then(Commands.argument("layer", StringArgumentType.string())
                                                        .then(Commands.literal("entity")
                                                                .then(Commands.argument("target", EntityArgument.entity())
                                                                    .executes((context)->{
                                                                        executeEntityAnimation(context,
                                                                                context.getArgument("animation",String.class),
                                                                                context.getArgument("layer",String.class),
                                                                                EntityArgument.getEntity(context,"target"));
                                                                        return 1;
                                                                    })
                                                                )
                                                        )
                                                        .then(Commands.literal("tile")
                                                                .then(Commands.argument("target", BlockPosArgument.blockPos())
                                                                    .executes((context)->{
                                                                        executeTileEntityAnimation(context,
                                                                                context.getArgument("animation",String.class),
                                                                                context.getArgument("layer",String.class),
                                                                                BlockPosArgument.getBlockPos(context,"target"));
                                                                        return 1;
                                                                    })
                                                                )
                                                        )

                                                )
                                        )

                                )
                                .then(Commands.literal("reload")
                                        .then(Commands.literal("configs").executes((ctx)->{
                                            reloadConfigs(ctx);
                                            return 1;
                                        }))
                                        .then(Commands.literal("models").executes(ctx->{
                                            reloadModels(ctx);
                                            return 1;
                                        }))
                                        .then(Commands.literal("animations").executes(ctx->{
                                            reloadAnimations(ctx);
                                            return 1;
                                        }))

                                )

                )
        ;
    }

    public static void reloadAnimations(CommandContext<CommandSourceStack> ctx){
        CommandSourceStack stack = ctx.getSource();
        if (SharedConstants.IS_RUNNING_IN_IDE){
            stack.sendSystemMessage(Component.literal("Reloading animations..."));
            FDModEvents.loadAnimations();
            PacketDistributor.sendToAllPlayers(new TriggerAnimationReloadPacket());
            stack.sendSystemMessage(Component.literal("Reload complete!"));
        }else{
            stack.sendFailure(Component.literal("Could only be used in dev environment."));
        }
    }

    public static void reloadModels(CommandContext<CommandSourceStack> ctx){
        CommandSourceStack stack = ctx.getSource();
        if (SharedConstants.IS_RUNNING_IN_IDE){
            stack.sendSystemMessage(Component.literal("Reloading models..."));
            FDModEvents.loadModels();
            PacketDistributor.sendToAllPlayers(new TriggerModelReloadPacket());
            stack.sendSystemMessage(Component.literal("Reload complete!"));
        }else{
            stack.sendFailure(Component.literal("Could only be used in dev environment."));
        }
    }

    public static void reloadConfigs(CommandContext<CommandSourceStack> ctx){
        CommandSourceStack stack = ctx.getSource();
        for (JsonConfig config : FDRegistries.CONFIGS){
            stack.sendSystemMessage(Component.literal("Loading config: " + config.getName()));
            config.loadFromDisk();
            stack.sendSystemMessage(Component.literal("Loaded config: " + config.getName()).withStyle(ChatFormatting.GREEN));
        }
        PacketDistributor.sendToAllPlayers(new JsonConfigSyncPacket());
    }

    public static void executeEntityAnimation(CommandContext<CommandSourceStack> ctx, String animname, String tickerName, Entity entity) throws CommandSyntaxException {
        CommandSourceStack sourceStack = ctx.getSource();
        Animation animation = FDRegistries.ANIMATIONS.get(ResourceLocation.parse(animname));
        if (animation == null){
            sourceStack.sendFailure(Component.literal("No such animation found: " + animname));
            return;
        }
        if (!(entity instanceof AnimatedObject object)){
            sourceStack.sendFailure(Component.literal("Entity is not an animatable object"));
            return;
        }
        sourceStack.sendSystemMessage(Component.literal("Playing animation"));
        object.getSystem().startAnimation(tickerName,new AnimationTicker.Builder(animation)
                        .setLoopMode(Animation.LoopMode.ONCE)
                        .setToNullTransitionTime(0)
                .build());
    }

    public static void executeTileEntityAnimation(CommandContext<CommandSourceStack> ctx, String animname, String tickerName, BlockPos pos) throws CommandSyntaxException {
        CommandSourceStack sourceStack = ctx.getSource();
        ServerLevel level = ctx.getSource().getLevel();
        Animation animation = FDRegistries.ANIMATIONS.get(ResourceLocation.parse(animname));
        if (animation == null){
            sourceStack.sendFailure(Component.literal("No such animation found: " + animname));
            return;
        }
        if (!(level.getBlockEntity(pos) instanceof AnimatedObject object)){
            sourceStack.sendFailure(Component.literal("Tile entity is not an animatable object"));
            return;
        }
        sourceStack.sendSystemMessage(Component.literal("Playing animation"));
        object.getSystem().startAnimation(tickerName,new AnimationTicker.Builder(animation)
                        .setLoopMode(Animation.LoopMode.ONCE)
                        .setToNullTransitionTime(0)
                .build());
    }




}
