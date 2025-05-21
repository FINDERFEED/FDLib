package com.finderfeed.fdlib.systems.config.test;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.config.Comment;
import com.finderfeed.fdlib.systems.config.ConfigValue;
import com.finderfeed.fdlib.systems.config.ManualSerializeable;
import com.finderfeed.fdlib.systems.config.ReflectiveJsonConfig;
import com.finderfeed.fdlib.systems.cutscenes.CurveType;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class TestConfig extends ReflectiveJsonConfig implements ManualSerializeable {

    @ConfigValue
    public int testField1 = 1;

    @ConfigValue
    @Comment("Testim fignyu")
    public CurveType testField2 = CurveType.LINEAR;

    @ConfigValue
    public TestConfignEntry testEntry = new TestConfignEntry();

    public Item sausage = Items.ACACIA_LEAVES;

    public TestConfig() {
        super(FDLib.location("test"));
    }

    @Override
    public boolean process(JsonObject object) {

        if (object.has("sausage")){
            try {
                String s = object.get("sausage").getAsString();
                sausage = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(s));
            }catch (Exception e){
                object.addProperty("sausage",BuiltInRegistries.ITEM.getKey(Items.ACACIA_LEAVES).toString());
                sausage = Items.ACACIA_LEAVES;
                return true;
            }
        }else{
            object.addProperty("sausage",BuiltInRegistries.ITEM.getKey(Items.ACACIA_LEAVES).toString());
            sausage = Items.ACACIA_LEAVES;
            return true;
        }

        return false;
    }

    @Override
    public boolean isClientside() {
        return false;
    }
}
