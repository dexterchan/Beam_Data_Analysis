package io.beam.exp.core.outputStream.firebase;

import io.beam.exp.core.outputStream.CryptoDataOutputStream;
import model.TradeEx;

import java.io.FileNotFoundException;
import java.io.IOException;


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
