package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

public class AttackDefinition implements WeightedEntry {

    private String name;
    private AttackOptions attack;
    protected int weight;

    protected AttackDefinition(String name, int weight){
        this.name = name;
        this.weight = weight;
    }

    protected AttackDefinition(AttackOptions attack, int weight){
        this.attack = attack;
        this.weight = weight;
    }

    public String getExecutorName() {
        return name;
    }

    public AttackOptions getOptions(){
        return this.attack;
    }

    @Override
    public Weight getWeight() {
        return Weight.of(this.weight);
    }
}
