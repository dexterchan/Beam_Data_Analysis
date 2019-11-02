package io.beam.exp.outputstream;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import model.TradeEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class TradeExFireBaseOutputStream  implements CryptoDataOutputStream<TradeEx> {
    Logger log = LoggerFactory.getLogger(QuoteFireBaseOutputStream.class);
    private Firestore myDB = null;
    private final static String CollectionName = "TradeEx";
    public TradeExFireBaseOutputStream() throws FileNotFoundException, IOException {
        myDB = FirebaseDBHelper.getFireStoreDB();
    }

    @Override
    public void write(TradeEx t) throws Exception{
        //asynchronously write data

        ApiFuture<WriteResult> result =  myDB.collection(CollectionName).document(TradeEx.getKey(t)).set(t);
        log.debug("Update time : " + result.get().getUpdateTime());
    }
}
