package io.beam.exp.core.service.model;


import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.observe.Subject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Subscription <T> implements Subject<T> {
    @Getter @Setter
    private  String exchange;
    @Getter @Setter
    private  String baseCcy;
    @Getter @Setter
    private  String counterCcy;
    @Getter @Setter
    private  boolean TurnOn=true;
    @Getter @Setter
    private  SubscriptionStatus subscriptionStatus = SubscriptionStatus.WAIT;
    private Set<Observer> observerSet = new HashSet<>();

    public Subscription(String exchange, String baseCcy, String counterCcy){
        this.exchange = exchange;
        this.baseCcy = baseCcy;
        this.counterCcy = counterCcy;
    }

    public String getType(){
        return this.getClass().getName();
    }


    @Override
    public void registerObserver( Observer<T> o) {
        subscriptionStatus = SubscriptionStatus.RUN;
        if (!observerSet.contains(o)) {
            observerSet.add(o);
        }
    }

    @Override
    public void removeObserver( Observer<T> o) {
        observerSet.remove(o);
        if (observerSet.size()==0){
            subscriptionStatus = SubscriptionStatus.STOP;
        }
    }

    @Override
    public void notifyOservers(T msg) {
        if (msg != null && subscriptionStatus==SubscriptionStatus.RUN) {
            observerSet.forEach(o->{
                o.update(msg);
            });
        }
    }

    @Override
    public void notifyObservers(Throwable ex) {
        if (ex != null && subscriptionStatus==SubscriptionStatus.RUN) {
            observerSet.forEach(o->{
                o.throwError(ex);
            });
        }
    }
}
