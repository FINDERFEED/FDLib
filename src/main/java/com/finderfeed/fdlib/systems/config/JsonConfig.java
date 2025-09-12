package com.finderfeed.fdlib.systems.config;

import com.finderfeed.fdlib.FDLib;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class JsonConfig {
    protected static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private Path path;
    private Path directory;
    private String loadedJson;
    private ResourceLocation name;
    public JsonConfig(ResourceLocation location){

        String p = location.getPath();
        int id = p.lastIndexOf('/');

        if (id != -1){
            String filename = p.substring(id + 1);
            p = p.substring(0,id + 1);
            directory = FMLPaths.CONFIGDIR.get().resolve(location.getNamespace()).resolve(p);
            path = directory.resolve(filename + ".json");
        }else{
            directory = FMLPaths.CONFIGDIR.get().resolve(location.getNamespace());
            path = directory.resolve(p + ".json");
        }


        this.name = location;
    }


    public void loadFromDisk(){
        try {
            FDLib.LOGGER.info("Loading config " + name);
            if (!Files.exists(path)){
                Files.createDirectories(directory);
                Writer writer = Files.newBufferedWriter(path);
                GSON.toJson(new JsonObject(),writer);
                writer.close();
            }
            Reader reader = Files.newBufferedReader(path);
            JsonObject object = GSON.fromJson(reader,JsonObject.class);
            reader.close();

            boolean changesWereMade;

            if (object == null){
                changesWereMade = true;
                object = new JsonObject();
                this.parseJson(object);
            }else{
                changesWereMade = this.parseJson(object);
            }


            if (changesWereMade){
                Writer writer = Files.newBufferedWriter(path);
                GSON.toJson(object,writer);
                writer.close();
            }
            this.loadedJson = object.toString();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse all data and assign all values into json config
     * @return if changes to json object were made
     */
    public abstract boolean parseJson(JsonObject object);

    public abstract boolean isClientside();

    public ResourceLocation getName() {
        return name;
    }

    public String getLoadedJson() {
        return loadedJson;
    }

    public void setLoadedJson(JsonObject loadedJson) {
        this.loadedJson = loadedJson.toString();
    }
}
