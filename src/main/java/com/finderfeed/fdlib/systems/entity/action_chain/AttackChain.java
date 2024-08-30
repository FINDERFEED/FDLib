package com.finderfeed.fdlib.systems.entity.action_chain;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;

import java.util.*;

public class AttackChain {

    private HashMap<String,Attack> registeredAttacks = new HashMap<>();

    private List<Pair<Integer,AttackOptions>> attacks = new ArrayList<>();

    private Queue<AttackInstance> chain = new ArrayDeque<>();

    private AttackInstance currentAttack = null;

    private RandomSource source;

    public AttackChain(RandomSource source){
        this.source = source;
    }




    public AttackChain addAttack(int priority,AttackOptions options){
        this.addToList(priority,options);
        return this;
    }


    public void tick(){
        if (this.chain.isEmpty() && currentAttack == null){
            this.buildQueue();
        }

        if (currentAttack != null){
            if (currentAttack.attack.getExecutor().execute(currentAttack)){
                AttackOptions options = currentAttack.options;
                if (options != null){

                }

            }else{
                currentAttack.tick++;
            }
        }else{

        }

    }

    private void buildQueue(){
        int num = attacks.get(0).getFirst();
        Queue<AttackInstance> queue = new ArrayDeque<>();
        List<AttackInstance> attacksToAdd = new ArrayList<>();
        for (int i = 0; i < attacks.size();i++){
            var pair = this.attacks.get(i);
            if (pair.getFirst() != num){
                num = pair.getFirst();
                i--;
                while (!attacksToAdd.isEmpty()){
                    int rnd = source.nextInt(attacksToAdd.size());
                    queue.offer(attacksToAdd.get(rnd));
                    attacksToAdd.remove(rnd);
                }
                continue;
            }else{
                AttackOptions options = pair.getSecond();
                attacksToAdd.add(new AttackInstance(options,options.getAttack(this.source)));
            }
        }
        this.chain = queue;
    }


    private void addToList(int priority,AttackOptions attack){
        this.attacks.add(new Pair<>(priority,attack));
        this.attacks.sort(Comparator.comparingInt(Pair::getFirst));
        AttackOptions options = attack;
        while (options != null){
            for (Attack a : options){
                this.registeredAttacks.put(a.getName(),a);
            }
            options = attack.nextAttack;
        }
    }

    public AttackInstance getCurrentAttack() {
        return currentAttack;
    }
}
