package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block;

import net.minecraft.client.gui.GuiGraphics;

public interface TextBlockEntry {

    void render(GuiGraphics graphics, TextBlockWidget textBlock, TextBlockCursor cursor, float mouseX, float mouseY, float pticks, boolean lastEntry);

}
