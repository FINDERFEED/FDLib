package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

public class AttackDefinition implements WeightedEntry {

    private String name;
    protected int weight;

    protected AttackDefinition(String name, int weight){
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    @Override
    public Weight getWeight() {
        return Weight.of(this.weight);
    }
}
