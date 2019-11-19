package io.beam.exp.core.outputStream.bigquery;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.DatasetInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptoDataBigQueryOutputStream <T>  {

    // Create a service instance
    BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
    private static final String datasetId = "Crypto";

    public CryptoDataBigQueryOutputStream() {
        bigquery.create(DatasetInfo.newBuilder(datasetId).build());

    }


    public void write(T q) throws Exception {

    }
}
