package com.finderfeed.fdlib.systems.bedrock.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class JsonHelper {


    public static float parseFloat(JsonObject object,String memberName){
        return object.has(memberName) ? object.get(memberName).getAsFloat() : 0;
    }
    public static Vec3 parseVec3(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        return new Vec3(array.get(0).getAsDouble(),array.get(1).getAsDouble(),array.get(2).getAsDouble());
    }
    public static Vector3f parseVector3f(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        return new Vector3f(array.get(0).getAsFloat(),array.get(1).getAsFloat(),array.get(2).getAsFloat());
    }

    public static Vec3 parseVec3(JsonObject element,String memberName){
        if (!element.has(memberName)){
            return Vec3.ZERO;
        }
        JsonArray array = element.getAsJsonArray(memberName);
        return new Vec3(array.get(0).getAsDouble(),array.get(1).getAsDouble(),array.get(2).getAsDouble());
    }
    public static Vector3f parseVector3f(JsonObject element,String memberName){
        if (!element.has(memberName)){
            return new Vector3f();
        }
        JsonArray array = element.getAsJsonArray(memberName);
        return new Vector3f(array.get(0).getAsFloat(),array.get(1).getAsFloat(),array.get(2).getAsFloat());
    }

    public static Vec3 parseVec3(JsonObject element,String memberName,Vec3 def){
        if (!element.has(memberName)){
            return def;
        }
        JsonArray array = element.getAsJsonArray(memberName);
        return new Vec3(array.get(0).getAsDouble(),array.get(1).getAsDouble(),array.get(2).getAsDouble());
    }

    public static Vec2 parseVec2(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        return new Vec2(array.get(0).getAsFloat(),array.get(1).getAsFloat());
    }

    public static String getString(JsonObject object,String memberName){
        if (!object.has(memberName)){
            return "";
        }
        return object.get(memberName).getAsString();
    }

    public static String getString(JsonObject object,String memberName,String defaults){
        if (!object.has(memberName)){
            return defaults;
        }
        return object.get(memberName).getAsString();
    }

}
