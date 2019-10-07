package model;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;
import java.util.Date;

public class TradeEx {
    public enum OrderType{
        BID,ASK
    };

    protected  OrderType type;
    protected  BigDecimal originalAmount;
    protected  String currencyPair;
    protected  BigDecimal price;
    protected  Date timestamp;

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TradeEx{" +
                "type=" + type +
                ", originalAmount=" + originalAmount +
                ", currencyPair='" + currencyPair + '\'' +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}
