package io.beam.exp.service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class SubscriptionStatus {
    @Getter @Setter
    private String exchange;
    @Getter @Setter
    private String baseCcy;
    @Getter @Setter
    private String counterCcy;
    @Getter
    private final boolean TurnOn;
    @Getter
    private final String QuoteStatus ;
    @Getter
    private final String TradeExStatus;


}
