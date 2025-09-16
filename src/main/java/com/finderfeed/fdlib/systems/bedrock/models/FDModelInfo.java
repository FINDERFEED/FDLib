package com.finderfeed.fdlib.systems.bedrock.models;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FDModelInfo {

    private ResourceLocation modelName;
    private float cubeScale;
    public List<FDModelPartDefinition> partDefinitionList = new ArrayList<>();

    /**
     * Location is automatically pointing to assets/bedrock/models/ you just need to specify the file (without geo json bla bla bla)
     */
    public FDModelInfo(ResourceLocation location,float cubeScale){
        this.modelName = location;
        this.cubeScale = cubeScale;
    }

    public void load(JsonObject model){
        this.parseModel(model,this.cubeScale);
    }

    public ResourceLocation getModelName() {
        return modelName;
    }

    public float getCubeScale() {
        return cubeScale;
    }

    private void parseModel(JsonObject bmodel, float cubeScale){
        this.partDefinitionList.clear();
        JsonObject jmodel = bmodel.getAsJsonArray("minecraft:geometry")
                .get(0).getAsJsonObject();
        JsonObject description = jmodel.getAsJsonObject("description");
        int texWidth = description.get("texture_width").getAsInt();
        int texHeight = description.get("texture_height").getAsInt();
        JsonArray parts = jmodel.getAsJsonArray("bones");

        Map<String,Vector3f> pivotOffsets = new HashMap<>();

        for (JsonElement epart : parts){
            JsonObject part = epart.getAsJsonObject();
            String name = JsonHelper.getString(part,"name");
            String parent = JsonHelper.getString(part,"parent");
            Vector3f pivot = JsonHelper.parseVector3f(part,"pivot").mul(-1,1,1,new Vector3f());
            Vector3f rotation = JsonHelper.parseVector3f(part,"rotation").mul(-1,-1,1,new Vector3f());
            List<FDCube> cubes;
            if (part.has("cubes")){
                cubes = this.parseCubes(part.getAsJsonArray("cubes"),pivot,texWidth,texHeight,cubeScale);
            }else{
                cubes = new ArrayList<>();
            }

            Vector3f v = pivotOffsets.get(parent);
            if (v == null){
                v = new Vector3f();
            }
            pivotOffsets.put(name,new Vector3f(pivot));
            pivot.sub(v);

            FDModelPartDefinition definition = new FDModelPartDefinition(cubes,name,parent,rotation,pivot);
            this.partDefinitionList.add(definition);
        }
    }

    private List<FDCube> parseCubes(JsonArray array,Vector3f bonePivot,int texWidth,int texHeight,float cubeScale){
        List<FDCube> cubes = new ArrayList<>();
        for (JsonElement element : array){
            cubes.add(FDCube.fromJson(element.getAsJsonObject(),bonePivot,texWidth,texHeight,cubeScale));
        }
        return cubes;
    }


    public record ModelSyncInstance(ResourceLocation location,List<FDModelPartDefinition> definitions){

        public static final NetworkCodec<ModelSyncInstance> CODEC = NetworkCodec.composite(
                NetworkCodec.STRING,inst->inst.location.toString(),
                NetworkCodec.listOf(FDModelPartDefinition.CODEC),inst->inst.definitions,
                (str,defs)->{
                    return new ModelSyncInstance(ResourceLocation.tryParse(str),defs);
                }
        );

    }

//    private JsonElement loadJsonModel(ResourceLocation location) {
//
//
//        ResourceManager manager = Minecraft.getInstance().getResourceManager();
//        Optional<Resource> modelJson = manager.getResource(location);
//        try{
//            if (modelJson.isPresent()){
//
//                BufferedReader stream = modelJson.get().openAsReader();
//                JsonElement element = GSON.fromJson(stream,JsonElement.class);
//                stream.close();
//                return element;
//            }else{
//                throw new RuntimeException("Couldn't find model: " + location);
//            }
//        }catch (Exception e){
//            throw new RuntimeException("Error loading model file: " + location,e);
//        }
//    }
}
