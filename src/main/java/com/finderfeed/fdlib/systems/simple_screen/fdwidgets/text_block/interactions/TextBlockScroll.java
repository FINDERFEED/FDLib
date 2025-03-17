package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.interactions;

import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.TextBlockWidget;

@FunctionalInterface
public interface TextBlockScroll {

    void scroll(TextBlockWidget textBlock, float mx, float my, float scrollX, float scrollY);

}
