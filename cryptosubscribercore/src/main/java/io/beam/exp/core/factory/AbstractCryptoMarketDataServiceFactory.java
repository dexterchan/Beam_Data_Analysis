package io.beam.exp.core.factory;

import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;

public interface AbstractCryptoMarketDataServiceFactory {
    CryptoMarketDataService<Quote> createQuoteService();
    CryptoMarketDataService<TradeEx> createTradeService();
}
