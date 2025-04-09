package com.jangburich.utils;

import java.util.UUID;

public class SecretNumberGenerator {
    private SecretNumberGenerator() {}

    public static String generateSecretNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
