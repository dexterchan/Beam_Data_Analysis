package io.beam.exp.outputstream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import model.Quote;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class QuoteFireBaseOutputStream implements CryptoDataOutputStream<Quote> {
    Logger log = LoggerFactory.getLogger(QuoteFireBaseOutputStream.class);
    private Firestore myDB = null;
    private final static String CollectionName = "Quote";
    public QuoteFireBaseOutputStream() throws FileNotFoundException, IOException {
        myDB = FirebaseDBHelper.getFireStoreDB();
    }

    @Override
    public void write(Quote q) throws Exception{
        //asynchronously write data

        ApiFuture<WriteResult> result =  myDB.collection(CollectionName).document(Quote.getKey(q)).set(q);
        log.debug("Update time : " + result.get().getUpdateTime());
    }
}
