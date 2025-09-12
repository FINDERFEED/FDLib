package com.finderfeed.fdlib.systems.config.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

@RegisterFDPacket("fdlib:update_all_configs")
public class JsonConfigSyncPacket extends FDPacket {

    private List<String> names;
    private List<String> configs;
    public JsonConfigSyncPacket(){
        names = new ArrayList<>();
        configs = new ArrayList<>();
        for (var entry : FDRegistries.CONFIGS){
            if (!entry.isClientside()) {
                names.add(FDRegistries.CONFIGS.getKey(entry).toString());
                configs.add(entry.getLoadedJson());
            }
        }
    }
    public JsonConfigSyncPacket(FriendlyByteBuf buf){
        int namesLen = buf.readInt();
        names = new ArrayList<>();
        for (int i = 0; i < namesLen;i++){
            names.add(buf.readUtf());
        }
        configs = new ArrayList<>();
        for (int i = 0; i < namesLen;i++){
            configs.add(buf.readUtf());
        }
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(names.size());
        for (String name : names){
            buf.writeUtf(name);
        }
        for (String sconfig : configs){
            buf.writeUtf(sconfig);
        }
    }

    @Override
    public void clientAction(IPayloadContext context) {
        for (int i = 0; i < names.size();i++){
            JsonConfig config = FDRegistries.CONFIGS.get(ResourceLocation.parse(names.get(i)));
            if (!config.isClientside()) {
                JsonObject object = JsonParser.parseString(configs.get(i)).getAsJsonObject();
                config.setLoadedJson(object);
                config.parseJson(object);
            }
        }
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }

}
