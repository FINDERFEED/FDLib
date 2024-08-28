package com.finderfeed.fdlib.systems.screen.default_components.value_components;

import com.finderfeed.fdlib.systems.screen.FDEditorComponent;
import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.annotations.OnlyPositive;
import com.finderfeed.fdlib.systems.screen.default_components.text.FDColoredTextBox;

import java.lang.annotation.Annotation;

public class FloatVComponent extends FDColoredTextBox<Float> {

    public FloatVComponent(FDScreen screen, String uniqueId) {
        super(screen, uniqueId, 0,0,0,12);
        this.filter = "-?\\d+(.\\d*)?";
    }

    @Override
    public boolean isWidthFixed() {
        return false;
    }

    @Override
    public void setValue(Float value) {
        this.insertText(Float.toString(value));
    }

    @Override
    public Float getValue() {
        if (!this.value.isEmpty()){
            try {
                return Float.parseFloat(this.value.toString());
            }catch (Exception e){
                return 0f;
            }
        }
        return 0f;
    }

    @Override
    public void applyOptions(FDEditorComponent owner, Annotation annotation) {
        super.applyOptions(owner,annotation);
        if (annotation instanceof OnlyPositive){
            this.filter = "\\d+(.\\d*)?";
        }
    }
}
