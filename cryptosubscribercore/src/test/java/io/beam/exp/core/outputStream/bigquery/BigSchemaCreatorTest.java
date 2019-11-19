package io.beam.exp.core.outputStream.bigquery;

import com.google.cloud.bigquery.Schema;
import model.TradeEx;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BigSchemaCreatorTest {

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
        BigSchemaCreator bigSchemaCreator = new BigSchemaCreator();
        Map<String, String> schema = bigSchemaCreator.bqschemaHelper(TradeEx.class);
        assertThat(schema).containsEntry("exchange","STRING");
        assertThat(schema).containsEntry("bid","NUMERIC");
        assertThat(schema).hasSize(11);

    }

    @Test
    void createSchema() {
        TradeEx trade = new TradeEx();
        BigSchemaCreator bigSchemaCreator = new BigSchemaCreator();
        Schema schema = bigSchemaCreator.createSchema(trade.getClass());
        assertThat(schema).isNotNull();
    }
}