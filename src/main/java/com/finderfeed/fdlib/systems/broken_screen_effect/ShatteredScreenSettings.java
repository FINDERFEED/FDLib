package com.finderfeed.fdlib.systems.broken_screen_effect;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
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
    public static final ResourceLocation DATA_1_1 = FDLib.location("textures/gui/effects/broken_screen_1_data.png");
    public static final ResourceLocation DATA_1_GLASSY = FDLib.location("textures/gui/effects/broken_screen_1_data_2.png");
    public static final ResourceLocation DATA_2 = FDLib.location("textures/gui/effects/broken_screen_1_data_2.png");
    public static final ResourceLocation SCREEN_1 = FDLib.location("textures/gui/effects/broken_screen_1.png");
    public static final ResourceLocation SCREEN_2 = FDLib.location("textures/gui/effects/broken_screen_2.png");

    public static NetworkCodec<ShatteredScreenSettings> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.RESOURCE_LOCATION, v -> v.shatteredScreenDataTexture,
            NetworkCodec.RESOURCE_LOCATION, v -> v.shatteredScreenTexture,
            NetworkCodec.INT, v -> v.inTime,
            NetworkCodec.INT, v -> v.stayTime,
            NetworkCodec.INT, v -> v.outTime,
            NetworkCodec.FLOAT, v -> v.maxOffset,
            NetworkCodec.FLOAT, v -> v.chromaticAbberationStrength,
            NetworkCodec.BOOL, v -> v.onScreen,
            ShatteredScreenSettings::new
    );

    public ResourceLocation shatteredScreenDataTexture;
    public ResourceLocation shatteredScreenTexture;
    public int stayTime;
    public int inTime;
    public int outTime;
    public float maxOffset;
    public float chromaticAbberationStrength;
    public boolean onScreen;

    public ShatteredScreenSettings(ResourceLocation shatteredScreenDataTexture, ResourceLocation shatteredScreenTexture, int inTime, int stayTime, int outTime, float maxOffset, float chromaticAbberationStrength, boolean onScreen) {
        this.shatteredScreenTexture = shatteredScreenTexture;
        this.shatteredScreenDataTexture = shatteredScreenDataTexture;
        this.inTime = inTime;
        this.outTime = outTime;
        this.maxOffset = maxOffset;
        this.stayTime = stayTime;
        this.chromaticAbberationStrength = chromaticAbberationStrength;
        this.onScreen = onScreen;
    }

    public ShatteredScreenSettings(ResourceLocation shatteredScreenDataTexture, int inTime, int stayTime, int outTime, float maxOffset, float chromaticAbberationStrength, boolean onScreen) {
        this.shatteredScreenTexture = NULL_LOCATION;
        this.shatteredScreenDataTexture = shatteredScreenDataTexture;
        this.inTime = inTime;
        this.outTime = outTime;
        this.maxOffset = maxOffset;
        this.stayTime = stayTime;
        this.chromaticAbberationStrength = chromaticAbberationStrength;
        this.onScreen = onScreen;
    }

    public ShatteredScreenSettings() {
        this.shatteredScreenDataTexture = DATA_1_GLASSY;
        this.shatteredScreenTexture = NULL_LOCATION;
        this.stayTime = 0;
        this.inTime = 0;
        this.outTime = 20;
        this.maxOffset = 0.1f;
        this.chromaticAbberationStrength = 0.05f;
        this.onScreen = true;
    }


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private ShatteredScreenSettings settings = new ShatteredScreenSettings();

        public Builder() {}

        public Builder setDataTexture(ResourceLocation resourceLocation) {
            settings.shatteredScreenDataTexture = resourceLocation;
            return this;
        }

        public Builder setOverlayTexture(ResourceLocation resourceLocation) {
            settings.shatteredScreenTexture = resourceLocation;
            return this;
        }

        public Builder time(int inTime, int stayTime, int outTime) {
            settings.inTime = inTime;
            settings.stayTime = stayTime;
            settings.outTime = outTime;
            return this;
        }

        public Builder setMaxOffset(float maxOffset) {
            settings.maxOffset = maxOffset;
            return this;
        }

        public Builder setChromaticAbberationStrength(float strength) {
            settings.chromaticAbberationStrength = strength;
            return this;
        }

        public Builder onScreen(boolean onScreen) {
            settings.onScreen = onScreen;
            return this;
        }

        public ShatteredScreenSettings build() {
            return settings;
        }
    }


}
