package io.beam.exp.outputstream;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import io.beam.exp.cryptorealtime.model.TradeEx;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class TradeExFireBaseOutputStream extends CryptoDataFireBaseOutputStream<TradeEx> implements CryptoDataOutputStream<TradeEx> {

    private final static String CollectionName = "TradeEx";
    public TradeExFireBaseOutputStream() throws FileNotFoundException, IOException {
        super();
    }

    @Override
    public void write(TradeEx t) throws Exception{
        super.write(CollectionName, TradeEx.getKey(t), t);
    }
}
