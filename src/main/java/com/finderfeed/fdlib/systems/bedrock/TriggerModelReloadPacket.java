package com.finderfeed.fdlib.systems.bedrock;

import com.finderfeed.fdlib.init.FDModEvents;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:reload_models_packet")
public class TriggerModelReloadPacket extends FDPacket {

    public TriggerModelReloadPacket(FriendlyByteBuf buf){

    }

    public TriggerModelReloadPacket(){

    }


    @Override
    public void write(RegistryFriendlyByteBuf buf) {

    }

    @Override
    public void clientAction(IPayloadContext context) {
        if (SharedConstants.IS_RUNNING_IN_IDE){ // no need to reload on production
            FDModEvents.loadModels();
        }
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
