package com.finderfeed.fdlib.systems.screen.screen_effect;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.FDRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


@RegisterFDPacket("fdlib:screen_effect")
public class SendScreenEffectPacket<D extends ScreenEffectData, T extends ScreenEffect<D>> extends FDPacket {

    private D data;
    private ScreenEffectType<D,T> type;
    private int inTime;
    private int stayTime;
    private int outTime;

    public SendScreenEffectPacket(D data, ScreenEffectType<D,T> type,int inTime, int stayTime, int outTime){
        this.data = data;
        this.type = type;
        this.stayTime = stayTime;
        this.outTime = outTime;
        this.inTime = inTime;
    }

    public SendScreenEffectPacket(FriendlyByteBuf buf){
        var registry = FDRegistries.SCREEN_EFFECTS.get();

        String location = buf.readUtf();
        ScreenEffectType<?,?> t = registry.getValue(ResourceLocation.tryParse(location));

        ScreenEffectData effectData = t.dataCodec.fromNetwork(buf);
        this.data = (D) effectData;
        this.type = (ScreenEffectType<D, T>) t;
        this.inTime = buf.readInt();
        this.stayTime = buf.readInt();
        this.outTime = buf.readInt();
    }


    @Override
    public void write(FriendlyByteBuf buf) {
        var registry = FDRegistries.SCREEN_EFFECTS.get();
        var location = registry.getKey(type);
        buf.writeUtf(location.toString());
        type.dataCodec.toNetwork(buf,data);
        buf.writeInt(inTime);
        buf.writeInt(stayTime);
        buf.writeInt(outTime);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        ScreenEffect<?> effect = type.factory.create(data,inTime,stayTime,outTime);
        ScreenEffectOverlay.addScreenEffect(effect);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }

}
