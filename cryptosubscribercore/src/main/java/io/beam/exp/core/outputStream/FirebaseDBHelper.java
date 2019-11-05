package io.beam.exp.core.outputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseDBHelper {
    private static boolean initialized = false;


    public synchronized static  Firestore getFireStoreDB() throws IOException {
        Firestore myDB = null;

        if (!initialized) {
            String serviceAcctFile = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            InputStream serviceAccount = new FileInputStream(serviceAcctFile);
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            initialized = true;
        }

        myDB = FirestoreClient.getFirestore();
        return myDB;
    }
}
