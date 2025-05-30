package com.finderfeed.fdlib;

import com.finderfeed.fdlib.network.lib_packets.PlayerMovePacket;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterData;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterHandler;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterPacket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.neoforged.neoforgespi.locating.IModFile;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;


// just random things
public class FDHelpers {


    public static final Gson GSON = new GsonBuilder().create();

    public static List<Entity> traceEntities(Level level, Vec3 start, Vec3 end,double boxInflate, Predicate<Entity> predicate){
        var list = level.getEntitiesOfClass(Entity.class,new AABB(start,end).inflate(boxInflate),predicate);
        Iterator<Entity> entityIterator = list.iterator();
        while (entityIterator.hasNext()){
            Entity e = entityIterator.next();
            AABB box = e.getBoundingBox().inflate(boxInflate);
            var opt = box.clip(start,end);
            if (opt.isEmpty()){
                var opt2 = box.clip(end,start);
                if (opt2.isEmpty()){
                    entityIterator.remove();
                }
            }
        }
        return list;
    }

    public static JsonElement readJsonFileFromAssets(ResourceLocation location,String format){
        ResourceLocation rl = ResourceLocation.tryBuild(location.getNamespace(),location.getPath() + format);
        try {
            byte[] bytes = readFileFromAssets(rl);
            String s = new String(bytes);
            JsonElement element = GSON.fromJson(s,JsonElement.class);
            return element;
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    public static JsonElement readJsonFileFromAssets(ResourceLocation location){
        return readJsonFileFromAssets(location,".json");
    }

    public static byte[] readFileFromAssets(ResourceLocation rl) throws IOException {
        ModList list = ModList.get();
        String modid = rl.getNamespace();
        String location = rl.getPath();
        IModFileInfo info = list.getModFileById(modid);
        if (info == null){throw new IOException("Cannot locate mod " + modid);}
        IModFile file = info.getFile();
        Path path = file.findResource("assets",modid,location);
        if (!Files.exists(path) || Files.isDirectory(path)){
            throw new IOException("File not found: " + rl);
        }
        return Files.readAllBytes(path);
    }


    public static List<Field> getAnnotatedFieldsInClass(Class<?> clazz,Class<? extends Annotation> annotationClass){
        Field[] fields = clazz.getDeclaredFields();
        List<Field> annotated = new ArrayList<>();
        for (Field field : fields){
            if (field.getAnnotation(annotationClass) != null){
                annotated.add(field);
            }
        }
        return annotated;
    }

    public static List<Field> getAllAnnotatedFieldsInClass(Class<?> clazz, Class<? extends Annotation> annotationClass){
        List<Field> fields = new ArrayList<>(getAnnotatedFieldsInClass(clazz,annotationClass));
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null){
            fields.addAll(getAnnotatedFieldsInClass(superclass,annotationClass));
            superclass = superclass.getSuperclass();
        }
        return fields;
    }

    public static List<ClassFields> collectAllAnnotatedFieldsInClass(Class<?> clazz, Class<? extends Annotation> annotationClass){
        List<ClassFields> classFields = new ArrayList<>();

        List<Field> fields = getAnnotatedFieldsInClass(clazz,annotationClass);
        if (!fields.isEmpty()) {
            classFields.add(new ClassFields(clazz, fields));
        }

        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null){
            fields = getAnnotatedFieldsInClass(superclass,annotationClass);
            if (!fields.isEmpty()) {
                classFields.add(new ClassFields(superclass, fields));
            }
            superclass = superclass.getSuperclass();
        }
        return classFields;
    }


    public static <T> List<Class<?>> getAnnotatedClasses(Class<T> annotationClass){
        Type type = Type.getType(annotationClass);
        ModList modList = ModList.get();
        List<ModFileScanData> datas = modList.getAllScanData();
        List<Class<?>> classes = new ArrayList<>();
        for (ModFileScanData data : datas) {
            var annotationDatas = data.getAnnotations();
            for (var adata : annotationDatas) {
                if (!adata.annotationType().equals(type)) continue;
                try {
                    Class<?> clazz = Class.forName(adata.clazz().getClassName());
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unexpected error - class not found: " + adata.clazz().getClassName());
                }
            }
        }
        return classes;
    }


    public record ClassFields(Class<?> owner,List<Field> fields){}
}
