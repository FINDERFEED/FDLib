package com.finderfeed.fdlib.systems.entity.action_chain;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class AttackChain {

    private HashMap<String,AttackExecutor> registeredAttackExecutors = new HashMap<>();

    private List<Pair<Integer,AttackOptions<?>>> attackOptions = new ArrayList<>();

    private List<Pair<Supplier<Boolean>, String>> alwaysTryCastAttacks = new ArrayList<>();

    private Queue<String> chain = new ArrayDeque<>();

    private AttackInstance currentAttack = null;

    private Function<String, AttackAction> attackListener = (inst)->AttackAction.PROCEED;

    private RandomSource source;

    public AttackChain(RandomSource source){
        this.source = source;
    }

    public AttackChain registerInClassAttacks(Class<? extends Entity> entityClass){
        for (Method method : entityClass.getDeclaredMethods()){
            Attack attack = method.getAnnotation(Attack.class);
            if (attack != null){
                method.setAccessible(true);
                this.registerAttack(attack.value(), (instance)->{
                    try {
                        return (boolean) method.invoke(instance);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        return this;
    }

    /**
     * Will always try to cast this attack whenever current attack is not yet decided
     */
    public AttackChain addAlwaysTryCastAttack(Supplier<Boolean> condition, String attack){
        this.alwaysTryCastAttacks.add(new Pair<>(condition, attack));
        return this;
    }

    public AttackChain attackListener(Function<String, AttackAction> listener){
        this.attackListener = listener;
        return this;
    }

    public AttackChain registerAttack(String name, AttackExecutor executor){
        this.registeredAttackExecutors.put(name,executor);
        return this;
    }

    public AttackChain addAttack(int priority, AttackOptions options){
        this.attackOptions.add(new Pair<>(priority,options));
        this.attackOptions.sort(Comparator.comparingInt(Pair::getFirst));
        return this;
    }

    public AttackChain addAttack(int priority,String attack){
        return this.addAttack(priority, AttackOptions.builder()
                        .addAttack(attack)
                .build());
    }

    public void tick(){
        if (this.chain.isEmpty() && currentAttack == null){
            this.buildQueue();
        } else {
            if (currentAttack != null) {
                //i know this looks VERY VERY strange but hey, it works!
                int previousStage = currentAttack.stage;
                if (currentAttack.attack.execute(currentAttack)){
                    currentAttack = null;
                    this.pollAndExecuteAttack();
                }else{
                    if (currentAttack.stage == previousStage){
                        currentAttack.tick++;
                    }
                }
            } else {
                this.pollAndExecuteAttack();
            }
        }
    }

    private boolean tryCastAlwaysAttack(){
        for (var attack : this.alwaysTryCastAttacks){
            var predicate = attack.getFirst();
            if (predicate.get()){
                var attackName = attack.getSecond();
                this.currentAttack = new AttackInstance(attackName, this.registeredAttackExecutors.get(attackName));
                return true;
            }
        }
        return false;
    }

    //polls an attack, executes it once, if its immediately finished polls next and does this until attack is not
    //executed immediately or whole chain is drain, then goes to next tick
    private void pollAndExecuteAttack(){
        while (currentAttack == null && !this.chain.isEmpty()) {

            String executorName = this.chain.peek();

            AttackAction attackAction = this.attackListener.apply(executorName);


            if (attackAction == AttackAction.WAIT){
                break;
            }else if (attackAction == AttackAction.SKIP){
                this.chain.poll();
                continue;
            }else{
                if (attackAction != AttackAction.PROCEED) {
                    break;
                }
            }

            if (this.tryCastAlwaysAttack()){
                return;
            }

            this.chain.poll();

            AttackExecutor executor = this.registeredAttackExecutors.get(executorName);
            if (executor == null) {
                throw new RuntimeException("Attack not registered: " + executorName);
            }
            this.currentAttack = new AttackInstance(executorName,executor);
            if (currentAttack.attack.execute(currentAttack)) {
                currentAttack = null;
            }else{
                //if executed and haven't ended - we check if the stage remained the same, if yes - one tick is accounted
                if (currentAttack.stage == 0) {
                    currentAttack.tick++;
                }
            }
        }
    }


    public void buildQueue(){
        this.chain.clear();

        List<AttackOptions> samePriority = new ArrayList<>();
        int currentValue = attackOptions.get(0).getFirst();

        for (Pair<Integer, AttackOptions<?>> pair : attackOptions) {
            if (pair.getFirst() == currentValue) {
                samePriority.add(pair.getSecond());
            } else {
                //adding the attacks to queue
                this.addSamePriorityOptionsToQueue(samePriority);
                //setting the current priority value to current thing in the queue
                samePriority.add(pair.getSecond());
                currentValue = pair.getFirst();
            }
        }

        this.addSamePriorityOptionsToQueue(samePriority);
    }

    private void addSamePriorityOptionsToQueue(List<AttackOptions> samePriority){
        while (!samePriority.isEmpty()){
            int rndid = source.nextInt(samePriority.size());
            AttackOptions options = samePriority.get(rndid);

            this.addOptionsToQueue(options);

            samePriority.remove(rndid);
        }
    }

    private void addOptionsToQueue(AttackOptions options){

        AttackOptions<?> pre = options.getPreAttackOptions();
        if (pre != null){
            this.addOptionsToQueue(pre);
        }

        AttackOptions<?> next = options;
        while (next != null){

            Collection<? extends AttackDefinition> definitions = next.getAttacks(source);

            for (var definition : definitions) {
                if (definition.getExecutorName() != null) {
                    this.chain.offer(definition.getExecutorName());
                } else {
                    this.addOptionsToQueue(definition.getOptions());
                }
            }

            next = next.getNextAttackOptions();
        }
    }


    public void reset(){
        this.currentAttack = null;
        this.chain.clear();
    }



    public AttackInstance getCurrentAttack() {
        return currentAttack;
    }

    public void load(CompoundTag tag){

        int index = 0;
        while (tag.contains("attack_" + index)){
            this.chain.offer(tag.getString("attack_" + index));
            index++;
        }

        if (tag.contains("currentAttack")){
            String name = tag.getString("currentAttack");
            int tick = tag.getInt("currentAttackTick");
            int stage = tag.getInt("currentAttackStage");
            AttackExecutor executor = this.registeredAttackExecutors.get(name);
            AttackInstance instance = new AttackInstance(name,executor);
            instance.tick = tick;
            instance.stage = stage;
            this.currentAttack = instance;
        }

    }

    public void save(CompoundTag tag){
        int index = 0;
        for (String s : this.chain){
            tag.putString("attack_" + index++,s);
        }

        if (this.currentAttack != null){
            tag.putString("currentAttack", currentAttack.name);
            tag.putInt("currentAttackTick",currentAttack.tick);
            tag.putInt("currentAttackStage",currentAttack.stage);
        }
    }
}
