package com.finderfeed.fdlib.systems.cutscenes.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;


@RegisterFDPacket("fdlib:stop_cutscene")
public class StopCutscenePacket extends FDPacket {

    public StopCutscenePacket(){

    }

    public StopCutscenePacket(FriendlyByteBuf buf){

    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        CutsceneCameraHandler.stopCutscene();
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }

}
