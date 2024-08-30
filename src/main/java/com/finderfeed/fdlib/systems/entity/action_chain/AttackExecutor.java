package com.finderfeed.fdlib.systems.entity.action_chain;

@FunctionalInterface
public interface AttackExecutor {

    boolean execute(AttackInstance attack);

}
