package io.beam.exp.blockchaininfo.service;
import io.beam.exp.blockchaininfo.model.BitcoinState;
import io.beam.exp.blockchaininfo.outputstream.bigquery.BitcoinBQBlockchaininfoOutputStream;
import io.beam.exp.blockchaininfo.service.BlockchainInfoBitconStatusService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
@Slf4j
@Tag("integration")
class BlockchainInfoBitconStatusServiceTest {

    @Test
    @Disabled
    void get() {
        BlockchainInfoBitconStatusService blockchainInfoBitconStatusService = new BlockchainInfoBitconStatusService();

        BitcoinState bitcoinState = blockchainInfoBitconStatusService.get();
        log.debug(bitcoinState.toString());
    }

    @Test
    @Disabled
    void runPeriodic() {
        BlockchainInfoBitconStatusService blockchainInfoBitconStatusService = new BlockchainInfoBitconStatusService();

        BitcoinBQBlockchaininfoOutputStream bitcoinBQBlockchaininfoOutputStream = new BitcoinBQBlockchaininfoOutputStream();
        blockchainInfoBitconStatusService.runPeriodic(5, state->{
            log.debug(state.toString());
            try {
                bitcoinBQBlockchaininfoOutputStream.write(state);
            }catch(Exception ex){
                log.error(ex.getMessage());
            }
        });
    }
}