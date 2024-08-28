package com.finderfeed.fdlib.systems.screen.default_components.value_components;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.default_components.text.FDColoredTextBox;

public class StringVComponent extends FDColoredTextBox<String> {

    public StringVComponent(FDScreen screen, String uniqueId) {
        super(screen, uniqueId, 0,0,0,12);
    }

    @Override
    public boolean isWidthFixed() {
        return false;
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        this.value = new StringBuilder(value);
    }

    @Override
    public String getValue() {
        return this.value.toString();
    }
}
