package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.HashMap;

@EventBusSubscriber(modid = FDLib.MOD_ID)
public class FDItemAnimationHandler {

    private static final HashMap<ItemStack, AnimatedItem> ANIMATED_ITEMS = new HashMap<>(); // Wtf?

    @SubscribeEvent
    public static void tickItems(ClientTickEvent.Pre event){
        tickAnimatedItems();
    }

    /**
     * Only for client side
     */
    public static FDItemAnimationSystem getItemAnimationSystem(ItemStack itemStack){
        tellItemThatItIsAlive(itemStack);
        AnimatedItem animatedItem = ANIMATED_ITEMS.get(itemStack);
        return animatedItem.animationSystem;
    }

    public static void tickAnimatedItems(){
        var iterator = ANIMATED_ITEMS.entrySet().iterator();
        while (iterator.hasNext()){
            var item = iterator.next();
            AnimatedItem animatedItem = item.getValue();
            animatedItem.tick();
            if (animatedItem.shouldBeRemoved()){
                iterator.remove();
            }
        }
    }

    public static void tellItemThatItIsAlive(ItemStack itemStack){
        if (ANIMATED_ITEMS.containsKey(itemStack)){
            var item = ANIMATED_ITEMS.get(itemStack);
            item.tellThatIAmAlive();
        }else{
            ANIMATED_ITEMS.put(itemStack, new AnimatedItem(itemStack));
        }
    }

    private static class AnimatedItem {

        public FDItemAnimationSystem animationSystem;
        public ItemStack itemStack;
        public int age;

        public AnimatedItem(ItemStack itemStack){
            this.itemStack = itemStack;
            this.animationSystem = new FDItemAnimationSystem();
        }

        public void tick(){
            age++;
            animationSystem.tick();
        }

        public boolean shouldBeRemoved(){
            return age > 400 || itemStack.isEmpty();
        }

        public void tellThatIAmAlive(){
            this.age = 0;
        }

    }

}
