package io.beam.exp.blockchaininfo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/*

    getdifficulty - Current difficulty target as a decimal number
    getblockcount - Current block height in the longest chain
    latesthash - Hash of the latest block
    bcperblock - Current block reward in BTC
    totalbc - Total Bitcoins in circulation (delayed by up to 1 hour])
    probability - Probability of finding a valid block each hash attempt
    hashestowin - Average number of hash attempts needed to solve a block
    nextretarget - Block height of the next difficulty retarget

    avgtxsize - Average transaction size for the past 1000 blocks. Change the number of blocks by passing an integer as the second argument e.g. avgtxsize/2000
    avgtxvalue - Average transaction value (1000 Default)
    interval - average time between blocks in seconds
    eta - estimated time until the next block (in seconds)
    avgtxnumber - Average number of transactions per block (100 Default)

 */
@Getter @Setter
public class BitcoinState implements Cloneable{
    double difficulty;
    long blockcount;
    long bcperblock;
    long totalbc;
    double probability;
    long hashestowin;
    long nextretarget;
    double avgtxsize;
    double avgtxvalue;
    double interval;
    double eta;
    double avgtxnumber;
    Date timestamp;

    @Override
    public String toString() {
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
        return g.toJson(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
