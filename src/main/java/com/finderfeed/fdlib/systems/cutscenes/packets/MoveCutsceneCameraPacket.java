package com.finderfeed.fdlib.systems.cutscenes.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraftforge.neoforge.network.handling.IPayloadContext;

/**
 * Won't do anything if cutscene is not active
 */
@RegisterFDPacket("fdlib:move_cutscene_camera")
public class MoveCutsceneCameraPacket extends FDPacket {

    private CutsceneData cutsceneData;

    public MoveCutsceneCameraPacket(CutsceneData cutsceneData){
        this.cutsceneData = cutsceneData;
    }

    public MoveCutsceneCameraPacket(RegistryFriendlyByteBuf buf){
        this.cutsceneData = CutsceneData.decode(buf);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        this.cutsceneData.encode(buf);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        CutsceneCameraHandler.moveCamera(cutsceneData);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }

}
