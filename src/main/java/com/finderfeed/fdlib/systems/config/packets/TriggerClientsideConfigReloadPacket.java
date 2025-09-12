package com.finderfeed.fdlib.systems.config.packets;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.neoforge.network.handling.IPayloadContext;


@RegisterFDPacket("fdlib:trigger_clientside_config_reload")
public class TriggerClientsideConfigReloadPacket extends FDPacket {

    private boolean sendMessages;

    public TriggerClientsideConfigReloadPacket(boolean sendMessages){
        this.sendMessages = sendMessages;
    }

    public TriggerClientsideConfigReloadPacket(FriendlyByteBuf buf){
        this.sendMessages = buf.readBoolean();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(sendMessages);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        Player player = FDClientHelpers.getClientPlayer();
        for (var v : FDRegistries.CONFIGS.entrySet()){
            JsonConfig config = v.getValue();
            if (config.isClientside()) {
                if (player != null && sendMessages) {
                    player.sendSystemMessage(Component.literal("Reloading client config: " + config.getName()).withStyle(ChatFormatting.GREEN));
                }
                config.loadFromDisk();
                if (player != null && sendMessages) {
                    player.sendSystemMessage(Component.literal("Reload complete").withStyle(ChatFormatting.GREEN));
                }
            }
        }
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
