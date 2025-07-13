package com.jangburich.domain.store.service.provider;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jangburich.application.store.provider.RandomNumberProvider;

@ActiveProfiles("local")
@SpringBootTest
class RandomNumberProviderTest {

    @Autowired
    private RandomNumberProvider randomNumberProvider;

    @Test
    @DisplayName("4자리 숫자를 랜덤으로 생성한다.")
    void createFourDigitNumber() {
        // given & when
        String fourDigitNumber = randomNumberProvider.createFourDigitNumber();

        // then
        Assertions.assertThat(fourDigitNumber).isNotNull();
        Assertions.assertThat(fourDigitNumber.length()).isEqualTo(4);
    }

    @Test
    @DisplayName("8자리 숫자를 랜덤으로 생성한다.")
    void createEightDigitNumber() {
        // given & when
        String eightDigitNumber = randomNumberProvider.createEightDigitNumber();

        // then
        Assertions.assertThat(eightDigitNumber).isNotNull();
        Assertions.assertThat(eightDigitNumber.length()).isEqualTo(8);
    }

}