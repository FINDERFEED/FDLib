package com.finderfeed.fdlib.systems.test;

import com.finderfeed.fdlib.init.FDModels;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.FDLayers;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.entity_types.FDLivingEntity;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class FDTestEntity extends FDLivingEntity {
    public FDTestEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        this.getModel().getModelPartTransformation("core");
    }



    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public FDModelInfo getModelBase() {
        return FDModels.TEST.get();
    }

    @Override
    public FDLayers getLayerInfos() {
        return new FDLayers()
                .addLayer("test",FDModels.TEST2.get())
                ;
    }
}
