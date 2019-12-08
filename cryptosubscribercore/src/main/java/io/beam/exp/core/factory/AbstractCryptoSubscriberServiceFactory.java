package io.beam.exp.core.factory;

import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;

public interface AbstractCryptoSubscriberServiceFactory {
    CryptoSubscriberService<Quote> createQuoteService();
    CryptoSubscriberService<TradeEx> createTradeService();
}
