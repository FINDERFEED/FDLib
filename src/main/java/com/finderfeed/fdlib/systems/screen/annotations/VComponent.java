package com.finderfeed.fdlib.systems.screen.annotations;


import com.finderfeed.fdlib.systems.screen.ValueComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a field with that and supply a class that extends ValueComponent
 * It will be used in a component editor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VComponent {

    Class<? extends ValueComponent<?>> value();

}
