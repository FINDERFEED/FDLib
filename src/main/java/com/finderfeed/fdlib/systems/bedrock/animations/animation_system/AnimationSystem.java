package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.AnimationContext;
import com.finderfeed.fdlib.systems.bedrock.animations.TransitionAnimation;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;

import java.util.HashMap;

public abstract class AnimationSystem {

    private HashMap<String,AnimationTicker> tickers = new HashMap<>();

    private HashMap<String,Float> variables = new HashMap<>();

    private boolean isFrozen;

    public void tick(){
        if (this.isFrozen()) return;
        var entryIterator = tickers.entrySet().iterator();
        while (entryIterator.hasNext()){
            var entry = entryIterator.next();
            AnimationTicker ticker = entry.getValue();
            Animation animation = ticker.getAnimation();

            if (ticker.hasEnded()){
                Animation.LoopMode mode = ticker.getLoopMode();
                if (animation.isToNullTransition()){
                    entryIterator.remove();
                    continue;
                }
                AnimationContext context = new AnimationContext(animation, mode);
                context.variables = this.variables;
                ticker.addVariables(context,0);
                switch (mode) {
                    case ONCE -> {
                        int toNullTime = ticker.getToNullTransitionTime();
                        if (toNullTime != 0) {
                            Animation nullTransition = animation.createTransitionTo(context, null, ticker.getTime(0), ticker.getToNullTransitionTime(),false);
                            ticker.resetTime();
                            ticker.setAnimation(nullTransition);
                        }else{
                            entryIterator.remove();
                            continue;
                        }
                    }
//                    case LOOP -> {
//                        ticker.resetTime();
//                        if (animation.isTransition()){
//                            ticker.setAnimation(((TransitionAnimation)animation).getTransitionTo());
//                        }
//                    }
                    case HOLD_ON_LAST_FRAME -> {
                        continue;
                    }
                }
            }
            ticker.tick();
        }
    }



    public void applyAnimations(FDModel model,float partialTicks){
        model.resetTransformations();
        for (var entry : tickers.values()){
            Animation animation = entry.getAnimation();
            AnimationContext context = new AnimationContext(animation, entry.getLoopMode());
            context.variables = variables;
            entry.addVariables(context,partialTicks);
            animation.applyAnimation(context,model,entry.getTime(partialTicks));
        }
    }


    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
        this.onFreeze(frozen);
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void startAnimation(String name, AnimationTicker ticker){
        AnimationTicker current = this.getTicker(name);
        if (current == null){
            this.tickers.put(name,ticker);
        }else{
            if (ticker.equals(current)) return;
            Animation anim = current.getAnimation();
            AnimationContext context = new AnimationContext(anim, ticker.getLoopMode());
            context.variables = this.variables;
            current.addVariables(context,0);
            Animation transition = current.getAnimation().createTransitionTo(context,
                    ticker.getAnimation(),
                    current.getTime(0),
                    ticker.getToNullTransitionTime(),
                    ticker.isReversed()
            );
            AnimationTicker newTicker = new AnimationTicker(ticker);
            newTicker.setAnimation(transition);
            newTicker.resetTime();
            this.tickers.put(name,newTicker);
        }
        this.onAnimationStart(name,ticker);
    }

    public void stopAnimation(String name){
        AnimationTicker ticker = this.getTicker(name);
        if (ticker != null){
            Animation animation = ticker.getAnimation();
            if (!animation.isToNullTransition()){
                if (ticker.getToNullTransitionTime() != 0) {
                    AnimationContext context = new AnimationContext(animation, Animation.LoopMode.ONCE);
                    context.variables = this.variables;
                    ticker.addVariables(context,0);
                    Animation toNull = animation.createTransitionTo(context, null, ticker.getTime(0),
                            ticker.getToNullTransitionTime(),false);
                    AnimationTicker newTicker = new AnimationTicker(ticker);
                    newTicker.setLoopMode(Animation.LoopMode.ONCE);
                    newTicker.setAnimation(toNull);
                    newTicker.resetTime();
                    this.tickers.put(name,newTicker);
                }else{
                    this.tickers.remove(name);
                }
            }
        }
        this.onAnimationStop(name);
    }


    public void setVariable(String name,float variable){
        this.variables.put(name,variable);
        this.onVariableAdded(name,variable);
    }

    public float getVariable(String name){
        return this.variables.get(name);
    }

    public AnimationTicker getTicker(String name){
        return tickers.get(name);
    }

    public Animation getTickerAnimation(String ticker){
        if (this.tickers.containsKey(ticker)){
            return this.getTicker(ticker).getAnimation();
        }else{
            return null;
        }
    }

    /*
        Not advised to modify, is used for a sync packet
     */
    public HashMap<String, AnimationTicker> getTickers() {
        return tickers;
    }

    public abstract void onAnimationStart(String name, AnimationTicker ticker);
    public abstract void onAnimationStop(String name);
    public abstract void onFreeze(boolean state);
    public abstract void onVariableAdded(String name,float variable);

}
