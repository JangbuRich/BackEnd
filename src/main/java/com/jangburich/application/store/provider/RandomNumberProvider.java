package com.jangburich.application.store.provider;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RandomNumberProvider {
    private final Random random;

    public String createFourDigitNumber() {
        StringBuilder number = new StringBuilder();

        int firstDigit = random.nextInt(10);
        number.append(firstDigit);

        int secondDigit = random.nextInt(10);
        number.append(secondDigit);

        int thirdDigit;
        if(firstDigit == secondDigit) {
            thirdDigit = generateDifferentDigit(firstDigit);
        } else {
            thirdDigit = random.nextInt(10);
        }
        number.append(thirdDigit);

        int fourthDigit;
        if (secondDigit == thirdDigit) {
            fourthDigit = generateDifferentDigit(secondDigit);
        } else {
            fourthDigit = random.nextInt(10);
        }
        number.append(fourthDigit);

        return number.toString();
    }

    public String createEightDigitNumber() {
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            number.append(random.nextInt(10));
        }

        return number.toString();
    }

    private int generateDifferentDigit(int excludeDigit) {
        List<Integer> validDigits = IntStream.range(0, 10)
            .filter(digit -> digit != excludeDigit)
            .boxed()
            .toList();

        return validDigits.get(random.nextInt(validDigits.size()));
    }

}
