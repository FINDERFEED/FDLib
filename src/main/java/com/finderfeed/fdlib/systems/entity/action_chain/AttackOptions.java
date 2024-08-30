package com.finderfeed.fdlib.systems.entity.action_chain;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackOptions implements Iterable<Attack> {

    private WeightedRandomList<Attack> list;

    protected AttackOptions nextAttack = null;

    public AttackOptions(String name,AttackExecutor executor){
        list = WeightedRandomList.create(new Attack(name,executor,1));
    }

    private AttackOptions(){
    }


    public Attack getAttack(RandomSource source){
        return this.list.getRandom(source).get();
    }

    public AttackOptions getNextAttack() {
        return nextAttack;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public @NotNull Iterator<Attack> iterator() {
        return list.unwrap().iterator();
    }

    public static class Builder{

        private List<Attack> attacks = new ArrayList<>();

        private AttackOptions nextAttack = null;

        public Builder addAttack(String name,int weight,AttackExecutor executor){
            this.attacks.add(new Attack(name,executor,weight));
            return this;
        }

        public Builder addAttack(String name,AttackExecutor executor){
            this.attacks.add(new Attack(name,executor,1));
            return this;
        }

        public Builder setNextAttack(AttackOptions next){
            this.nextAttack = next;
            return this;
        }

        public AttackOptions build(){
            AttackOptions options = new AttackOptions();
            options.list = WeightedRandomList.create(attacks);
            options.nextAttack = nextAttack;
            return options;
        }


    }


}
