package com.sel.smartfood.data.model;

public class Emitter<T> {
    private boolean isHandled;
    private T data;

    public Emitter(T data){
        this.data = data;
        this.isHandled = false;
    }

    public T getData(){
        if (!isHandled){
            isHandled = true;
            return data;
        }
        return null;
    }

    public boolean isHandled(){
        return isHandled;
    }
}
