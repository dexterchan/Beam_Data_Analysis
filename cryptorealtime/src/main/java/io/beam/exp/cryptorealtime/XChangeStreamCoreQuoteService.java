package io.beam.exp.cryptorealtime;

import com.google.common.collect.ImmutableMap;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.hitbtc.HitbtcStreamingExchange;
import io.reactivex.disposables.Disposable;
import model.OrderBook;
import model.Quote;
import model.TradeEx;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XChangeStreamCoreQuoteService implements ExchangeQuoteInterface {
    Logger log = LoggerFactory.getLogger(XChangeStreamCoreQuoteService.class);

    CurrencyPair pair = null;
    StreamingExchange exchange = null;
    Disposable orderBooksubscription = null;
    Disposable tickSubscription = null;

    private static final Map<String, String> exchangeClassMap = ImmutableMap.of(
            "hitbtc", HitbtcStreamingExchange.class.getName());

    public static ExchangeQuoteInterface of(String currencyExchangeName, String baseSymbol, String counterSymbol) {

        String exchName = Optional.of(exchangeClassMap.get(currencyExchangeName)).get();

        XChangeStreamCoreQuoteService exch = new XChangeStreamCoreQuoteService();
        exch.exchange = StreamingExchangeFactory.INSTANCE.createExchange(exchName);
        exch.pair = new CurrencyPair(String.format("%s/%s", baseSymbol, counterSymbol));

        return exch;
    }

    @Override
    public void subscribe(Consumer<TradeEx> handle, Consumer<Quote> ohandle) {
        // Connect to the Exchange WebSocket API. Blocking wait for the connection.
        exchange.connect().blockingAwait();

        // Subscribe to live trades update.
        exchange.getStreamingMarketDataService()
                .getTrades(pair)
                .subscribe(trade -> {
                    TradeEx q = convertTradeEx(trade);
                    handle.accept(q);
                }, throwable -> {
                    log.error("Error in subscribing trades.", throwable);
                });
/*
        // Subscribe order book data with the reference to the subscription.
         orderBooksubscription = exchange.getStreamingMarketDataService()
                .getOrderBook(pair)
                .subscribe(orderBook -> {
                    // Do something
                    log.info("Incoming orderBook: {}", orderBook);
                    OrderBook o=new OrderBook();
                    ohandle.accept(o);
                });*/

        tickSubscription = exchange.getStreamingMarketDataService()
                .getTicker(pair)
                .subscribe(tick -> {
                    Quote q = convertQuote(tick);
                    ohandle.accept(q);
                }, throwable -> {
                    log.error("Error in subscribing quote.", throwable);
                });


    }

    @Override
    public void unsubscribe() throws Exception{
        this.close();
    }

    static TradeEx convertTradeEx(Trade t) {
        TradeEx q = new TradeEx();
        q.setCurrencyPair(t.getCurrencyPair().toString());
        q.setOriginalAmount(
                Optional.ofNullable(t.getOriginalAmount()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setPrice(
                Optional.ofNullable(t.getPrice()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setTimestamp(t.getTimestamp());
        if (t.getType() == Order.OrderType.ASK) {
            q.setType(TradeEx.OrderType.ASK);
        } else if (t.getType() == Order.OrderType.BID) {
            q.setType(TradeEx.OrderType.BID);
        }
        return q;
    }

    static Quote convertQuote(Ticker t) {
        Quote q = new Quote();
        q.setVolume(Optional.ofNullable(t.getVolume()).map(BigDecimal::doubleValue).orElse(0.0));
        q.setAsk(Optional.ofNullable(t.getAsk()).map(BigDecimal::doubleValue).orElse(0.0));
        q.setBid(
                Optional.ofNullable(t.getBid()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setCurrencyPair(t.getCurrencyPair().toString());
        q.setHigh(
                Optional.ofNullable(t.getHigh()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setLast(
                Optional.ofNullable(t.getLast()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setLow(
                Optional.ofNullable(t.getLow()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setOpen(
                Optional.ofNullable(t.getOpen()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setQuoteVolume(
                Optional.ofNullable(t.getQuoteVolume()).map(BigDecimal::doubleValue).orElse(0.0)
        );
        q.setTimestamp(t.getTimestamp());

        return q;
    }


    protected XChangeStreamCoreQuoteService() {
    }

    @Override
    public void close() throws Exception {
        log.info("Closing subscription: {}", pair.toString());
        Optional.ofNullable(exchange).ifPresent(x -> x.disconnect());
        //Optional.ofNullable(orderBooksubscription).ifPresent(x->x.dispose());
        Optional.ofNullable(tickSubscription).ifPresent(x -> x.dispose());
    }



}
