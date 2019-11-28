package io.beam.exp.blockchaininfo.service;

import io.beam.exp.blockchaininfo.model.BitcoinState;
import java.util.function.Supplier;

public interface BitcoinStatusInterface extends Supplier<BitcoinState> {
}
