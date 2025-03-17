package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.text_block_parser;

import java.util.HashMap;

public class FDString {

    private TextBlockProcessor processor;
    private HashMap<String,String> arguments;

    public FDString(TextBlockProcessor textBlockProcessor, HashMap<String,String> arguments){
        this.processor = textBlockProcessor;
        this.arguments = arguments;
    }

    public HashMap<String, String> getArguments() {
        return arguments;
    }

    public TextBlockProcessor getProcessor() {
        return processor;
    }
}
