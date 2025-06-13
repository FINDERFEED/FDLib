package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments;

import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class BaseModelAttachmentData implements INBTSerializable<CompoundTag> {

    public static final StreamCodec<? super RegistryFriendlyByteBuf, BaseModelAttachmentData> CODEC = FDByteBufCodecs.composite(
            ByteBufCodecs.FLOAT,v->v.translationX,
            ByteBufCodecs.FLOAT,v->v.translationY,
            ByteBufCodecs.FLOAT,v->v.translationZ,

            ByteBufCodecs.FLOAT,v->v.rotationX,
            ByteBufCodecs.FLOAT,v->v.rotationY,
            ByteBufCodecs.FLOAT,v->v.rotationZ,

            ByteBufCodecs.FLOAT,v->v.scaleX,
            ByteBufCodecs.FLOAT,v->v.scaleY,
            ByteBufCodecs.FLOAT,v->v.scaleZ,
            (tx,ty,tz,rx,ry,rz,sx,sy,sz)->{
                BaseModelAttachmentData b = new BaseModelAttachmentData();
                b.translationX = tx;
                b.translationY = ty;
                b.translationZ = tz;

                b.rotationX = rx;
                b.rotationY = ry;
                b.rotationZ = rz;

                b.scaleX = sx;
                b.scaleY = sy;
                b.scaleZ = sz;

                return b;
            }
    );

    private float translationX = 0;
    private float translationY = 0;
    private float translationZ = 0;

    private float rotationX = 0;
    private float rotationY = 0;
    private float rotationZ = 0;

    private float scaleX = 1;
    private float scaleY = 1;
    private float scaleZ = 1;

    public BaseModelAttachmentData translation(float x,float y,float z){
        this.translationX = x;
        this.translationY = y;
        this.translationZ = z;
        return this;
    }

    public BaseModelAttachmentData rotation(float x,float y,float z){
        this.rotationX = x;
        this.rotationY = y;
        this.rotationZ = z;
        return this;
    }

    public BaseModelAttachmentData scale(float x,float y,float z){
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
        return this;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {

        CompoundTag tag = new CompoundTag();

        tag.putFloat("tx",translationX);
        tag.putFloat("ty",translationY);
        tag.putFloat("tz",translationZ);

        tag.putFloat("rx",rotationX);
        tag.putFloat("ry",rotationY);
        tag.putFloat("rz",rotationZ);

        tag.putFloat("sx",scaleX);
        tag.putFloat("sy",scaleY);
        tag.putFloat("sz",scaleZ);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

        this.translationX = nbt.getFloat("tx");
        this.translationY = nbt.getFloat("ty");
        this.translationZ = nbt.getFloat("tz");

        this.rotationX = nbt.getFloat("rx");
        this.rotationY = nbt.getFloat("ry");
        this.rotationZ = nbt.getFloat("rz");

        this.scaleX = nbt.getFloat("sx");
        this.scaleY = nbt.getFloat("sy");
        this.scaleZ = nbt.getFloat("sz");

    }

    public float getTranslationX() {
        return translationX;
    }

    public float getTranslationY() {
        return translationY;
    }

    public float getTranslationZ() {
        return translationZ;
    }

    public float getRotationX() {
        return rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }
}
