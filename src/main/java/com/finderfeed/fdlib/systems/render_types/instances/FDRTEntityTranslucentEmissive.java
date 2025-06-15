package com.finderfeed.fdlib.systems.render_types.instances;

import com.finderfeed.fdlib.systems.render_types.FDRenderType;
import com.finderfeed.fdlib.systems.render_types.RenderTypeFactory;
import net.minecraft.client.renderer.RenderType;

public class FDRTEntityTranslucentEmissive extends FDRenderType {

    @Override
    public RenderTypeFactory factory() {
        return RenderType::entityTranslucentEmissive;
    }

}
