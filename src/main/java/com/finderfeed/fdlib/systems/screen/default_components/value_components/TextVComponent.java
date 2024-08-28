package com.finderfeed.fdlib.systems.screen.default_components.value_components;

import com.finderfeed.fdlib.systems.screen.FDEditorComponent;
import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.default_components.text.FDColoredTextBox;
import net.minecraft.network.chat.Component;

import java.lang.annotation.Annotation;

public class TextVComponent extends FDColoredTextBox<Component> {

    public TextVComponent(FDScreen screen, String uniqueId) {
        super(screen, uniqueId,0,0,0,12);
    }

    @Override
    public boolean isWidthFixed() {
        return false;
    }

    @Override
    public void setValue(Component value) {
        this.value = new StringBuilder(value.getString());
    }

    @Override
    public Component getValue() {
        return Component.translatable(this.value.toString());
    }

    @Override
    public void applyOptions(FDEditorComponent owner, Annotation annotation) {

    }

}
