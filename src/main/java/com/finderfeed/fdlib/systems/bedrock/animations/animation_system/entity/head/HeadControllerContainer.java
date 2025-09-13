package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.head;

import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;

public class HeadControllerContainer<T extends Mob & AnimatedObject & IHasHead<T>> extends LookControl {

    private HashMap<String, HeadController<T>> headControllers = new HashMap<>();

    private Mode mode = Mode.LOOK;

    private T entity;

    public HeadControllerContainer(T entity){
        super(entity);
        this.entity = entity;
    }

    public HeadControllerContainer<T> addHeadController(FDModel model, String headBone){
        this.headControllers.put(headBone, new HeadController<>(this, model,headBone,entity));
        return this;
    }

    public void clientTick(){
        for (var entry : headControllers.entrySet()){
            var value = entry.getValue();
            value.clientTick();
        }
    }

    public HeadController<T> getController(String headBone){
        return headControllers.get(headBone);
    }

    public Mode getControllersMode() {
        return mode;
    }

    public void setControllersMode(Mode mode) {
        if (mode != this.mode) {
            Mode old = this.mode;
            this.mode = mode;
            if (!entity.level().isClientSide){
                FDPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(()->entity),new ChangeHeadControllerModePacket<>(entity, mode));
                return;
            }
            for (var entry : headControllers.entrySet()) {
                var value = entry.getValue();
                value.onControllerModeChanged(old, mode);
            }
        }
    }

    @Override
    public void setLookAt(double x, double y, double z, float p_24954_, float p_24955_) {
        if (!entity.level().isClientSide){

            FDPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(()->entity),new WantedLookCoordinatesPacket(entity,x,y,z));
        }
        super.setLookAt(x, y, z, p_24954_, p_24955_);
    }

    public enum Mode {
        LOOK,
        ANIMATION,
        ANIMATION_AND_LOOK
    }

}
