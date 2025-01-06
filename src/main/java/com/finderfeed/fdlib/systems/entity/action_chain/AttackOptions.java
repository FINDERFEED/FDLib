package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackOptions implements Iterable<AttackDefinition> {

    private WeightedRandomList<AttackDefinition> weightedRandomList;

    private AttackOptions nextAttackOptions = null;

    public AttackOptions(List<AttackDefinition> attackDefinitions, AttackOptions next){
        this.nextAttackOptions = next;
        this.weightedRandomList = WeightedRandomList.create(attackDefinitions);
    }

    public AttackDefinition getAttack(RandomSource source){
        return weightedRandomList.getRandom(source).get();
    }

    public AttackOptions getNextAttackOptions() {
        return nextAttackOptions;
    }

    @NotNull
    @Override
    public Iterator<AttackDefinition> iterator() {
        return weightedRandomList.unwrap().iterator();
    }

    public static Builder builder(){
        return new AttackOptions.Builder();
    }


    public static class Builder {

        private List<AttackDefinition> attackDefinitions = new ArrayList<>();
        private AttackOptions nextAttackOptions = null;

        public Builder addAttack(int weight, String executorName){
            this.attackDefinitions.add(new AttackDefinition(executorName,weight));
            return this;
        }

        public Builder addAttack(String executorName){
            this.attackDefinitions.add(new AttackDefinition(executorName,1));
            return this;
        }

        public Builder setNextAttack(AttackOptions options){
            this.nextAttackOptions = options;
            return this;
        }

        public AttackOptions build(){
            return new AttackOptions(attackDefinitions,nextAttackOptions);
        }

    }
}
