package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

public class Attack implements WeightedEntry {

    private String name;
    private AttackExecutor executor;
    protected int weight;

    protected Attack(String name,AttackExecutor executor,int weight){
        this.name = name;
        this.weight = weight;
        this.executor = executor;
    }

    public AttackExecutor getExecutor() {
        return executor;
    }

    public String getName() {
        return name;
    }

    @Override
    public Weight getWeight() {
        return Weight.of(this.weight);
    }
}
