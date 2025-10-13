package com.finderfeed.fdlib.systems.trails;

import com.finderfeed.fdlib.systems.shapes.FD2DShape;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.rendering.renderers.ShapeOnCurveRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FDTrailRenderer {

    public static <T> void renderTrail(T object, FDTrailDataGenerator<T> trailDataGen, VertexConsumer vertexConsumer, PoseStack matrices, float radius, int shapeVertices, int lod, float pticks, FDColor startColor, FDColor endColor){

        var points = trailDataGen.getPoints();

        if (points.size() <= 1) return;

        Vec3 pos = trailDataGen.extractPosition(object, pticks);

        var list = trailDataGen.getConvertedPointsForRendering(pos, pticks);

        ShapeOnCurveRenderer.start(vertexConsumer)
                .shape(FD2DShape.createSimpleCircleNVertexShape(radius,shapeVertices))
                .trailScalingFunction()
                .startColor(startColor)
                .endColor(endColor)
                .curvePositions(list)
                .pose(matrices)
                .lod(lod)
                .render();


    }

}
