package com.finderfeed.fdlib.systems.cutscenes.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:cutscene_packet")
public class StartCutscenePacket extends FDPacket {

    private CutsceneData data;

    public StartCutscenePacket(FriendlyByteBuf buf){
        CompoundTag tag = buf.readNbt();
        CutsceneData d = new CutsceneData();
        d.autoLoad(tag);
        this.data = d;
    }

    public StartCutscenePacket(CutsceneData data){
        this.data = data;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        data.autoSave(tag);
        buf.writeNbt(tag);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        CutsceneCameraHandler.startCutscene(data);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
