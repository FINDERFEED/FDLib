package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

public class AnimationEvent {

    private int time;
    private boolean hasExecuted = false;
    private Runnable event;

    public AnimationEvent(int time,Runnable event){
        this.event = event;
        this.time = time;
    }

    public boolean hasExecuted() {
        return hasExecuted;
    }

    public void reset(){
        this.hasExecuted = false;
    }

    public void setExecuted(boolean hasExecuted) {
        this.hasExecuted = hasExecuted;
    }

    public void execute(){
        if (!hasExecuted) {
            this.event.run();
            hasExecuted = true;
        }
    }
}
