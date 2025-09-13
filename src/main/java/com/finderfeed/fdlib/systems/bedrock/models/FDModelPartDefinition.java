package com.finderfeed.fdlib.systems.bedrock.models;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import org.joml.Vector3f;

import java.util.List;

public class FDModelPartDefinition {

    public static final NetworkCodec<FDModelPartDefinition> CODEC = NetworkCodec.composite(
            NetworkCodec.listOf(FDCube.CODEC),part->part.cubes,
            NetworkCodec.STRING,part->part.name,
            NetworkCodec.STRING,part->part.parent,
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
