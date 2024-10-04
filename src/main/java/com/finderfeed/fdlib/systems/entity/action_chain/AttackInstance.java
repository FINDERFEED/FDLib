package com.finderfeed.fdlib.systems.entity.action_chain;

public class AttackInstance {

    public Attack attack;

    public int tick;

    public int stage;

    public AttackInstance(Attack attack){
        this.attack = attack;
    }

    public void nextStage(){
        tick = 0;
        stage++;
    }


}
