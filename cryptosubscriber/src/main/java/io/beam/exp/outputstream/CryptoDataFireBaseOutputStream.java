package io.beam.exp.outputstream;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CryptoDataFireBaseOutputStream <T>  {

    Logger log = LoggerFactory.getLogger(CryptoDataFireBaseOutputStream.class);
    private Firestore myDB = null;

    public CryptoDataFireBaseOutputStream() throws FileNotFoundException, IOException {
        myDB = FirebaseDBHelper.getFireStoreDB();
    }

    public void write(String CollectionName, String key, T obj) throws Exception {
        //asynchronously write data
        ApiFuture<WriteResult> result =  myDB.collection(CollectionName).document(key).set(obj);
        log.debug("Update time : " + result.get().getUpdateTime());
    }
}
