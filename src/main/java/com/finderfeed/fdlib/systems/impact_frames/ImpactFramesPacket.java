package com.finderfeed.fdlib.systems.impact_frames;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RegisterFDPacket("fdlib:impact_frames_packet")
public class ImpactFramesPacket extends FDPacket {

    private List<ImpactFrame> frames;

    public ImpactFramesPacket(List<ImpactFrame> frames){
        this.frames = frames;
    }

    public ImpactFramesPacket(FriendlyByteBuf buf){
        this.frames = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size;i++){
            this.frames.add(ImpactFrame.STREAM_CODEC.fromNetwork(buf));
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(frames.size());
        for (var frame : frames){
            ImpactFrame.STREAM_CODEC.toNetwork(buf,frame);
        }
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        frames.forEach(ImpactFramesHandler::addImpactFrame);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
