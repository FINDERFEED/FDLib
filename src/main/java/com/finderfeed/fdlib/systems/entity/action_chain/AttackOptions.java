package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackOptions implements Iterable<AttackDefinition> {

    private WeightedRandomList<AttackDefinition> weightedRandomList;

    private AttackOptions preAttackOptions = null;
    private AttackOptions nextAttackOptions = null;

    public AttackOptions(List<AttackDefinition> attackDefinitions,AttackOptions pre, AttackOptions next){
        this.nextAttackOptions = next;
        this.preAttackOptions = pre;
        this.weightedRandomList = WeightedRandomList.create(attackDefinitions);
    }

    public AttackDefinition getAttack(RandomSource source){
        return weightedRandomList.getRandom(source).get();
    }

    public AttackOptions getNextAttackOptions() {
        return nextAttackOptions;
    }

    public AttackOptions getPreAttackOptions() {
        return preAttackOptions;
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
        private AttackOptions preAttackOptions = null;
        private AttackOptions nextAttackOptions = null;

        public Builder addAttack(int weight, String executorName){
            this.attackDefinitions.add(new AttackDefinition(executorName,weight));
            return this;
        }

        public Builder addAttack(String executorName){
            return this.addAttack(1,executorName);
        }

        public Builder addAttack(int weight, AttackOptions options){
            this.attackDefinitions.add(new AttackDefinition(options,weight));
            return this;
        }

        public Builder addAttack(AttackOptions options){
            return this.addAttack(1,options);
        }

        public Builder setNextAttack(AttackOptions options){
            this.nextAttackOptions = options;
            return this;
        }

        public Builder setNextAttack(String executorName){
            return this.setNextAttack(AttackOptions.builder()
                            .addAttack(executorName)
                    .build());
        }

        public Builder setPreAttack(AttackOptions options){
            this.preAttackOptions = options;
            return this;
        }

        public Builder setPreAttack(String executorName){
            return this.setPreAttack(AttackOptions.builder()
                    .addAttack(executorName)
                    .build());
        }



        public AttackOptions build(){
            return new AttackOptions(attackDefinitions,preAttackOptions,nextAttackOptions);
        }

    }
}
