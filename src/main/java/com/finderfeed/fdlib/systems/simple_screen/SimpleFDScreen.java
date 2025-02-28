package com.finderfeed.fdlib.systems.simple_screen;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;

public abstract class SimpleFDScreen extends Screen {

    public float relX;
    public float relY;


    public SimpleFDScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();

        Window window = Minecraft.getInstance().getWindow();

        float width = window.getGuiScaledWidth();
        float height = window.getGuiScaledHeight();

        this.relX = width / 2 - this.getScreenWidth() / 2;
        this.relY = height / 2 - this.getScreenHeight() / 2;

    }

    public Vector2f getAnchor(float wMod, float hMod){
        Window window = Minecraft.getInstance().getWindow();
        float height = window.getGuiScaledHeight();
        float width = window.getGuiScaledWidth();
        return new Vector2f(width * wMod,height * hMod);
    }

    public Vector2f getCenterAnchor(){
        return this.getAnchor(0.5f,0.5f);
    }

    public Vector2f getUpRightAnchor(){
        return this.getAnchor(1,0);
    }

    public Vector2f getDownRightAnchor(){
        return this.getAnchor(1,1);
    }

    public Vector2f getDownLeftAnchor(){
        return this.getAnchor(0,1);
    }

    public Vector2f getScreenRelativeAnchor(){
        return new Vector2f(relX,relY);
    }

    public abstract float getScreenWidth();

    public abstract float getScreenHeight();



}
