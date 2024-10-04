package com.finderfeed.fdlib.nbt;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.FDHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class TagSerializationHelper {

    public static <T extends AutoSerializable> void saveFields(CompoundTag tag,T object){
        Class<? extends AutoSerializable> clazz = object.getClass();
        List<Field> fields = FDHelpers.getAnnotatedFieldsInClass(clazz, SerializableField.class);
        for (Field field : fields){
            field.setAccessible(true);
            saveField(object,tag,field);
        }
    }

    public static <T extends AutoSerializable> void loadFields(CompoundTag tag,T object){
        Class<? extends AutoSerializable> clazz = object.getClass();
        List<Field> fields = FDHelpers.getAnnotatedFieldsInClass(clazz, SerializableField.class);
        for (Field field : fields){
            field.setAccessible(true);
            loadField(object,tag,field);
        }
    }

    private static <T extends AutoSerializable> void saveField(T object,CompoundTag tag,Field field){
        try {
            String name = field.getName();
            Object value = field.get(object);
            if (value instanceof Integer i){
                tag.putInt(name,i);
            }else if (value instanceof Float f){
                tag.putFloat(name,f);
            }else if (value instanceof Double d){
                tag.putDouble(name,d);
            }else if (value instanceof Boolean d){
                tag.putBoolean(name,d);
            }else if (value instanceof String s){
                tag.putString(name,s);
            }else if (value instanceof UUID uuid){
                tag.putUUID(name,uuid);
            }else if (value instanceof Tag t) {
                tag.put(name, t);
            }else if (value == null){
                //don't serialize anything
            }else if (value instanceof AutoSerializable autoSerializable){
                CompoundTag t = new CompoundTag();
                autoSerializable.autoSave(t);
                tag.put(name,t);
            } else {
                Class<?> clazz = field.getType();
                if (FDTagDeserializers.DESERIALIZERS.containsKey(clazz)){
                    TagDeserializer<?> deserializer = FDTagDeserializers.DESERIALIZERS.get(clazz);
                    hackyUseSerializer(name,deserializer,value,tag);
                }else{
                    FDLib.LOGGER.error("Deserializer for field: " + name + " in class " + object.getClass() + " not found.");
                }
            }
        }catch (Exception e){
            FDLib.LOGGER.error("Failed to serialize field in " + object.getClass() + " class: " + field.getName());
            e.printStackTrace();
        }
    }

    private static <T extends AutoSerializable> void loadField(T object,CompoundTag tag,Field field){
        try {
            String name = field.getName();
            Class<?> fieldType = field.getType();

            if (!tag.contains(name)){
                if (int.class.isAssignableFrom(fieldType)){
                    field.set(object,tag.getInt(name));
                }else if(float.class.isAssignableFrom(fieldType)){
                    field.set(object,tag.getFloat(name));
                }else if (double.class.isAssignableFrom(fieldType)){
                    field.set(object,tag.getDouble(name));
                }else if (boolean.class.isAssignableFrom(fieldType)){
                    field.set(object,tag.getBoolean(name));
                }else {
                    field.set(object, null);
                }
                return;
            }

            if (Integer.class.isAssignableFrom(fieldType) || int.class.isAssignableFrom(fieldType)){
                field.set(object,tag.getInt(name));
            }else if (Float.class.isAssignableFrom(fieldType) || float.class.isAssignableFrom(fieldType)){
                field.set(object,tag.getFloat(name));
            }else if (Double.class.isAssignableFrom(fieldType) || double.class.isAssignableFrom(fieldType)){
                field.set(object,tag.getDouble(name));
            }else if (Boolean.class.isAssignableFrom(fieldType) || boolean.class.isAssignableFrom(fieldType)){
                field.set(object,tag.getBoolean(name));
            }else if (String.class.isAssignableFrom(fieldType)){
                field.set(object,tag.getString(name));
            }else if (UUID.class.isAssignableFrom(fieldType)){
                field.set(object,tag.getUUID(name));
            }else if (Tag.class.isAssignableFrom(fieldType)) {
                field.set(object,tag.get(name));
            }else if (AutoSerializable.class.isAssignableFrom(fieldType)){
                CompoundTag t = tag.getCompound(name);
                AutoSerializable serializable = (AutoSerializable) field.get(object);
                if (serializable == null){
                    try {
                        Constructor<?> constructor = fieldType.getConstructor();
                        serializable = (AutoSerializable) constructor.newInstance();
                    }catch (Exception e) {
                        throw new RuntimeException("Tried to load an uninitialized AutoSerializable field." +
                                " All fields that implement that interface should be initialized or at least have a zero-argument constructor!");
                    }
                }
                serializable.autoLoad(t);
            } else {
                Class<?> clazz = field.getType();
                if (FDTagDeserializers.DESERIALIZERS.containsKey(clazz)){
                    TagDeserializer<?> deserializer = FDTagDeserializers.DESERIALIZERS.get(clazz);
                    Object value = hackyUseDeserializer(name,deserializer,tag);
                    field.set(object,value);
                }else{
                    FDLib.LOGGER.error("Deserializer for field: " + name + " in class " + object.getClass() + " not found.");
                }
            }
        }catch (Exception e){
            FDLib.LOGGER.error("Failed to serialize field in " + object.getClass() + " class: " + field.getName());
            e.printStackTrace();
        }
    }

    private static <T> void hackyUseSerializer(String name,TagDeserializer<T> deserializer,Object object,CompoundTag tag){
        deserializer.serialize(name, (T) object,tag);
    }
    private static <T> T hackyUseDeserializer(String name,TagDeserializer<T> deserializer,CompoundTag tag){
        return deserializer.deserialize(name,tag);
    }

}
