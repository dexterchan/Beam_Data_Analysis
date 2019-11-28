package io.beam.exp.blockchaininfo;

import io.beam.exp.blockchaininfo.outputstream.bigquery.BitcoinBQBlockchaininfoOutputStream;
import io.beam.exp.blockchaininfo.service.BlockchainInfoBitconStatusService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    final static int period_min = 5;
    public static void main(String[] args){
        BlockchainInfoBitconStatusService blockchainInfoBitconStatusService = new BlockchainInfoBitconStatusService();

        BitcoinBQBlockchaininfoOutputStream bitcoinBQBlockchaininfoOutputStream = new BitcoinBQBlockchaininfoOutputStream();
        blockchainInfoBitconStatusService.runPeriodic(period_min * 60, state->{
            try {
                bitcoinBQBlockchaininfoOutputStream.write(state);
            }catch(Exception ex){
                log.error(ex.getMessage());
            }
        });
    }
}
