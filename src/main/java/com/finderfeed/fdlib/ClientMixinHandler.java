package com.finderfeed.fdlib;

import com.finderfeed.fdlib.systems.shake.ScreenShake;
import com.finderfeed.fdlib.systems.shake.ScreenShakeInstance;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = FDLib.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class ClientMixinHandler {


    private static final List<ScreenShakeInstance> SHAKES = new ArrayList<>();

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){

        if (event.phase != TickEvent.Phase.END) return;

        var iter = SHAKES.iterator();
        while (iter.hasNext()){
            var inst = iter.next();
            if (!inst.hasEnded()){
                inst.tick();
            }else{
                iter.remove();
            }
        }

    }

    @SubscribeEvent
    public static void onLogoff(ClientPlayerNetworkEvent.LoggingOut event){
        SHAKES.clear();
    }

    private static boolean renderShake = true;

    public static void beforeLevel(){
        renderShake = true;
    }

    public static void beforeHand(){
        renderShake = false;
    }

    public static void bobHurt(PoseStack matrices,float pticks){
        if (!renderShake) return;
        if (Minecraft.getInstance().level == null) return;
        if (Minecraft.getInstance().isPaused()) return;
        if (Minecraft.getInstance().player == null) return;
        for (ScreenShakeInstance instance : SHAKES){
            var shake = instance.shake;
            var time = instance.currentTime;
            shake.process(matrices,time,pticks);
        }
    }

    public static void addShake(ScreenShake shake){
        SHAKES.add(new ScreenShakeInstance(shake));
    }


}