package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.interactions;

import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.TextBlockWidget;

@FunctionalInterface
public interface TextBlockClick {

    void click(TextBlockWidget block, float mouseX, float mouseY, int key);

}
