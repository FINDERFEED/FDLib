package com.finderfeed.fdlib.systems.screen;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FDSCChildren implements Iterable<FDScreenComponent> {

    private FDScreenComponent owner;
    private HashMap<String,FDScreenComponent> children;
    private FDScreen screen;

    public FDSCChildren(FDScreenComponent parent,FDScreen screen){
        this.screen = screen;
        this.owner = parent;
        this.children = new LinkedHashMap<>();
    }

    public void tick(){
        Iterator<Map.Entry<String,FDScreenComponent>> iterator = children.entrySet().iterator();
        while (iterator.hasNext()){
            var entry = iterator.next();
            var cmp = entry.getValue();
            if (cmp.isRemoved()){
                iterator.remove();
                cmp.setParent(null);
                this.getScreen().removeComponent(cmp.getUniqueId());
            }else{
                cmp.tick();
            }
        }
    }

    public void removeChild(String id){
        FDScreenComponent component = this.children.remove(id);
        this.owner.onChildRemoved(component);
        component.setParent(null);
        screen.removeComponent(id);
    }

    public void setAsChild(FDScreenComponent child){
        String id = child.getUniqueId();
        if (screen.getComponentById(id) == null){
            screen.addComponent(child);
        }
        FDScreenComponent parent = child.getParent();
        if (parent != null){
            FDSCChildren c = parent.getChildren();
            c.children.remove(id);
        }
        child.setParent(this.getOwner());
        this.children.put(id,child);
        this.owner.onChildAdded(child);
    }


    public void removeAllChildren(){
        Iterator<Map.Entry<String,FDScreenComponent>> iterator = children.entrySet().iterator();
        while (iterator.hasNext()){
            var entry = iterator.next();
            iterator.remove();
            this.owner.onChildRemoved(entry.getValue());
            entry.getValue().setParent(null);
            screen.removeComponent(entry.getKey());
        }
    }

    public boolean hasChild(String id){
        return children.containsKey(id);
    }

    public FDScreen getScreen() {
        return screen;
    }

    public FDScreenComponent getOwner() {
        return owner;
    }

    public List<FDScreenComponent> getChildren() {
        return children.values().stream().toList();
    }

    @NotNull
    @Override
    public Iterator<FDScreenComponent> iterator() {
        return children.values().iterator();
    }
}
