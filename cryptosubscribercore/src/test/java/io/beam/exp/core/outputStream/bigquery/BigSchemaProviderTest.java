package io.beam.exp.core.outputStream.bigquery;

import com.google.cloud.bigquery.Schema;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BigSchemaProviderTest {

    @Test
    void testbqschemaHelper() {
        String TradeExJsonRef="{" +
                "  \"timestamp\": \"TIMESTAMP\"," +
                "  \"exchange\": \"STRING\"," +
                "  \"currencyPair\": \"STRING\"," +
                "  \"open\": \"NUMERIC\"," +
                "  \"last\": \"NUMERIC\"," +
                "  \"bid\": \"NUMERIC\"," +
                "  \"ask\": \"NUMERIC\"," +
                "  \"high\": \"NUMERIC\"," +
                "  \"low\": \"NUMERIC\"," +
                "  \"volume\": \"NUMERIC\"," +
                "  \"quoteVolume\": \"NUMERIC\"" +
                "}";
        BigSchemaProvider bigSchemaProvider = new BigSchemaProvider();
        Map<String, String> schema = bigSchemaProvider.bqschemaHelper(Quote.class);
        assertThat(schema).containsEntry("exchange","STRING");
        assertThat(schema).containsEntry("bid","NUMERIC");
        assertThat(schema).hasSize(11);

    }

    @Test
    void createTradeExSchema() {
        TradeEx trade = new TradeEx();
        BigSchemaProvider bigSchemaProvider = new BigSchemaProvider();
        Schema schema = bigSchemaProvider.provideSchema(trade.getClass());
        assertThat(schema).isNotNull();

    }
    @Test
    void createQuoteExSchema(){
        Quote quote = new Quote();
        BigSchemaProvider bigSchemaProvider = new BigSchemaProvider();
        Schema schema = bigSchemaProvider.provideSchema(quote.getClass());
        assertThat(schema).isNotNull();
    }
}