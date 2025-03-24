package com.finderfeed.fdlib.util;


import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class FDUtil {

    public static void printVector3f(Vector3f vector3f){
        System.out.println("X: " + vector3f.x + " Y: " + vector3f.y + " Z: " + vector3f.z);
    }

    public static int encodeDirection(Vec3 direction){
        direction = direction.normalize();
        int dx = (int)Math.round((direction.x + 1) / 2 * 0xff);
        int dy = (int)Math.round((direction.y + 1) / 2 * 0xff);
        int dz = (int)Math.round((direction.z + 1) / 2 * 0xff);

        int data = 0;
        data += dx;
        data += dy << 8;
        data += dz << 16;

        return data;
    }

    //int should only have a direction encoded
    public static Vec3 decodeDirection(int data){
        int dx = data & 0x0000ff;
        int dy = (data & 0x00ff00) >> 8;
        int dz = data >> 16;
        Vec3 v = new Vec3(
                ((float)dx / 0xff) * 2 - 1,
                ((float)dy / 0xff) * 2 - 1,
                ((float)dz / 0xff) * 2 - 1
        );
        return v;
    }



}
