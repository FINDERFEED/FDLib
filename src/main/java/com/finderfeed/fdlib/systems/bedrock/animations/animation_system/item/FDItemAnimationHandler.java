package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.animated_item.AnimatedItemStackContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = FDLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FDItemAnimationHandler {

    public static AnimatedItemStackContext currentRenderedContext = null;

    private static final HashMap<AnimatedItemStackContext, AnimatedItemContainer> ANIMATED_ITEMS = new HashMap<>(); // Wtf?

    @SubscribeEvent
    public static void tickItems(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.START) return;
        tickAnimatedItems();
    }

    /**
     * Only for client side
     */
    public static FDItemAnimationSystem getItemAnimationSystem(AnimatedItemStackContext itemStack){
        if (ANIMATED_ITEMS.containsKey(itemStack)){
            AnimatedItemContainer animatedItem = ANIMATED_ITEMS.get(itemStack);
            return animatedItem.animationSystem;
        }else{
            return null;
        }
    }

    public static void tickAnimatedItems(){
        var iterator = ANIMATED_ITEMS.entrySet().iterator();
        while (iterator.hasNext()){
            var item = iterator.next();
            AnimatedItemContainer animatedItem = item.getValue();
            animatedItem.tick();
            if (animatedItem.shouldBeRemoved()){
                iterator.remove();
            }
        }
    }

    public static void tellIAmCurrentlyRendering(AnimatedItemStackContext ctx, boolean setCurrentRenderingContext){
        if (ANIMATED_ITEMS.containsKey(ctx)){

            var item = ANIMATED_ITEMS.get(ctx);
            item.tellThatIAmAlive();

        }else{
            ANIMATED_ITEMS.put(ctx, new AnimatedItemContainer(ctx));
        }

        if (setCurrentRenderingContext){
            currentRenderedContext = ctx;
        }

    }


    private static class AnimatedItemContainer {

        public FDItemAnimationSystem animationSystem;
        public AnimatedItemStackContext itemStack;
        public int age;

        public AnimatedItemContainer(AnimatedItemStackContext itemStack){
            this.itemStack = itemStack;
            this.animationSystem = new FDItemAnimationSystem();
        }

        public void tick(){
            if (itemStack.getItemStack().getItem() instanceof AnimatedItem animatedItemTickListener){
                animatedItemTickListener.animatedItemTick(itemStack);
            }
            animationSystem.tick();
            age++;
        }

        public boolean shouldBeRemoved(){
            return age > 200 || itemStack.getItemStack().isEmpty();
        }

        public void tellThatIAmAlive(){
            this.age = 0;
        }

    }

}
