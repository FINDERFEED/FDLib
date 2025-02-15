package com.finderfeed.fdlib.systems.cutscenes.test;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:test_camera_packet")
public class TestCameraPacket extends FDPacket {

    public TestCameraPacket(FriendlyByteBuf buf){

    }

    public TestCameraPacket(){

    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.testCameraPacket();
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
