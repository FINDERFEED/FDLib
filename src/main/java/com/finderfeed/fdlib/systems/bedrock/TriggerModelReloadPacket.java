package com.finderfeed.fdlib.systems.bedrock;

import com.finderfeed.fdlib.init.FDModEvents;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;


@RegisterFDPacket("fdlib:reload_models_packet")
public class TriggerModelReloadPacket extends FDPacket {

    public TriggerModelReloadPacket(FriendlyByteBuf buf){

    }

    public TriggerModelReloadPacket(){

    }


    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        if (SharedConstants.IS_RUNNING_IN_IDE){ // no need to reload on production
            FDModEvents.loadModels();
        }
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
