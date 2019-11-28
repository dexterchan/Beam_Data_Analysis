package io.beam.exp.blockchaininfo.outputstream.bigquery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.bigquery.*;
import io.beam.exp.blockchaininfo.model.BitcoinState;
import io.beam.exp.blockchaininfo.outputstream.BlockchaininfoOutputStream;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class BitcoinBQBlockchaininfoOutputStream implements BlockchaininfoOutputStream<BitcoinState> {

    // Create a service instance
    private static BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
    private static final String datasetId = "Crypto";
    private static final String tablename = "BitCoinStatus";
    private TableId tableId = null;

    public BitcoinBQBlockchaininfoOutputStream(){
        tableId = TableId.of(datasetId, tablename);

    }
    private Map<String, Object> getMapFromPOJO(BitcoinState obj){
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> m = oMapper.convertValue(obj, Map.class);
        m.put("timestamp", (Long)m.get("timestamp")/1000);
        return m;
    }
    @Override
    public void write(BitcoinState obj) throws Exception {
        Map<String, ?> objMap = getMapFromPOJO(obj);
        InsertAllRequest insertRequest =
                InsertAllRequest.newBuilder(tableId).addRow(objMap).build();
        // Insert rows
        InsertAllResponse insertResponse = bigquery.insertAll(insertRequest);
        // Check if errors occurred
        if (insertResponse.hasErrors()) {
            log.error(obj.toString());
            log.error(objMap.toString());
            log.error(insertResponse.toString());
        }
    }
}
