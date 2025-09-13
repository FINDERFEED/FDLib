package com.finderfeed.fdlib.data_structures;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class IntRangedList<T> implements Iterable<T> {



    private List<IntRangedListEntry<T>> entries = new ArrayList<>();

    public IntRangedList(){

    }

    public IntRangedList(IntRangedList<T> other){
        this.entries.addAll(other.entries);
    }

    public IntRangedList(List<T> objects, boolean isSorted, Function<T, Integer> intExtractor){
        if (isSorted){
            for (T o : objects){
                entries.add(new IntRangedListEntry<>(intExtractor.apply(o),o));
            }
        }else{
            for (T o : objects){
                this.add(intExtractor.apply(o),o,false);
            }
        }
    }


    public T getValue(int num){
        return entries.get(indexOf(num)).value;
    }

    public int indexOf(int num){
        if (entries.isEmpty()) return -1;
        if (entries.size() == 1) return 0;
        var first = entries.get(0);
        if (num <= first.num){
            return 0;
        }else{
            var last = entries.get(entries.size() - 1);
            if (num >= last.num){
                return entries.size() - 1;
            }
        }
        for (int i = 1; i < entries.size();i++){
            var entry = entries.get(i);
            if (num < entry.num){
                return i - 1;
            }
        }
        return -1;
    }

    public List<T> getValues(int number,int before,int after){
        List<T> r = new ArrayList<>(); for (int i = 0; i < before + after + 1;i++) r.add(null);
        int entryIndex = this.indexOf(number);
        r.set(before,entries.get(entryIndex).value);
        int listIndex = before + 1;
        int t = 1;
        while (t <= after){
            int id = entryIndex + t;
            if (id < entries.size()){
                r.set(listIndex,entries.get(id).value);
            }else{
                break;
            }
            t++;
            listIndex++;
        }
        listIndex = 0;
        while (before > 0){
            int id = entryIndex - before;
            if (id >= 0){
                r.set(listIndex,entries.get(id).value);
            }
            before--;
            listIndex++;
        }
        return r;
    }

    public List<T> getAllValuesAfter(int number){
        if (entries.isEmpty()) return null;
        int index = indexOf(number);
        List<T> l = new ArrayList<>();
        var value = this.entries.get(index);
        if (value.num >= number){
            l.add(value.value);
        }
        for (int i = index + 1; i < entries.size();i++){
            l.add(entries.get(i).value);
        }
        return l;
    }




    public void add(int num,T value,boolean replace){
        int index = 0;
        for (var entry : entries){
            int n = entry.num;
            if (n > num){
                break;
            }else if (num == n){
                if (!replace) {
                    throw new RuntimeException("Trying to add element with the same int value");
                }else{
                    entries.set(index,new IntRangedListEntry<>(num,value));
                    return;
                }
            }
            index++;
        }
        entries.add(index,new IntRangedListEntry<>(num,value));
    }

    public T get(int index){
        return this.entries.get(index).value;
    }

    public int size(){
        return this.entries.size();
    }

    public T getFirst(){
        return this.entries.get(0).value;
    }

    public T getLast(){
        return this.entries.get(entries.size() - 1).value;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.entries.stream().map(IntRangedListEntry::value).iterator();
    }

    public boolean isEmpty(){
        return this.entries.isEmpty();
    }

    private record IntRangedListEntry<T>(int num,T value){}
}
