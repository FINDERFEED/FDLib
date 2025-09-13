package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffect;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CutsceneScreenEffectData {


    private HashMap<Integer, List<ScreenEffectInstance<?,?>>> screenEffects = new HashMap<>();

    public <A extends ScreenEffectData,B extends ScreenEffect<A>> void putScreenEffectOnTick(int tick, ScreenEffectType<A,B> type, A data, int inTime, int stayTime, int outTime){
        this.screenEffects.computeIfAbsent(tick, v -> new ArrayList<>()).add(new ScreenEffectInstance<>(type, data, inTime, stayTime, outTime));
    }

    public List<ScreenEffectInstance<?,?>> getAllEffectsOnTick(int tick){
        return screenEffects.get(tick);
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeInt(this.screenEffects.size());


        var registry = FDRegistries.SCREEN_EFFECTS.get();

        for (var entry : this.screenEffects.entrySet()){

            var tick = entry.getKey();
            var pairs = entry.getValue();

            int pairsCount = pairs.size();

            buf.writeInt(pairsCount);

            buf.writeInt(tick);

            for (var pair : pairs){
                var type = pair.type;
                var data = pair.data;


                var location = registry.getKey(type);
                buf.writeUtf(location.toString());

                this.encodeScreenEffectData(buf, type, data);
                buf.writeInt(pair.inTime);
                buf.writeInt(pair.stayTime);
                buf.writeInt(pair.outTime);
            }

        }
    }

    private <A extends ScreenEffectData,B extends ScreenEffect<A>> void encodeScreenEffectData(FriendlyByteBuf buf, ScreenEffectType<A,B> type, ScreenEffectData data){
        var codec = type.dataCodec;
        codec.toNetwork(buf, (A) data);
    }

    public static CutsceneScreenEffectData decode(FriendlyByteBuf buf){

        HashMap<Integer, List<ScreenEffectInstance<?,?>>> screenEffects = new HashMap<>();

        int effects = buf.readInt();

        var registry = FDRegistries.SCREEN_EFFECTS.get();

        for (int i = 0; i < effects; i++){

            int pairs = buf.readInt();

            int tick = buf.readInt();

            for (int k = 0; k < pairs; k++){
                String location = buf.readUtf();
                ScreenEffectType<?,?> t = registry.getValue(ResourceLocation.parse(location));
                ScreenEffectData effectData = t.dataCodec.fromNetwork(buf);
                var data = effectData;
                var inTime = buf.readInt();
                var stayTime = buf.readInt();
                var outTime = buf.readInt();
                putInMap(screenEffects, t, data, inTime, stayTime, outTime, tick);
            }

        }

        CutsceneScreenEffectData cutsceneScreenEffectData = new CutsceneScreenEffectData();
        cutsceneScreenEffectData.screenEffects = screenEffects;
        return cutsceneScreenEffectData;
    }

    private static <A extends ScreenEffectData,B extends ScreenEffect<A>> void putInMap(HashMap<Integer, List<ScreenEffectInstance<?,?>>> map, ScreenEffectType<A,B> type, ScreenEffectData data, int inTime, int stayTime, int outTime, int tick){
        map.computeIfAbsent(tick,v->new ArrayList<>()).add(new ScreenEffectInstance<>(type,(A)data,inTime,stayTime,outTime));
    }



    public record ScreenEffectInstance<A extends ScreenEffectData,B extends ScreenEffect<A>>(ScreenEffectType<A,B> type, A data, int inTime, int stayTime, int outTime){

    }





}
