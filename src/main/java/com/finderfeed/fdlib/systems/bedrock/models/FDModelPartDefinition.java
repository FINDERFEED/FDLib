package com.finderfeed.fdlib.systems.bedrock.models;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;

import java.util.List;

public class FDModelPartDefinition {

    public static final StreamCodec<FriendlyByteBuf,FDModelPartDefinition> CODEC = StreamCodec.composite(
            FDCube.CODEC.apply(ByteBufCodecs.list()),part->part.cubes,
            ByteBufCodecs.STRING_UTF8,part->part.name,
            ByteBufCodecs.STRING_UTF8,part->part.parent,
            ByteBufCodecs.VECTOR3F,part->part.initRotation,
            ByteBufCodecs.VECTOR3F,part->part.pivot,
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
