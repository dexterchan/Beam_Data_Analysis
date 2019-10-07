package io.beam.exp.cryptorealtime;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
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

    @Override
    public void subscribe(Consumer<TradeEx> handle, Consumer<Quote> ohandle) {
        // Connect to the Exchange WebSocket API. Blocking wait for the connection.
        exchange.connect().blockingAwait();
        BlockingQueue<Quote> blockingQueue = new LinkedBlockingQueue<Quote>();

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

    static TradeEx convertTradeEx(Trade t) {
        TradeEx q = new TradeEx();
        q.setCurrencyPair(t.getCurrencyPair().toString());
        q.setOriginalAmount(t.getOriginalAmount());
        q.setPrice(t.getPrice());
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
        q.setVolume(t.getVolume());
        q.setAsk(t.getAsk());
        q.setBid(t.getBid());
        q.setCurrencyPair(t.getCurrencyPair().toString());
        q.setHigh(t.getHigh());
        q.setLast(t.getLast());
        q.setLow(t.getLow());
        q.setOpen(t.getOpen());
        q.setQuoteVolume(t.getQuoteVolume());
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

    public static ExchangeQuoteInterface of(String currencyExchangeName, String baseSymbol, String counterSymbol) {
        XChangeStreamCoreQuoteService exch = new XChangeStreamCoreQuoteService();
        exch.exchange = StreamingExchangeFactory.INSTANCE.createExchange(currencyExchangeName);
        exch.pair = new CurrencyPair(String.format("%s/%s", baseSymbol, counterSymbol));

        return exch;
    }

}
