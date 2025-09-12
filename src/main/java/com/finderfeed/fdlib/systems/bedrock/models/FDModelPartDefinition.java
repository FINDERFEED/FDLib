package com.finderfeed.fdlib.systems.bedrock.models;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import org.joml.Vector3f;

import java.util.List;

public class FDModelPartDefinition {

    public static final NetworkCodec<FriendlyByteBuf,FDModelPartDefinition> CODEC = NetworkCodec.composite(
            FDCube.CODEC.apply(NetworkCodec.list()),part->part.cubes,
            NetworkCodec.STRING_UTF8,part->part.name,
            NetworkCodec.STRING_UTF8,part->part.parent,
            NetworkCodec.VECTOR3F,part->part.initRotation,
            NetworkCodec.VECTOR3F,part->part.pivot,
            FDModelPartDefinition::new
    );


    public List<FDCube> cubes;
    public String parent;
    public Vector3f initRotation;
    public Vector3f pivot;
    public String name;

    public FDModelPartDefinition(List<FDCube> cubes,String name,String parent,Vector3f initRotation,Vector3f pivot){
        this.cubes = cubes;
        this.name = name;
        this.parent = parent;
        this.initRotation = new Vector3f(initRotation);
        this.pivot = new Vector3f(pivot);
    }

}
