package com.manageyourbrain.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Memory getMemorySample1() {
        return new Memory().id(1L).topic("topic1").key("key1").comment("comment1");
    }

    public static Memory getMemorySample2() {
        return new Memory().id(2L).topic("topic2").key("key2").comment("comment2");
    }

    public static Memory getMemoryRandomSampleGenerator() {
        return new Memory()
            .id(longCount.incrementAndGet())
            .topic(UUID.randomUUID().toString())
            .key(UUID.randomUUID().toString())
            .comment(UUID.randomUUID().toString());
    }
}
