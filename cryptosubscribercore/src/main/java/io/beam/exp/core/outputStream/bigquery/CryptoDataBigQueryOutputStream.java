package io.beam.exp.core.outputStream.bigquery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.bigquery.*;
import io.beam.exp.core.outputStream.CryptoDataOutputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.util.Map;

@Slf4j
public class CryptoDataBigQueryOutputStream <T> implements CryptoDataOutputStream<T> {

    // Create a service instance
    private static BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
    private static final String datasetId = "Crypto";

    private TableId tableId = null;

    public CryptoDataBigQueryOutputStream(Class c, String tableName) {
        try {
            bigquery.create(DatasetInfo.newBuilder(datasetId).build());
        }catch(BigQueryException be){
            log.error(be.getMessage());
            if(!be.getReason().equals("duplicate")){
                throw be;
            }
        }
        tableId = TableId.of(datasetId, tableName);

        Schema schema = null;
        try {
            BigSchemaProvider bigSchemaProvider = new BigSchemaProvider();
            schema = bigSchemaProvider.provideSchema(c);
        }catch(Exception ex){
            log.info("Cannot find Schema json file for "+c.getName());
            log.info("Abort table creation");
            return;
        }

        // Create a table
        StandardTableDefinition tableDefinition = StandardTableDefinition.of(schema);
        try {
            bigquery.create(TableInfo.of(tableId, tableDefinition));
        }catch(BigQueryException be){
            log.error(be.getMessage());
            if(!be.getReason().equals("duplicate")){
                throw be;
            }
        }

    }

    private Map<String, Object> getMapFromPOJO(T obj){
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> m = oMapper.convertValue(obj, Map.class);
        m.put("timestamp", (Long)m.get("timestamp")/1000);
        return m;
    }

    @Override
    public void write(T obj) throws Exception {

        Map<String, ?> objMap = getMapFromPOJO(obj);
        InsertAllRequest insertRequest =
                InsertAllRequest.newBuilder(tableId).addRow(objMap).build();
        // Insert rows
        InsertAllResponse insertResponse = bigquery.insertAll(insertRequest);
        // Check if errors occurred
        if (insertResponse.hasErrors()) {
            log.error(objMap.toString());
            log.error(insertResponse.toString());
        }
    }
}
