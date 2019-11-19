package io.beam.exp.core.outputStream.firebase;

import io.beam.exp.core.outputStream.CryptoDataOutputStream;
import model.Quote;

import java.io.FileNotFoundException;
import java.io.IOException;


public class QuoteFireBaseOutputStream extends CryptoDataFireBaseOutputStream<Quote> implements CryptoDataOutputStream<Quote> {

    private final static String CollectionName = "Quote";
    public QuoteFireBaseOutputStream() throws FileNotFoundException, IOException {
       super();
    }

    @Override
    public void write(Quote q) throws Exception{
        super.write(CollectionName, Quote.getKey(q), q);
    }
}
