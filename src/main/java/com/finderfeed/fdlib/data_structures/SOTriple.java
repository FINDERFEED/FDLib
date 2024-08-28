package com.finderfeed.fdlib.data_structures;

//single object triple
public class SOTriple<T> {

    private T left;
    private T center;
    private T right;

    public SOTriple(T left,T center,T right){
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public T getCenter() {
        return center;
    }

    public T getRight() {
        return right;
    }

}
