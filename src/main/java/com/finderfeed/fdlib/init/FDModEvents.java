package com.finderfeed.fdlib.init;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.FDHelpers;
import com.finderfeed.fdlib.nbt.FDTagDeserializers;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.ReflectiveJsonConfig;
import com.finderfeed.fdlib.systems.particle.CompositeParticleProcessor;
import com.finderfeed.fdlib.systems.particle.EmptyParticleProcessor;
import com.finderfeed.fdlib.systems.particle.ParticleProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD)
public class FDModEvents {


    @SubscribeEvent
    public static void commonSetupEvent(FMLCommonSetupEvent event){

        //memorize all default values in reflective configs
        for (JsonConfig config : FDRegistries.CONFIGS){
            if (config instanceof ReflectiveJsonConfig c){
                c.memorizeDefaultValues();
            }
        }

        event.enqueueWork(()->{


            loadAnimations();
            loadModels();
            FDTagDeserializers.registerDeserializer(Vec3.class,FDDefaultTagDeserializers.VEC3);
        });
    }

    public static void loadAnimations(){
        FDLib.LOGGER.info("Loading FD animations...");
        for (var entry : FDRegistries.ANIMATIONS.entrySet()){
            ResourceKey<Animation> key = entry.getKey();
            Animation value = entry.getValue();
            ResourceLocation animationName = key.location();
            ResourceLocation location = value.getFileLocation();
            if (location == null) throw new RuntimeException("The animation file location is not specified! Use Animation constructor with ResourceLocation which points to a file containing the animation!");
            ResourceLocation actualLocation = ResourceLocation.tryBuild(location.getNamespace(),
                    "bedrock/animations/" + location.getPath()
            );
            FDLib.LOGGER.info("Loading animation: " + animationName);
            JsonElement json = FDHelpers.readJsonFileFromAssets(actualLocation);
            JsonObject object = json.getAsJsonObject();
            JsonObject animations = object.getAsJsonObject("animations");
            String path = animationName.getPath();
            if (!animations.has(path)) throw new RuntimeException("Animation file " + location + " doesn't contain animation " + animationName);
            value.load(animationName,animations.getAsJsonObject(path));
        }
        FDLib.LOGGER.info("FD Animations loaded!");
    }

    public static void loadModels(){
        FDLib.LOGGER.info("Loading FD models...");
        for (var entry : FDRegistries.MODELS.entrySet()){
            FDModelInfo value = entry.getValue();
            ResourceLocation location = value.getModelName();
            ResourceLocation actualLocation = ResourceLocation.tryBuild(location.getNamespace(),
                    "bedrock/models/" + location.getPath()
            );
            FDLib.LOGGER.info("Loading model: " + location);
            JsonElement json = FDHelpers.readJsonFileFromAssets(actualLocation,".geo.json");
            value.load(json.getAsJsonObject());
        }
        FDLib.LOGGER.info("FD Models loaded!");
    }


}
