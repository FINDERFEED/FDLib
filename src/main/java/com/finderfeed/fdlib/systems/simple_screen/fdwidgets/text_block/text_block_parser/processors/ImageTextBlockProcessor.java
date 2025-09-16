package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.text_block_parser.processors;

import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.TextBlockEntry;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.interactions.TextBlockEntryInteraction;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.text_block_entries.image_entry.ImageTextEntry;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.text_block_parser.TextBlockProcessor;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;

public class ImageTextBlockProcessor extends TextBlockProcessor {

    @Override
    public List<TextBlockEntry> parse(float textScale, boolean renderShadow, int textColor, HashMap<String, String> arguments) {

        String path = arguments.get("path");

        String modid;

        if (path.contains(":")){
            String[] modidpath = path.split(":");
            modid = modidpath[0];
            path = modidpath[1];
        }else{
            modid = "minecraft";
        }

        ResourceLocation location = ResourceLocation.tryParse(
                modid + ":textures/gui/" + path + ".png"
        );

        ImageTextEntry imageTextEntry = new ImageTextEntry(location, textScale, TextBlockEntryInteraction.empty());

        return List.of(imageTextEntry);
    }
}
