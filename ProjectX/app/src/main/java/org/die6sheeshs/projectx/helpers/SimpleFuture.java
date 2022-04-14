package org.die6sheeshs.projectx.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleFuture<T> {

    private T value = null;
    private boolean hasBeenSet = false;
    private List<Consumer<T>> consumers = new ArrayList<>();

    public void setValue(T value){
        if(hasBeenSet){
            throw new UnsupportedOperationException("May not set value more than once");
        }
        this.value = value;
        consumers.forEach(c -> c.accept(value));
        hasBeenSet = true;
    }

    public void doActionWhenValueSet(Consumer<T> action){
        if(hasBeenSet){
            action.accept(value);
        }else{
            consumers.add(action);
        }
    }
}
