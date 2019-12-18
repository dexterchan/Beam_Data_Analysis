package io.beam.exp.core.service.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.observe.Subject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Subscription <T> implements Subject<T> {
    @Getter @Setter
    private  String exchange;
    @Getter @Setter
    private  String baseCcy;
    @Getter @Setter
    private  String counterCcy;
    @Getter
    private String dataName;

    private  boolean active=true;
    @Getter @Setter
    private  SubscriptionStatus subscriptionStatus = SubscriptionStatus.WAIT;
    private Set<Observer> observerSet = new HashSet<>();

    public Subscription(String exchange, String baseCcy, String counterCcy, String dataName){
        this.exchange = exchange;
        this.baseCcy = baseCcy;
        this.counterCcy = counterCcy;
        this.dataName = dataName;
    }



    @Override
    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    @Override
    public boolean isActive() {
        return this.active;
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

    @Override
    public String toString() {
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
        return g.toJson(this);
    }

    @Override
    public Map<String, String> getDescription() {
        log.debug("Get description");
        Map<String, String> m = new HashMap<>();
        m.put("dataName", dataName);
        m.put("baseCcy",this.baseCcy);
        m.put("counterCcy",this.counterCcy);
        m.put("active",String.valueOf(this.active));

        StringBuilder sb = new StringBuilder();
        this.observerSet.forEach(
                observer -> {
                    sb.append(observer.getDescription());
                    sb.append(",");
                }
        );
        m.put("observers", sb.toString());

        log.debug("Get description return");
        return  m;
    }
}
