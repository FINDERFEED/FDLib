package com.finderfeed.fdlib.systems.entity.action_chain;

import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AttackOptions<T extends AttackDefinition> implements Iterable<T> {

    protected List<T> definitions;
    protected AttackOptions<?> preAttackOptions = null;
    protected AttackOptions<?> nextAttackOptions = null;

    public AttackOptions(List<T> attackDefinitions,AttackOptions<?> pre, AttackOptions<?> next){
        this.nextAttackOptions = next;
        this.preAttackOptions = pre;
        this.definitions = attackDefinitions;
    }

    public abstract Collection<T> getAttacks(RandomSource source);

    public AttackOptions<?> getNextAttackOptions() {
        return nextAttackOptions;
    }

    public AttackOptions<?> getPreAttackOptions() {
        return preAttackOptions;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return definitions.iterator();
    }

    public static class ChainAttackOptions extends AttackOptions<AttackDefinition>{

        public ChainAttackOptions(List<AttackDefinition> attackDefinitions, AttackOptions<?> pre, AttackOptions<?> next) {
            super(attackDefinitions, pre, next);
        }

        @Override
        public Collection<AttackDefinition> getAttacks(RandomSource source) {
            return this.definitions;
        }

    }

    public static class WeightedRandomAttackOptions extends AttackOptions<WeightedAttackDefinition> {

        private WeightedRandomList<WeightedAttackDefinition> weightedRandomList;

        public WeightedRandomAttackOptions(List<WeightedAttackDefinition> attackDefinitions, AttackOptions pre, AttackOptions next) {
            super(attackDefinitions, pre, next);
            this.weightedRandomList = WeightedRandomList.create(attackDefinitions);
        }

        public Collection<WeightedAttackDefinition> getAttacks(RandomSource source){
            return List.of(weightedRandomList.getRandom(source).get());
        }

    }

    public static WeightedRandomAttackOptionsBuilder builder(){
        return new WeightedRandomAttackOptionsBuilder();
    }

    public static ChainedAttackOptionsBuilder chainOptionsBuilder(){
        return new ChainedAttackOptionsBuilder();
    }

    public static class ChainedAttackOptionsBuilder {

        private List<AttackDefinition> attackDefinitions = new ArrayList<>();
        private AttackOptions<?> preAttackOptions = null;
        private AttackOptions<?> nextAttackOptions = null;

        public ChainedAttackOptionsBuilder addAttack(String executorName){
            this.attackDefinitions.add(new AttackDefinition(executorName));
            return this;
        }

        public ChainedAttackOptionsBuilder addAttack(AttackOptions<?> options){
            this.attackDefinitions.add(new AttackDefinition(options));
            return this;
        }

        public ChainedAttackOptionsBuilder setNextAttack(AttackOptions<?> options){
            this.nextAttackOptions = options;
            return this;
        }

        public ChainedAttackOptionsBuilder setNextAttack(String executorName){
            return this.setNextAttack(AttackOptions.chainOptionsBuilder()
                    .addAttack(executorName)
                    .build());
        }

        public ChainedAttackOptionsBuilder setPreAttack(AttackOptions<?> options){
            this.preAttackOptions = options;
            return this;
        }

        public ChainedAttackOptionsBuilder setPreAttack(String executorName){
            return this.setPreAttack(AttackOptions.chainOptionsBuilder()
                    .addAttack(executorName)
                    .build());
        }

        public ChainAttackOptions build(){
            return new ChainAttackOptions(this.attackDefinitions, preAttackOptions, nextAttackOptions);
        }

    }

    public static class WeightedRandomAttackOptionsBuilder {

        private List<WeightedAttackDefinition> attackDefinitions = new ArrayList<>();
        private AttackOptions<?> preAttackOptions = null;
        private AttackOptions<?> nextAttackOptions = null;

        public WeightedRandomAttackOptionsBuilder addAttack(int weight, String executorName){
            this.attackDefinitions.add(new WeightedAttackDefinition(executorName,weight));
            return this;
        }

        public WeightedRandomAttackOptionsBuilder addAttack(String executorName){
            return this.addAttack(1,executorName);
        }

        public WeightedRandomAttackOptionsBuilder addAttack(int weight, AttackOptions<?> options){
            this.attackDefinitions.add(new WeightedAttackDefinition(options,weight));
            return this;
        }

        public WeightedRandomAttackOptionsBuilder addAttack(AttackOptions<?> options){
            return this.addAttack(1,options);
        }

        public WeightedRandomAttackOptionsBuilder setNextAttack(AttackOptions<?> options){
            this.nextAttackOptions = options;
            return this;
        }

        public WeightedRandomAttackOptionsBuilder setNextAttack(String executorName){
            return this.setNextAttack(AttackOptions.builder()
                            .addAttack(executorName)
                    .build());
        }

        public WeightedRandomAttackOptionsBuilder setPreAttack(AttackOptions<?> options){
            this.preAttackOptions = options;
            return this;
        }

        public WeightedRandomAttackOptionsBuilder setPreAttack(String executorName){
            return this.setPreAttack(AttackOptions.builder()
                    .addAttack(executorName)
                    .build());
        }



        public AttackOptions<WeightedAttackDefinition> build(){
            return new WeightedRandomAttackOptions(attackDefinitions,preAttackOptions,nextAttackOptions);
        }

    }
}
