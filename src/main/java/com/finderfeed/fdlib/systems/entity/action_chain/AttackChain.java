package com.finderfeed.fdlib.systems.entity.action_chain;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
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
                this.currentAttack = null;
            }else{
                currentAttack.tick++;
            }
        }else{
            AttackInstance attack = this.chain.poll();
            this.currentAttack = attack;
        }
    }

    private void buildQueue(){
        int num = attacks.get(0).getFirst();
        Queue<AttackInstance> queue = new ArrayDeque<>();
        List<Pair<AttackOptions,AttackInstance>> attacksToAdd = new ArrayList<>();
        for (int i = 0; i < attacks.size();i++){
            var pair = this.attacks.get(i);
            if (pair.getFirst() != num){
                num = pair.getFirst();
                i--;
                while (!attacksToAdd.isEmpty()){
                    int rnd = source.nextInt(attacksToAdd.size());

                    var p = attacksToAdd.get(rnd);
                    AttackInstance attack = p.getSecond();
                    AttackOptions options = p.getFirst();
                    queue.offer(attack);

                    AttackOptions next = options.nextAttack;
                    while (next != null){
                        AttackInstance instance = new AttackInstance(next.getAttack(source));
                        queue.offer(instance);
                        next = next.nextAttack;
                    }

                    attacksToAdd.remove(rnd);
                }
                continue;
            }else{
                AttackOptions options = pair.getSecond();
                attacksToAdd.add(new Pair<>(options,new AttackInstance(options.getAttack(this.source))));
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

    public void load(CompoundTag tag){
        this.chain = new ArrayDeque<>();
        try {
            int idx = 0;
            while (tag.contains("attack_" + idx)) {
                AttackInstance instance = new AttackInstance(this.registeredAttacks.get(tag.getString("attack_" + idx++)));
                this.chain.offer(instance);
            }
            if (tag.contains("currentAttack")) {
                this.currentAttack = new AttackInstance(
                        this.registeredAttacks.get(tag.getString("currentAttack"))
                );
                this.currentAttack.tick = tag.getInt("currentAttackTick");
            }
        }catch (Exception e){
            e.printStackTrace();
            this.chain = new ArrayDeque<>();
            this.currentAttack = null;
        }
    }

    public void save(CompoundTag tag){
        int idx = 0;
        for (AttackInstance instance : this.chain){
            tag.putString("attack_"+idx,instance.attack.getName());
        }
        if (currentAttack != null){
            tag.putString("currentAttack",currentAttack.attack.getName());
            tag.putInt("currentAttackTick",currentAttack.tick);
        }
    }
}
