package com.manageyourbrain.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MnemonicTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Mnemonic getMnemonicSample1() {
        return new Mnemonic().id(1L).name("name1");
    }

    public static Mnemonic getMnemonicSample2() {
        return new Mnemonic().id(2L).name("name2");
    }

    public static Mnemonic getMnemonicRandomSampleGenerator() {
        return new Mnemonic().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
