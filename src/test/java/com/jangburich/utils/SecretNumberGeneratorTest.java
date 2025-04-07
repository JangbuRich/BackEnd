package com.jangburich.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecretNumberGeneratorTest {

    @Test
    @DisplayName("8글자의 랜덤한 값을 리턴한다.")
    void generateSecretNumber_test1() {
        String result = SecretNumberGenerator.generateSecretNumber();

        assertThat(result.length()).isEqualTo(8);
    }
}