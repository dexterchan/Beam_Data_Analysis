package io.beam.exp.core.service.model;

import lombok.RequiredArgsConstructor;
import model.Quote;
import model.TradeEx;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor(staticName="of")
public class MarketData {
    String exchange;
    String baseCcy;
    String counterCcy;

    private Map<Class, Subscription> subscriptionMap = new HashMap<>();

    public static MarketData createMarketData(String exchange, String baseCcy, String counterCcy ){
        MarketData marketData = MarketData.of();
        marketData.baseCcy=baseCcy;
        marketData.counterCcy=counterCcy;
        marketData.exchange=exchange;
        return marketData;
    }

    public void addSubscription(Class c){
        subscriptionMap.put (c, new Subscription(exchange,baseCcy,counterCcy));
    }

    public Subscription<?> getSubscription(Class c){
        if (subscriptionMap.containsKey(c)){
            return subscriptionMap.get(c);
        }else{
            throw new IllegalArgumentException(String.format("Class: %c is not found in subscription", c.getName()));
        }
    }

}
