package com.finderfeed.fdlib.systems.bedrock.models;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import org.joml.Vector3f;

public class FDVertex {

    public static NetworkCodec<FDVertex> CODEC = NetworkCodec.composite(
            NetworkCodec.FLOAT,vertex->vertex.u,
            NetworkCodec.FLOAT,vertex->vertex.v,
            NetworkCodec.VECTOR3F,vertex->vertex.position,
            FDVertex::new
    );

    private float u;
    private float v;

    private Vector3f position;


    protected FDVertex(float u,float v,Vector3f position){
        this.u = u;
        this.v = v;
        this.position = new Vector3f(position);
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public Vector3f getPosition() {
        return position;
    }
}
