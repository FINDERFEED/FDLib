package com.finderfeed.fdlib.systems.screen;

import java.util.function.BiFunction;

public enum Anchor {
    TOP_LEFT((cmp,x)->x,(cmp,y)->y),
    TOP_RIGHT((cmp,x)->x + cmp.getWidth(),(cmp,y)->y),
    BOTTOM_RIGHT((cmp,x)->x,(cmp,y)->y + cmp.getHeight()),
    BOTTOM_LEFT((cmp,x)->x + cmp.getWidth(),(cmp,y)->y + cmp.getHeight()),
    TOP_CENTER((cmp,x)->x + cmp.getWidth() / 2,(cmp,y)->y),
    BOTTOM_CENTER((cmp,x)->x + cmp.getWidth() / 2,(cmp,y)->y + cmp.getHeight()),
    RIGHT_CENTER((cmp,x)->x  + cmp.getWidth(),(cmp,y)->y + cmp.getHeight() / 2),
    LEFT_CENTER((cmp,x)->x,(cmp,y)->y + cmp.getHeight() / 2),
    CENTER((cmp,x)->x + cmp.getWidth() / 2,(cmp,y)->y + cmp.getHeight() / 2),
    ;


    BiFunction<FDScreenComponent,Float,Float> xTransform;
    BiFunction<FDScreenComponent,Float,Float> yTransform;

    Anchor(BiFunction<FDScreenComponent,Float,Float> xTransform,BiFunction<FDScreenComponent,Float,Float> yTransform){
        this.xTransform = xTransform;
        this.yTransform = yTransform;
    }

    public float getX(FDScreenComponent component,float x){
        return xTransform.apply(component,x);
    }

    public float getY(FDScreenComponent component,float y){
        return yTransform.apply(component,y);
    }

}
