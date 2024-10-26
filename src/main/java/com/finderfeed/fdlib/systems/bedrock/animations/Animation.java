package com.finderfeed.fdlib.systems.bedrock.animations;

import com.finderfeed.fdlib.shunting_yard.RPNVector3f;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animation {

    public static final String TRANSITION = "transition";
    public static final String TO_NULL_TRANSITION = "transition_to_null";


    private ResourceLocation name;
    private ResourceLocation fileLocation;
    private int animTime;
    private LoopMode defaultLoopMode;
    private HashMap<String,BoneAnimationData> datas;


    public Animation(){}

    public Animation(ResourceLocation fileLocation){
        this.fileLocation = fileLocation;
    }



    public void applyAnimation(AnimationContext context,FDModel model,float elapsedTime,float partialTicks){
        try {
            for (var entry : datas.entrySet()) {
                BoneAnimationData data = entry.getValue();
                data.apply(model, context, elapsedTime, partialTicks);
            }
        }catch (Exception e){
            throw new RuntimeException("Error while applying animation: " + this.getName(),e);
        }
    }



    public Animation createTransitionTo(AnimationContext context, Animation next, float elapsedTime,int toNullTime,float partialTick){
        Animation animation;
        HashMap<String,BoneAnimationData> dataHashMap;
        if (next != null) {
            animation = new TransitionAnimation(next);
            animation.name = ResourceLocation.tryBuild("fdlib", TRANSITION);
            animation.defaultLoopMode = next.getDefaultLoopMode();
            animation.animTime = next.getAnimTime();
            dataHashMap = this.createToAnimationDatas(next,context,toNullTime,elapsedTime,partialTick);
        }else{
            animation = new Animation();
            animation.name = ResourceLocation.tryBuild("fdlib",TO_NULL_TRANSITION);
            animation.defaultLoopMode = LoopMode.ONCE;
            animation.animTime = toNullTime;
            dataHashMap = this.createToNullDatas(context,toNullTime,elapsedTime,partialTick);
        }
        animation.datas = dataHashMap;
        return animation;
    }


    private HashMap<String,BoneAnimationData> createToNullDatas(AnimationContext context,int toNullTime,float elapsedTime,float partialTick){
        HashMap<String,BoneAnimationData> map = new HashMap<>();
        for (var entry : this.datas.entrySet()){
            BoneAnimationData data = entry.getValue().createTransitionData(null,context,toNullTime,elapsedTime,partialTick);
            map.put(entry.getKey(),data);
        }
        return map;
    }

    private HashMap<String,BoneAnimationData> createToAnimationDatas(Animation next,AnimationContext context,int toNullTime,float elapsedTime,float partialTick){
        HashMap<String,BoneAnimationData> map = new HashMap<>(next.datas);
        for (var entry : this.datas.entrySet()){
            var d = next.datas.get(entry.getKey());
            if (d == null && toNullTime == 0){
                continue;
            }
            BoneAnimationData data = entry.getValue().createTransitionData(d,context,toNullTime,elapsedTime,partialTick);
            map.put(entry.getKey(),data);
        }
        return map;
    }


    public boolean isTransition(){
        return name.getPath().equals(TRANSITION);
    }

    public boolean isToNullTransition(){
        return name.getPath().equals(TO_NULL_TRANSITION);
    }

    public LoopMode getDefaultLoopMode() {
        return defaultLoopMode;
    }

    public ResourceLocation getFileLocation() {
        return fileLocation;
    }

    public int getAnimTime() {
        return animTime;
    }

    public ResourceLocation getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Animation that)) return false;
        if (this instanceof TransitionAnimation tthis && that instanceof TransitionAnimation tthat){
            return tthis.getTransitionTo() == tthat.getTransitionTo();
        }else if (this instanceof TransitionAnimation tthis){
            return tthis.getTransitionTo() == that;
        }else if (that instanceof TransitionAnimation tthat){
            return this == tthat.getTransitionTo();
        } else{
            return that == this;
        }
    }

    public void load(ResourceLocation name, JsonObject object){
//        this.loadedJson = object.toString();
        this.name = name;
        LoopMode mode = parseLoopMode(name,object);
        int length = Math.round(object.get("animation_length").getAsFloat() * 20); // to ticks
        this.animTime = length;
        JsonObject bones = object.getAsJsonObject("bones");
        HashMap<String,BoneAnimationData> animationDatas = new HashMap<>();
        for (var entry : bones.entrySet()){
            String boneName = entry.getKey();
            JsonElement bone = entry.getValue();
            BoneAnimationData data = parseData(boneName,bone.getAsJsonObject());
            animationDatas.put(boneName,data);
        }
        this.datas = animationDatas;
        this.defaultLoopMode = mode;
    }

    private BoneAnimationData parseData(String name,JsonObject object){
        List<KeyFrame> positions = null;
        List<KeyFrame> rotations = null;
        List<KeyFrame> scales = null;
        if (object.has("position")) positions = parseKeyFrames(KeyFrameLoadType.POSITION,object.get("position"));
        if (object.has("rotation")) rotations = parseKeyFrames(KeyFrameLoadType.ROTATION,object.get("rotation"));
        if (object.has("scale")) scales = parseKeyFrames(KeyFrameLoadType.SCALE,object.get("scale"));
        BoneAnimationData data = new BoneAnimationData(name,positions,rotations,scales,true);
        return data;
    }

    private List<KeyFrame> parseKeyFrames(KeyFrameLoadType loadType, JsonElement keyFrames){
        List<KeyFrame> frames = new ArrayList<>();
        if (keyFrames.isJsonObject()) {
            Map<String,JsonElement> map = keyFrames.getAsJsonObject().asMap();
            for (var entry : map.entrySet()) {
                int timestamp = Math.round(Float.parseFloat(entry.getKey()) * 20); //to ticks
                JsonElement keyframe = entry.getValue();
                KeyFrame keyFrame = parseKeyFrame(loadType, timestamp, keyframe);
                frames.add(keyFrame);
            }
        }else if (keyFrames.isJsonArray()){
            JsonArray array = keyFrames.getAsJsonArray();
            String v1 = array.get(0).getAsString();
            String v2 = array.get(1).getAsString();
            String v3 = array.get(2).getAsString();
            if (loadType == KeyFrameLoadType.POSITION){
                v1 = "-(" + v1 + ")";
            }else if (loadType == KeyFrameLoadType.ROTATION){
                v1 = "-(" + v1 + ")";
                v2 = "-(" + v2 + ")";
            }
            frames.add(new KeyFrame(new RPNVector3f(v1,v2,v3),0,InterpolationMode.LINEAR));
        }else if (keyFrames.isJsonPrimitive()){
            String value = keyFrames.getAsString();
            frames.add(new KeyFrame(new RPNVector3f(value,value,value), 0, InterpolationMode.LINEAR));
        }
        return frames;
    }

    private KeyFrame parseKeyFrame(KeyFrameLoadType type,int timestamp,JsonElement element){
        try {
            if (element.isJsonPrimitive()) {
                String value = element.getAsString();
                return new KeyFrame(new RPNVector3f(value,value,value), timestamp, InterpolationMode.LINEAR);
            } else if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();
                String v1 = array.get(0).getAsString();
                String v2 = array.get(1).getAsString();
                String v3 = array.get(2).getAsString();
                if (type == KeyFrameLoadType.POSITION){
                    v1 = "-(" + v1 + ")";
                }else if (type == KeyFrameLoadType.ROTATION){
                    v1 = "-(" + v1 + ")";
                    v2 = "-(" + v2 + ")";
                }
                return new KeyFrame(new RPNVector3f(v1,v2,v3),timestamp,InterpolationMode.LINEAR);
            }else if (element.isJsonObject()){
                JsonObject object = element.getAsJsonObject();

                RPNVector3f pre = null;
                if (object.has("pre")) {
                    JsonArray array = object.getAsJsonArray("pre");
                    String v1 = array.get(0).getAsString();
                    String v2 = array.get(1).getAsString();
                    String v3 = array.get(2).getAsString();
                    if (type == KeyFrameLoadType.POSITION){
                        v1 = "-(" + v1 + ")";
                    }else if (type == KeyFrameLoadType.ROTATION){
                        v1 = "-(" + v1 + ")";
                        v2 = "-(" + v2 + ")";
                    }
                    pre = new RPNVector3f(v1,v2,v3);
                }
                JsonArray array = object.getAsJsonArray("post");
                String v1 = array.get(0).getAsString();
                String v2 = array.get(1).getAsString();
                String v3 = array.get(2).getAsString();
                if (type == KeyFrameLoadType.POSITION){
                    v1 = "-(" + v1 + ")";
                }else if (type == KeyFrameLoadType.ROTATION){
                    v1 = "-(" + v1 + ")";
                    v2 = "-(" + v2 + ")";
                }
                InterpolationMode mode = parseMode(object);
                return new KeyFrame(pre,new RPNVector3f(v1,v2,v3),timestamp,mode);
            }else{
                throw new RuntimeException("Unknown keyframe format.");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while loading animation: " + this.name);
        }
    }


    private InterpolationMode parseMode(JsonObject keyFrame){
        if (keyFrame.has("lerp_mode")){
            String mode = keyFrame.get("lerp_mode").getAsString();
            if (mode.equals("catmullrom")){
                return InterpolationMode.CATMULLROM;
            }else{
                throw new RuntimeException("Unknown lerp mode: " + mode);
            }
        }else{
            return InterpolationMode.LINEAR;
        }
    }



    private LoopMode parseLoopMode(ResourceLocation name,JsonObject animation){
        if (animation.has("loop")){
            var element = animation.get("loop");
            String s = element.getAsString();
            if (s.equals("true")){
                return LoopMode.LOOP;
            }else if (s.equals("hold_on_last_frame")){
                return LoopMode.HOLD_ON_LAST_FRAME;
            }else{
                throw new RuntimeException("Unknown loop mode in animation: " + name);
            }
        }else{
            return LoopMode.ONCE;
        }
    }



    public enum LoopMode {
        LOOP,
        ONCE,
        HOLD_ON_LAST_FRAME
    }


    //key frame values from blockbench are exported wrongly so we do this
    private enum KeyFrameLoadType{
        POSITION(-1,1,1),
        ROTATION(-1,-1,1),
        SCALE(1,1,1)
        ;

        float xMod;
        float yMod;
        float zMod;

        KeyFrameLoadType(float x,float y,float z){
            this.xMod = x;
            this.yMod = y;
            this.zMod = z;
        }
    }
}
