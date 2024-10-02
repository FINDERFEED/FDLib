package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.to_other_mod.packets.PosLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class BossUtil {

    public static final int CHESED_GET_BLOCKS_FROM_EARTH_EVENT = 1;


    public static void posEvent(ServerLevel level, Vec3 pos, int event,double radius){
        PacketDistributor.sendToPlayersNear(level,null,pos.x,pos.y,pos.z,radius,new PosLevelEventPacket(pos,event));
    }

}
