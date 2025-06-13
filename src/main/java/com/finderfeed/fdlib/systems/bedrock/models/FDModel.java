package com.finderfeed.fdlib.systems.bedrock.models;

import com.finderfeed.fdlib.systems.bedrock.models.model_render_info.IFDModelAdditionalInfo;
import com.finderfeed.fdlib.util.math.RaycastUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;

public class FDModel {


    private ResourceLocation modelName;

    public FDModelPart main;
    private Map<String,FDModelPart> partsLookup;

    public FDModel(FDModelInfo info){
        this.modelName = info.getModelName();
        this.load(info.partDefinitionList);
    }

    public void render(PoseStack matrices, VertexConsumer vertex, int light, int overlay, float r, float g, float b, float a){
        this.main.render(matrices,vertex,light,overlay,r,g,b,a);
    }



    public Vector3f traceCubesInModelPart(Vector3f offset,Vector3f p1,Vector3f p2,String name){
        FDModelPart part = partsLookup.get(name);
        Matrix4f transformation = getModelPartTransformation(part);
        List<Vector3f> vs = new ArrayList<>();
        for (FDCube cube : part.cubes){
            for (FDFace fdFace : cube.getFaces()){
                List<Vector3f> vertices = Arrays.stream(fdFace.getVertices()).map(fdVertex -> {
                    Vector3f v = fdVertex.getPosition();
                    Vector4f t = transformation.transform(new Vector4f(v.x/16f,v.y/16f,v.z/16f,1));
                    return new Vector3f(t.x + offset.x,t.y + offset.y,t.z + offset.z);
                }).toList();

                Matrix3f m = transformation.normal(new Matrix3f());
                Vector3f normal = m.transform(fdFace.getNormal(),new Vector3f());

                if (vertices.size() == 4){
                    Vector3f tracedPoint = RaycastUtil.traceInfinitePlane(vertices.get(0),normal.mul(-1,new Vector3f()),p1,p2);
                    if (RaycastUtil.isPointOnLine(p1,p2,tracedPoint,0.005f)){
                        if (RaycastUtil.isPointInSquare(vertices.get(0),vertices.get(1),vertices.get(2),vertices.get(3),tracedPoint)) {
                            vs.add(tracedPoint);
                        }
                    }
                }

            }
        }
        if (!vs.isEmpty()) {
            vs.sort(Comparator.comparingDouble(v -> {
                return v.sub(p1, new Vector3f()).length();
            }));
            return vs.get(0);
        }else{
            return null;
        }
    }

    public Matrix4f getModelPartTransformation(String name){
        FDModelPart part = partsLookup.get(name);
        if (part == null){throw new RuntimeException("No such part exist.");}
        return getModelPartTransformation(part);
    }

    public Matrix4f getModelPartTransformation(FDModelPart part){
        Matrix4f mat = new Matrix4f();
        if (part.parent != null){
            this.collectParentTransformations(part,mat);
        }else{
            part.transform(mat);
        }
        return mat;
    }

    public Vector3f transformPoint(String boneName,Vector3f point){
        Vector4f p = new Vector4f(point.x,point.y,point.z,1);
        this.getModelPartTransformation(boneName).transform(p);
        return new Vector3f(p.x,p.y,p.z);
    }


    private void collectParentTransformations(FDModelPart part,Matrix4f mat){
        List<FDModelPart> parts = new ArrayList<>();
        FDModelPart p = part;
        while (p != null){
            parts.add(p);
            p = p.parent;
        }
        for (int i = parts.size() - 1; i >= 0;i--){
            p = parts.get(i);
            p.transform(mat);
        }
    }

    public FDModelPart getModelPart(String name){
        FDModelPart part;
        if ((part = partsLookup.get(name)) != null) {
            return part;
        }else{
            throw new RuntimeException("Couldn't find part: \"" + name + "\" in model: " + modelName);
        }
    }

    public FDModelPart getPartOrNull(String name){
        return this.partsLookup.get(name);
    }

    public void resetTransformations(){
        this.main.reset();
    }


    private void load(List<FDModelPartDefinition> definitions){
        Map<String,FDModelPart> allParts = new HashMap<>();
        Map<String,String> childParent = new HashMap<>();
        List<FDModelPart> noParentsParts = new ArrayList<>();
        for (FDModelPartDefinition definition : definitions){
            List<FDCube> cubes = definition.cubes;
            String name = definition.name;
            String parent = definition.parent;
            Vector3f rotation = definition.initRotation;
            Vector3f pivot = definition.pivot;
            FDModelPart part = new FDModelPart(name,allParts.get(parent),cubes,pivot,rotation);
            allParts.put(name,part);
            if (!parent.equals("")){
                childParent.put(name,parent);
            }else{
                noParentsParts.add(part);
            }
        }
        for (var entry : childParent.entrySet()){
            String sname = entry.getKey();
            String sparent = entry.getValue();
            FDModelPart parent = allParts.get(sparent);
            FDModelPart child = allParts.get(sname);
            parent.children.put(child.name,child);
        }
        this.main = new FDModelPart("root",null,new ArrayList<>(),new Vector3f(),new Vector3f());
        noParentsParts.forEach(part->{
            main.children.put(part.name,part);
            part.parent = main;
        });
        this.partsLookup = allParts;
    }

    public ResourceLocation getModelInfoId() {
        return modelName;
    }

}
