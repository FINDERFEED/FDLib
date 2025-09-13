package com.finderfeed.fdlib.systems.screen;

import com.finderfeed.fdlib.FDHelpers;
import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.systems.screen.annotations.FDName;
import com.finderfeed.fdlib.systems.screen.annotations.VComponent;
import com.finderfeed.fdlib.systems.screen.default_components.misc.DraggableComponent;
import com.finderfeed.fdlib.systems.screen.default_components.misc.FDVerticalComponentContainer;
import com.finderfeed.fdlib.systems.screen.default_components.text.FDSimpleText;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FDEditorComponent extends FDScreenComponent {

    public static float OFFSET = 2;
    public static float DRAGGABLE_COMPONENT_HEIGHT = 10;
    public FDScreenComponent editing;
    public List<Pair<Pair<Field,Object>,ValueComponent<?>>> valueComponents;

    public FDEditorComponent(FDScreenComponent editing,FDScreen screen, String uniqueId, float x, float y) {
        super(screen, uniqueId, x, y, 0,0);
        this.valueComponents = new ArrayList<>();
        this.initialize(editing);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        FDRenderUtil.fill(graphics.pose(),x,y,this.getWidth(),this.getHeight(),0f,0f,0f,1f);
        FDRenderUtil.fill(graphics.pose(),x,y,this.getWidth(),DRAGGABLE_COMPONENT_HEIGHT,0.5f,0.5f,0.5f,1f);
    }

    private void initialize(FDScreenComponent editing){
        this.editing = editing;
        this.valueComponents.clear();
        this.getChildren().removeAllChildren();
        List<FDHelpers.ClassFields> fields = FDHelpers.collectAllAnnotatedFieldsInClass(editing.getClass(), VComponent.class);
        int id = 0;
        float defaultOffset = OFFSET;
        float fullHeight = defaultOffset + DRAGGABLE_COMPONENT_HEIGHT;
        float componentContainerHeight = 100;
        FDVerticalComponentContainer attributesContainer = new FDVerticalComponentContainer(this.getScreen(),this.getUniqueId() + "_attributes",
                defaultOffset,fullHeight,componentContainerHeight);

        float defaultWidth = 100;
        float maxFixedWidth = defaultWidth;
        List<FDScreenComponent> nonFixedWidthComponents = new ArrayList<>();
        for (int i = fields.size() - 1; i >= 0;i--){
            FDHelpers.ClassFields classfields = fields.get(i);
            for (int g = 0; g < classfields.fields().size();g++) {
                Field field = classfields.fields().get(g);
                field.setAccessible(true);
                VComponent component = field.getAnnotation(VComponent.class);
                Class<? extends ValueComponent<?>> clazz = component.value();
                Constructor<? extends ValueComponent<?>> constructor;
                boolean hasFieldContext = false;
                try {
                    constructor = clazz.getConstructor(FDScreen.class, String.class);

                } catch (NoSuchMethodException e) {
                    try {
                        var fieldConstructor = clazz.getConstructor(Field.class, FDScreen.class, String.class);
                        constructor = fieldConstructor;
                        hasFieldContext = true;
                    } catch (NoSuchMethodException e1) {
                        throw new RuntimeException("No constructor was found in value component class " + clazz + " with parameters:" +
                                "(Optional) Field, FDScreen, String", e1);
                    }
                }
                ValueComponent<?> cmp;
                try {
                    if (!hasFieldContext) {
                        cmp = constructor.newInstance(this.getScreen(), this.getUniqueId() + id);
                    } else {
                        cmp = constructor.newInstance(field, this.getScreen(), this.getUniqueId() + id);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                float width;
                if (cmp.isWidthFixed()) {
                    width = cmp.getWidth();
                    maxFixedWidth = Math.max(width, maxFixedWidth);
                } else {
                    width = defaultWidth;
                    nonFixedWidthComponents.add(cmp);
                }

                cmp.setWidth(width);

                Component name = Component.literal(field.getName());
                FDName n = field.getAnnotation(FDName.class);
                if (n != null) name = Component.translatable(n.value());
                for (Annotation annotation : field.getAnnotations()) {
                    cmp.applyOptions(this, annotation);
                }
                Object o = null;
                try {
                    setDefaultComponentValue(cmp, o = field.get(editing));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.valueComponents.add(new Pair<>(new Pair<>(field, o), cmp));

                FDSimpleText text = new FDSimpleText(this.getScreen(), this.getUniqueId() + "_text" + id, 0, 0, name, 1f);
                maxFixedWidth = Math.max(maxFixedWidth, text.getWidth());
                attributesContainer.getChildren().setAsChild(text);
                attributesContainer.getChildren().setAsChild(cmp);


                id++;
            }
        }

        for (FDScreenComponent component : nonFixedWidthComponents){
            component.setWidth(maxFixedWidth);
        }

        attributesContainer.setWidth(attributesContainer.getWidth() + FDVerticalComponentContainer.SCROLLER_WIDTH);
        this.setWidth(attributesContainer.getWidth() + defaultOffset * 2);
        this.setHeight(attributesContainer.getHeight() + fullHeight + defaultOffset);
        DraggableComponent component = new DraggableComponent(this,this.getScreen(),this.getUniqueId() + "_dragging",0,0,this.getWidth(),DRAGGABLE_COMPONENT_HEIGHT);
        this.getChildren().setAsChild(attributesContainer);
        this.getChildren().setAsChild(component);
    }

    @Override
    public void onChildWidthChanged(FDScreenComponent child, float previous, float current) {
        super.onChildWidthChanged(child, previous, current);
        this.setWidth(current + OFFSET * 2);
    }

    @Override
    public void tick() {
        super.tick();
        for (var entry : this.valueComponents){
            Field field = entry.first.first;
            Object previousValue = entry.first.second;
            try {
                ValueComponent<?> component = entry.second;
                Object currentValue = field.get(this.editing);
                if ((currentValue != null && !currentValue.equals(previousValue)) || (previousValue != null && !previousValue.equals(currentValue))){
                    setDefaultComponentValue(component,currentValue);
                    entry.first.second = currentValue;
                }else {
                    Object val = component.getValue();
                    try {
                        field.set(editing, val);
                        entry.first.second = val;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private <T> void setDefaultComponentValue(ValueComponent<T> component,Object defaultVal){
        try {
            component.setValue((T)defaultVal);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
