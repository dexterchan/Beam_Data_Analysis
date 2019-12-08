package io.beam.exp.cryptorealtime.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class TradeEx {
    public enum OrderType{
        BID,ASK
    };

    protected  String exchange;
    protected  OrderType type;
    protected  double originalAmount;
    protected  String currencyPair;
    protected  double price;
    protected  Date timestamp;

    public String getExchange() {
        return Optional.ofNullable(this.exchange).map(Function.identity()).orElse("DEFAULT");
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamplong(){return timestamp.getTime();}

    @Override
    public String toString() {
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
        return g.toJson(this);
    }

    public static String getKey(TradeEx t){
        String key = String.format("%s_%s_%s_%d_%s",t.getExchange(),t.getCurrencyPair(), t.getType() ,t.timestamp.getTime(), UUID.randomUUID().toString());
        key=key.replaceAll("/|\\\\","");
        return key;
    }

}
