package com.finderfeed.fdlib.systems.screen.default_components.text;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import com.finderfeed.fdlib.systems.screen.annotations.FDColor;
import com.finderfeed.fdlib.systems.screen.annotations.FDName;
import com.finderfeed.fdlib.systems.screen.annotations.VComponent;
import com.finderfeed.fdlib.systems.screen.default_components.value_components.FloatVComponent;
import com.finderfeed.fdlib.systems.screen.default_components.value_components.TextVComponent;
import net.minecraft.network.chat.Component;

public abstract class FDTextComponent extends FDScreenComponent {

    @VComponent(TextVComponent.class)
    @FDColor(r=0.25f,g=0.25f,b=0.25f,a=1f)
    @FDName("Translation key")
    public Component component = Component.empty();

    @VComponent(FloatVComponent.class)
    @FDColor(r=0.25f,g=0.25f,b=0.25f,a=1f)
    @FDName("Text scale")
    public float textScale = 1f;

    public FDTextComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

}
