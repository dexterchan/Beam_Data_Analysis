package io.beam.exp.core.factory;

import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.outputStream.bigquery.CryptoDataBigQueryOutputStream;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.core.service.QuoteCryptoMarketDataService;
import io.beam.exp.core.service.TradeCryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class GCP_CryptoSubscriberServiceFactory implements AbstractCryptoSubscriberServiceFactory {

    protected final String QuoteTable;
    protected final String TradeTable;



    @Override
    public CryptoSubscriberService<Quote> createQuoteService() {
        Observer<Quote> quoteObserver = new CryptoDataBigQueryOutputStream<Quote> (Quote.class, QuoteTable);
        CryptoSubscriberService cryptoSubscriberService = new QuoteCryptoMarketDataService();
        cryptoSubscriberService.injectObserver(quoteObserver);
        cryptoSubscriberService.injectObserver(new Observer<Quote>() {
            @Override
            public void update(Quote msg) {
                log.info(msg.toString());
            }

            @Override
            public void throwError(Throwable ex) {
                log.error(ex.getMessage());
            }

            @Override
            public String getDescription() {
                return "log for Quote";
            }
        });
        return cryptoSubscriberService;
    }

    @Override
    public CryptoSubscriberService<TradeEx> createTradeService() {
        Observer<TradeEx> tradeExObserver = new CryptoDataBigQueryOutputStream<TradeEx> (TradeEx.class, TradeTable);
        CryptoSubscriberService cryptoSubscriberService = new TradeCryptoMarketDataService();
        cryptoSubscriberService.injectObserver(tradeExObserver);
        cryptoSubscriberService.injectObserver(new Observer<TradeEx>() {
            @Override
            public void update(TradeEx msg) {
                log.info(msg.toString());
            }

            @Override
            public void throwError(Throwable ex) {
                log.error(ex.getMessage());
            }

            @Override
            public String getDescription() {
                return "log for TradeEx";
            }
        });
        return cryptoSubscriberService;
    }
}
