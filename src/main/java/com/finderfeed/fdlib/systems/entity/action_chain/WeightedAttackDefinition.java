package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

public class WeightedAttackDefinition extends AttackDefinition implements WeightedEntry {

    private int weight;

    protected WeightedAttackDefinition(String name, int weight) {
        super(name);
        this.weight = weight;
    }

    protected WeightedAttackDefinition(AttackOptions attack, int weight) {
        super(attack);
        this.weight = weight;
    }

    @Override
    public Weight getWeight() {
        return Weight.of(weight);
    }

}
