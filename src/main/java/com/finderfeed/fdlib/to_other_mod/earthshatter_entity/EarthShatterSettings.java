package com.finderfeed.fdlib.to_other_mod.earthshatter_entity;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EarthShatterSettings implements AutoSerializable {

    public static StreamCodec<FriendlyByteBuf,EarthShatterSettings> NETWORK_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,t->t.upDistance,
            ByteBufCodecs.INT,t->t.upTime,
            ByteBufCodecs.INT,t->t.stayTime,
            ByteBufCodecs.INT,t->t.downTime,
            ByteBufCodecs.VECTOR3F,t->new Vector3f((float)t.direction.x,(float)t.direction.y,(float)t.direction.z),
            EarthShatterSettings::new
    );

    @SerializableField
    public float upDistance = 0.5f;

    @SerializableField
    public int upTime = 2;

    @SerializableField
    public int stayTime = 10;

    @SerializableField
    public int downTime = 10;

    @SerializableField
    public Vec3 direction = new Vec3(0,1,0);

    @SerializableField
    public EarthShatterSettings s;

    private EarthShatterSettings(float upDistance,int upTime,int stayTime,int downTime,Vector3f direction){
        this.upDistance = upDistance;
        this.upTime = upTime;
        this.stayTime = stayTime;
        this.downTime = downTime;
        this.direction = new Vec3(direction.x,direction.y,direction.z);
        s = new EarthShatterSettings(2);
    }

    public EarthShatterSettings(){
        s = new EarthShatterSettings(2);
    }

    public EarthShatterSettings(int test){
        s = null;
    }

    public static Builder builder(){
        return new Builder();
    }

    public int getLifetime(){
        return this.upTime + this.stayTime + this.downTime;
    }

    public static class Builder{

        private EarthShatterSettings shatterSettings;

        public Builder(){
            shatterSettings = new EarthShatterSettings();
        }

        public Builder upTime(int upTime){
            this.shatterSettings.upTime = upTime;
            return this;
        }

        public Builder downTime(int downTime){
            this.shatterSettings.downTime = downTime;
            return this;
        }

        public Builder stayTime(int stayTime){
            this.shatterSettings.stayTime = stayTime;
            return this;
        }

        public Builder direction(Vec3 direction){
            this.shatterSettings.direction = direction.normalize();
            return this;
        }
        public Builder direction(double x,double y,double z){
            return this.direction(new Vec3(x,y,z));
        }

        public EarthShatterSettings build(){
            return this.shatterSettings;
        }

    }
}
