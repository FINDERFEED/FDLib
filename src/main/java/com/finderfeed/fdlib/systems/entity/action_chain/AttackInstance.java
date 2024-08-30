package com.finderfeed.fdlib.systems.entity.action_chain;

public class AttackInstance {

    protected AttackOptions options;

    public Attack attack;

    public int tick;

    public AttackInstance(AttackOptions options,Attack attack){
        this.attack = attack;
        this.options = options;
    }



}
