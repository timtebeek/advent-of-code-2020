package com.github.timtebeek.advent2020.day15;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class RecitationTest {

    @ParameterizedTest
    @CsvSource(delimiterString = ", the 2020th number spoken is ", value = {
            "1,3,2, the 2020th number spoken is 1",
            "2,1,3, the 2020th number spoken is 10",
            "1,2,3, the 2020th number spoken is 27",
            "2,3,1, the 2020th number spoken is 78",
            "0,3,6, the 2020th number spoken is 436",
            "3,2,1, the 2020th number spoken is 438",
            "3,1,2, the 2020th number spoken is 1836",
            "7,12,1,0,16,2, the 2020th number spoken is 410"
    })
    void testPart1(String input, int expected) {
        var numbers = parseInput(input);
        assertThat(findRecitation(2020, numbers)).isEqualByComparingTo(expected);
    }

    private static List<Integer> parseInput(String notes) {
        return Stream.of(notes.split(","))
                .map(Integer::valueOf)
                .collect(toList());
    }

    private static int findRecitation(int limit, List<Integer> numbers) {
        int lastSpokenNumberIndex = numbers.size() - 1;
        while (lastSpokenNumberIndex < limit) {
            Integer lastSpokenNumber = numbers.get(lastSpokenNumberIndex);
            int previousIndexOf = numbers.subList(0, lastSpokenNumberIndex).lastIndexOf(lastSpokenNumber);
            numbers.add(previousIndexOf == -1 ? 0 : lastSpokenNumberIndex - previousIndexOf);
            lastSpokenNumberIndex++;
        }
        return numbers.get(lastSpokenNumberIndex - 1);
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ", the 30000000th number spoken is ", value = {
            "0,3,6, the 30000000th number spoken is 175594",
            "1,3,2, the 30000000th number spoken is 2578",
            "2,1,3, the 30000000th number spoken is 3544142",
            "1,2,3, the 30000000th number spoken is 261214",
            "2,3,1, the 30000000th number spoken is 6895259",
            "3,2,1, the 30000000th number spoken is 18",
            "3,1,2, the 30000000th number spoken is 362",
            "7,12,1,0,16,2, the 30000000th number spoken is -1"
    })
    @Disabled
    void testPart2(String input, int expected) {
        var numbers = parseInput(input);
        assertThat(findRecitation(30000000, numbers)).isEqualByComparingTo(expected);
    }
}
