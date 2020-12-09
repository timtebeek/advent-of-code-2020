package com.github.timtebeek.advent2020.day09;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class EncodingTest {

    @Test
    void testSample1() throws Exception {
        List<Long> numbers = parse("sample1.txt");
        assertThat(findFirstInvalidNumber(numbers, 5)).isEqualByComparingTo(127L);
    }

    @Test
    void testPart1() throws Exception {
        List<Long> numbers = parse("part1.txt");
        assertThat(findFirstInvalidNumber(numbers, 25)).isEqualByComparingTo(466456641L);
    }

    @Test
    void testSample2() throws Exception {
        List<Long> numbers = parse("sample1.txt");
        Long firstInvalidNumber = findFirstInvalidNumber(numbers, 5);
        assertThat(findContiguousSetMinMaxSum(numbers, firstInvalidNumber)).isEqualByComparingTo(62L);
    }

    @Test
    void testPart2() throws Exception {
        List<Long> numbers = parse("part1.txt");
        Long firstInvalidNumber = findFirstInvalidNumber(numbers, 25);
        assertThat(findContiguousSetMinMaxSum(numbers, firstInvalidNumber)).isEqualByComparingTo(55732936L);
    }

    private List<Long> parse(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.readAllLines(input).stream().map(Long::valueOf).collect(toList());
    }

    private static Long findFirstInvalidNumber(List<Long> numbers, int preambleLength) {
        int index = preambleLength;
        while (true) {
            Long valueAtIndex = numbers.get(index);
            List<Long> sums = generateSums(numbers.subList(index - preambleLength, index), preambleLength);
            if (!sums.contains(valueAtIndex)) {
                return valueAtIndex;
            }
            index++;
        }
    }

    private static List<Long> generateSums(List<Long> numbers, int preambleLength) {
        List<Long> sums = new ArrayList<>(preambleLength);
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                sums.add(numbers.get(i) + numbers.get(j));
            }
        }
        return sums;
    }

    private static long findContiguousSetMinMaxSum(List<Long> numbers, Long firstInvalidNumber) {
        long[] numberArray = numbers.stream().mapToLong(Long::longValue).toArray();
        for (int skip = 0; skip < numberArray.length; skip++) {
            for (int limit = 1; limit < numberArray.length - skip; limit++) {
                long[] array = LongStream.of(numberArray).skip(skip).limit(limit).toArray();
                long sum = LongStream.of(array).sum();
                if (firstInvalidNumber.longValue() == sum) {
                    return LongStream.of(array).min().getAsLong() + LongStream.of(array).max().getAsLong();
                } else if (firstInvalidNumber.longValue() < sum) {
                    continue;
                }
            }
        }
        throw new IllegalStateException("Not found");
    }

}
