package com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity;


import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.to_other_mod.BossClientPackets;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:earth_shatter_entity_spawn")
public class EarthShatterEntitySpawnPacket extends FDPacket {


    public int entityId;
    public EarthShatterSettings settings;


    public EarthShatterEntitySpawnPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.settings = EarthShatterSettings.NETWORK_CODEC.decode(buf);
    }

    public EarthShatterEntitySpawnPacket(EarthShatterEntity entity,EarthShatterSettings settings){
        this.entityId = entity.getId();
        this.settings = settings;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        EarthShatterSettings.NETWORK_CODEC.encode(buf,settings);
    }

    @Override
    public void clientAction(IPayloadContext context) {

        BossClientPackets.handleEarthShatterSpawnPacket(entityId,settings);

    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
