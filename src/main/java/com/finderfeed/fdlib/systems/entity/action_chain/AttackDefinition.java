package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

public class AttackDefinition {

    private String name;
    private AttackOptions attack;

    protected AttackDefinition(String name){
        this.name = name;
    }

    protected AttackDefinition(AttackOptions attack){
        this.attack = attack;
    }

    public String getExecutorName() {
        return name;
    }

    public AttackOptions getOptions(){
        return this.attack;
    }

}
