package com.challenge.simplepicpay.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static Long getRandomLong() {

        return ThreadLocalRandom.current().nextLong();
    }
}
