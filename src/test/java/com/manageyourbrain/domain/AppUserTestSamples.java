package com.manageyourbrain.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser().id(1L).name("name1").email("email1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser().id(2L).name("name2").email("email2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).email(UUID.randomUUID().toString());
    }
}
