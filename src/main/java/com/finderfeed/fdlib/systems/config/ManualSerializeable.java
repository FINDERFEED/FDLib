package com.finderfeed.fdlib.systems.config;

import com.google.gson.JsonObject;

public interface ManualSerializeable {

    /**
     * Here you can (de)serialize fields that could not be automatically serialized, for example Item fields.
     * <p>
     * If you failed to deserialize the fields (for example the data was missing in json object) you should write that data
     * and return true.
     * <p>
     * If everything deserialized correctly return false.
     * <p>
     * Fields that are (de)serialized this way should not be annotated with @ConfigValue
     * <p>
     * P.S. Returning true means that there were any changes to json object during deserialization.
     */
    boolean process(JsonObject object);

}
