package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.text_block_entries;

import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.TextBlockCursor;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.TextBlockEntry;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.TextBlockWidget;
import net.minecraft.client.gui.GuiGraphics;

public class NextLineTextEntry implements TextBlockEntry {

    private float lineHeight;

    public NextLineTextEntry(float lineHeight){
        this.lineHeight = lineHeight;
    }

    @Override
    public void render(GuiGraphics graphics, TextBlockWidget textBlock, TextBlockCursor cursor, float mx, float my, float pticks, boolean last) {
        cursor.nextLine(lineHeight);
    }
}
