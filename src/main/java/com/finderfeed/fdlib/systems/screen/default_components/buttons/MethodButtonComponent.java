package com.finderfeed.fdlib.systems.screen.default_components.buttons;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.screen.FDScreen;

import java.lang.reflect.Method;

public class MethodButtonComponent extends TexturedButtonComponent {


    public String method;

    public MethodButtonComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height,(screen1,btn,mx,my)->{
            Class<? extends FDScreen> clazz = screen1.getClass();
            String mth = ((MethodButtonComponent)btn).method;
            try {
                Method m = clazz.getDeclaredMethod(mth,btn.getClass());
                m.invoke(null,btn);
                return true;
            }catch (Exception e){
                FDLib.LOGGER.error("Static method " + mth + " not found in class " + clazz);
                return false;
            }
        });
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
