package com.finderfeed.fdlib.systems.screen;

import java.lang.annotation.Annotation;

public abstract class ValueComponent<T> extends FDScreenComponent {


    public ValueComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

    /**
     * Automatically sets width of the component if false to fit the editor window
     */
    public abstract boolean isWidthFixed();

    public abstract void setValue(T value);

    public abstract T getValue();

    /**
     * Iterates through all annotations on the field
     */
    public abstract void applyOptions(FDEditorComponent owner,Annotation annotation);

}
