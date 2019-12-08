package io.beam.exp.cryptorealtime.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public class Quote {
    private String exchange;
    private String currencyPair;
    private double open;
    private double last;
    private double bid;
    private double ask;
    private double high;
    private double low;

    private double volume;
    private double quoteVolume;
    protected Date timestamp;

    public String getExchange() {
        return Optional.ofNullable(this.exchange).map(Function.identity()).orElse("DEFAULT");
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(double quoteVolume) {
        this.quoteVolume = quoteVolume;
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

    public static String getKey(Quote q){
        String key = String.format("%s_%s_%d",q.getExchange(),q.currencyPair, q.timestamp.getTime());
        key=key.replaceAll("/|\\\\","");
        return key;
    }
}
