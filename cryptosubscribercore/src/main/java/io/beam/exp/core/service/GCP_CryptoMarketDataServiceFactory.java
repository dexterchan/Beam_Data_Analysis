package io.beam.exp.core.service;

import io.beam.exp.core.factory.AbstractCryptoMarketDataServiceFactory;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.outputStream.bigquery.CryptoDataBigQueryOutputStream;
import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.core.service.QuoteCryptoMarketDataService;
import io.beam.exp.core.service.TradeCryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class GCP_CryptoMarketDataServiceFactory implements AbstractCryptoMarketDataServiceFactory {

    protected final String QuoteTable;
    protected final String TradeTable;



    @Override
    public CryptoMarketDataService<Quote> createQuoteService() {
        Observer<Quote> quoteObserver = new CryptoDataBigQueryOutputStream<Quote> (Quote.class, QuoteTable);
        CryptoMarketDataService cryptoMarketDataService = new QuoteCryptoMarketDataService();
        cryptoMarketDataService.injectObserver(quoteObserver);
        cryptoMarketDataService.injectObserver(new Observer<Quote>() {
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
                return "System log";
            }
        });
        return cryptoMarketDataService;
    }

    @Override
    public CryptoMarketDataService<TradeEx> createTradeService() {
        Observer<TradeEx> tradeExObserver = new CryptoDataBigQueryOutputStream<TradeEx> (TradeEx.class, TradeTable);
        CryptoMarketDataService cryptoMarketDataService = new TradeCryptoMarketDataService();
        cryptoMarketDataService.injectObserver(tradeExObserver);
        cryptoMarketDataService.injectObserver(new Observer<TradeEx>() {
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
                return "System log";
            }
        });
        return cryptoMarketDataService;
    }
}
