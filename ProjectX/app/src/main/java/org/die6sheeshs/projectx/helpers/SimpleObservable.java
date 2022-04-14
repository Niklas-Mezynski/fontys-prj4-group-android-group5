package org.die6sheeshs.projectx.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleObservable <T>{

    List<SimpleObserver<T>> observers = new ArrayList<>();

    public void runActions(T observedValue){
        observers.forEach(o -> o.doAction(observedValue));
    }

    public void subscribe(SimpleObserver<T> o) {
        observers.add(o);
    }
}
