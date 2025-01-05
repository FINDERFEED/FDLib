package com.finderfeed.fdlib.systems.screen.screen_particles;

import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

public abstract class FDScreenParticle<T extends FDScreenParticle<T>> {

    //position
    private double x;
    private double y;

    private double xo;
    private double yo;

    //speed
    private double xd;
    private double yd;

    //acceleration
    private double xa;
    private double ya;

    private float roll;
    private float oRoll;
    private float rollSpeed;

    private double friction = 1f;

    private boolean removed = false;

    private int lifetime;
    private int age;


    public void tick(){
        if (age++ > lifetime){
            this.setRemoved(true);
            return;
        }

        this.xo = x;
        this.yo = y;

        this.xd += xa;
        this.yd += ya;

        this.xd *= friction;
        this.yd *= friction;

        this.x += xd;
        this.y += yd;

        this.oRoll = roll;
        this.roll += rollSpeed;
    }

    public abstract void render(GuiGraphics graphics, BufferBuilder vertex, float partialTicks);

    public abstract ParticleRenderType getParticleRenderType();


    public T setRoll(float roll,boolean updateOlds){
        this.roll = roll;
        if (updateOlds) {
            this.oRoll = roll;
        }
        return (T) this;
    }

    public T setRollSpeed(float rollSpeed){
        this.rollSpeed = rollSpeed;
        return (T) this;
    }

    public T setPos(double x,double y, boolean updateOlds){
        this.x = x;
        this.y = y;
        if (updateOlds) {
            this.xo = x;
            this.yo = y;
        }
        return (T) this;
    }

    public T setPos(Vector2f v,boolean updateOlds){
        return this.setPos(v.x,v.y,updateOlds);
    }

    public T setLifetime(int lifetime){
        this.lifetime = lifetime;
        return (T) this;
    }

    public T setAcceleration(double xa,double ya){
        this.xa = xa;
        this.ya = ya;
        return (T) this;
    }
    public T setAcceleration(Vector2f v){
        return this.setAcceleration(v.x,v.y);
    }

    public T setFriction(double friction){
        this.friction = friction;
        return (T) this;
    }

    public T sendToEngine(){
        FDScreenParticleEngine.addParticle(this);
        return (T) this;
    }

    public double getX(float partialTicks){
        return Mth.lerp(partialTicks,this.getXo(),this.getX());
    }

    public double getY(float partialTicks){
        return Mth.lerp(partialTicks,this.getYo(),this.getY());
    }

    public float getRoll(float partialTicks){
        return Mth.lerp(partialTicks,this.getoRoll(),this.getRoll());
    }

    public float getRoll() {
        return roll;
    }

    public float getoRoll() {
        return oRoll;
    }

    public float getRollSpeed() {
        return rollSpeed;
    }

    public double getX() {
        return x;
    }

    public double getXo() {
        return xo;
    }

    public double getXa() {
        return xa;
    }

    public double getXd() {
        return xd;
    }

    public double getY() {
        return y;
    }

    public double getYa() {
        return ya;
    }

    public double getYd() {
        return yd;
    }

    public double getYo() {
        return yo;
    }

    public double getFriction() {
        return friction;
    }

    public int getAge() {
        return age;
    }

    public int getLifetime() {
        return lifetime;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
