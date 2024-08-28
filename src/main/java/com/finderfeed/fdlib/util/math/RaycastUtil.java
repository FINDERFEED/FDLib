package com.finderfeed.fdlib.util.math;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RaycastUtil {

    public static Vector3f traceInfinitePlane(Vector3f point,Vector3f normal,Vector3f lineP1,Vector3f lineP2){
        var c = getPlaneCoefficients(point,normal);
        return traceInfinitePlane(c[0],c[1],c[2],c[3],lineP1,lineP2);
    }

    public static Vector3f traceInfinitePlane(float A,float B,float C,float D,Vector3f lineP1,Vector3f lineP2){
        float x1 = lineP1.x;
        float x2 = lineP2.x;
        float y1 = lineP1.y;
        float y2 = lineP2.y;
        float z1 = lineP1.z;
        float z2 = lineP2.z;

        float y =
                ((-D - A * x1 - C * z1) * (y2 - y1) + A * y1 * (x2 - x1) + C * y1 * (z2 - z1)) /
                (A * (x2 - x1) + B * (y2 - y1) + C * (z2 - z1));
        float x = (y - y1) / (y2 - y1) * (x2 - x1) + x1;
        float z = (y - y1) / (y2 - y1) * (z2 - z1) + z1;
        return new Vector3f(x,y,z);
    }



    //points should be like
    /*
    p2---->p3
    ↑      |
    |      |
    |      ↓
    p1<----p4
     */
    public static boolean isPointInSquare(Vector3f p1,Vector3f p2,Vector3f p3,Vector3f p4,Vector3f point){
        var pl1 = getPlaneCoefficients(p1,p2.sub(p1,new Vector3f()));
        var pl2 = getPlaneCoefficients(p2,p3.sub(p2,new Vector3f()));
        var pl3 = getPlaneCoefficients(p3,p4.sub(p3,new Vector3f()));
        var pl4 = getPlaneCoefficients(p4,p1.sub(p4,new Vector3f()));
        if (planeEquation(pl1,point) < 0){
            return false;
        }else if (planeEquation(pl2,point) < 0){
            return false;
        }else if (planeEquation(pl3,point) < 0){
            return false;
        }else if (planeEquation(pl4,point) < 0){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isPointOnLine(Vector3f lineP1,Vector3f lineP2,Vector3f point,float accuracy){

        float length = lineP1.sub(lineP2,new Vector3f()).length();
        float l1 = point.sub(lineP1,new Vector3f()).length();
        float l2 = point.sub(lineP2,new Vector3f()).length();

        return (l1 + l2) <= (length + accuracy);
    }



    public static float[] getPlaneCoefficients(Vector3f point,Vector3f normal){
        float x1 = point.x;
        float y1 = point.y;
        float z1 = point.z;
        float A = normal.x;
        float B = normal.y;
        float C = normal.z;
        float D = (-normal.x * x1 - normal.y * y1 - normal.z * z1);
        return new float[]{A,B,C,D};
    }

    public static float planeEquation(float[] c,Vector3f point){
        return planeEquation(c[0],c[1],c[2],c[3],point);
    }
    public static float planeEquation(float A,float B,float C,float D,Vector3f point){
        return A * point.x + B * point.y + C * point.z + D;
    }

    public static Entity raycastEntity(Entity exception,Level level, Vec3 begin, Vec3 end){
        EntityHitResult result = ProjectileUtil.getEntityHitResult(level,exception,begin,end, new AABB(begin,end),(entity)->true);
        if (result == null) return null;
        return result.getEntity();
    }
}
