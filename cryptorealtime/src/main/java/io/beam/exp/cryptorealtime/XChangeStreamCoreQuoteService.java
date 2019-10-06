package io.beam.exp.cryptorealtime;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import model.Quote;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XChangeStreamCoreQuoteService implements ExchangeQuoteInterface {
    Logger log= LoggerFactory.getLogger(XChangeStreamCoreQuoteService.class);

    CurrencyPair pair=null;
    StreamingExchange exchange =null;
    @Override
    public void subscribe(Supplier<Quote> handle) {

    }


    protected XChangeStreamCoreQuoteService(){}

    @Override
    public void close() throws Exception {
        log.info("Closing subscription: {}",pair.toString());
        Optional.ofNullable(exchange).ifPresent(x->x.disconnect());
    }

    public static ExchangeQuoteInterface of(String currencyExchangeName, String baseSymbol, String counterSymbol){
        XChangeStreamCoreQuoteService exch = new XChangeStreamCoreQuoteService();
        exch.exchange = StreamingExchangeFactory.INSTANCE.createExchange(currencyExchangeName);
        exch.pair = new CurrencyPair(String.format("%s/%s",baseSymbol,counterSymbol));

        return exch;
    }

}
