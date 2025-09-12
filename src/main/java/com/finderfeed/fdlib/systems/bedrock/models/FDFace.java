package com.finderfeed.fdlib.systems.bedrock.models;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import org.joml.Vector3f;

public class FDFace {

    public static NetworkCodec<FDFace> CODEC = NetworkCodec.composite(
            NetworkCodec.VECTOR3F,face->face.normal,
            FDVertex.CODEC,face->face.vertices[0],
            FDVertex.CODEC,face->face.vertices[1],
            FDVertex.CODEC,face->face.vertices[2],
            FDVertex.CODEC,face->face.vertices[3],
            FDFace::new
    );


    private FDVertex[] vertices = new FDVertex[4];
    private Vector3f normal;


    protected FDFace(Vector3f normal, FDVertex... vertex){
        this.normal = new Vector3f(normal);
        vertices[0] = vertex[0];
        vertices[1] = vertex[1];
        vertices[2] = vertex[2];
        vertices[3] = vertex[3];
    }

    protected FDFace(Vector3f normal, FDVertex v1,FDVertex v2,FDVertex v3,FDVertex v4){
        this.normal = new Vector3f(normal);
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
        vertices[3] = v4;
    }


    public FDVertex[] getVertices() {
        return vertices;
    }

    public Vector3f getNormal() {
        return normal;
    }
}
