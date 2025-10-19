package com.finderfeed.fdlib.systems.broken_screen_effect;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;


/**
 * Shattered screen data texture format:
 * <p>
 * Red corresponds to rotation angle (0-255 -> 0-360) of a (0,1) vector (up). This vector denotes in which direction the screen will be shifted in this pixel.
 * <p>
 * Green corresponds to strength by how much the screen will be shifted (0-255 -> 0-1)
 * <p>
 * Blue - less than 128 tilt to the left, more than 128 - tilt to the right
 *
 */
public class ShatteredScreenSettings {

    public static final ResourceLocation NULL_LOCATION = FDLib.location("null");

    public static StreamCodec<FriendlyByteBuf, ShatteredScreenSettings> STREAM_CODEC = StreamCodec.composite(
            FDByteBufCodecs.RESOURCE_LOCATION, v->v.shatteredScreenDataTexture,
            FDByteBufCodecs.RESOURCE_LOCATION, v->v.shatteredScreenTexture,
            ByteBufCodecs.INT, v->v.inTime,
            ByteBufCodecs.INT, v->v.stayTime,
            ByteBufCodecs.INT, v->v.outTime,
            ByteBufCodecs.FLOAT, v->v.maxOffset,
            ShatteredScreenSettings::new
    );

    public ResourceLocation shatteredScreenDataTexture;
    public ResourceLocation shatteredScreenTexture;
    public int stayTime;
    public int inTime;
    public int outTime;
    public float maxOffset;

    public ShatteredScreenSettings(ResourceLocation shatteredScreenDataTexture, ResourceLocation shatteredScreenTexture, int inTime, int stayTime, int outTime, float maxOffset){
        this.shatteredScreenTexture = shatteredScreenTexture;
        this.shatteredScreenDataTexture = shatteredScreenDataTexture;
        this.inTime = inTime;
        this.outTime = outTime;
        this.maxOffset = maxOffset;
        this.stayTime = stayTime;
    }

    public ShatteredScreenSettings(ResourceLocation shatteredScreenDataTexture, int inTime, int stayTime, int outTime, float maxOffset){
        this.shatteredScreenTexture = NULL_LOCATION;
        this.shatteredScreenDataTexture = shatteredScreenDataTexture;
        this.inTime = inTime;
        this.outTime = outTime;
        this.maxOffset = maxOffset;
        this.stayTime = stayTime;
    }

}
