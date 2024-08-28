package com.finderfeed.fdlib.systems.screen.default_components.buttons.selector_button;

import com.finderfeed.fdlib.systems.screen.FDEditorComponent;
import com.finderfeed.fdlib.systems.screen.FDScreen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EnumSelectorButton extends MainSelectorButton<Enum<?>> {

    protected Class<? extends Enum<?>> enumClass;

    public EnumSelectorButton(Field field, FDScreen screen, String uniqueId) {
        super(field, screen, uniqueId);
        this.enumClass = (Class<? extends Enum<?>>) field.getType();
    }

    @Override
    public String valueToString(Enum<?> value) {
        return value.name();
    }

    @Override
    public List<Enum<?>> values() {
        return (List<Enum<?>>) Arrays.stream(enumClass.getEnumConstants()).toList();
    }

    @Override
    public Enum<?> getValue() {
        return this.value;
    }

    @Override
    public void applyOptions(FDEditorComponent owner, Annotation annotation) {

    }
}
