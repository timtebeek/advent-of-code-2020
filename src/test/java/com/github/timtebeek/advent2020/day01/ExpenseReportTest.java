package com.github.timtebeek.advent2020.day01;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ExpenseReportTest {

    @Test
    void testSample() throws Exception {
        List<Integer> numbers = parse("sample1.txt");
        List<Integer> components = findSummedTwo(numbers, 2020);
        assertThat(components.stream()
                .reduce((a, b) -> a * b))
                        .hasValue(514579);
    }

    @Test
    void testPart1() throws Exception {
        List<Integer> numbers = parse("part1.txt");
        List<Integer> components = findSummedTwo(numbers, 2020);
        assertThat(components.stream()
                .reduce((a, b) -> a * b))
                        .hasValue(1003971);
    }

    @Test
    void testPart2() throws Exception {
        List<Integer> numbers = parse("part1.txt");
        List<Integer> components = findSummedThree(numbers, 2020);
        assertThat(components.stream()
                .reduce((a, b) -> a * b))
                        .hasValue(84035952);
    }

    private List<Integer> parse(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.lines(input)
                .map(Integer::valueOf)
                .collect(toList());
    }

    private static List<Integer> findSummedTwo(List<Integer> numbers, int sum) {
        for (Integer a : numbers) {
            for (Integer b : numbers) {
                if (a + b == sum) {
                    return List.of(a, b);
                }
            }
        }
        return Collections.emptyList();
    }

    private static List<Integer> findSummedThree(List<Integer> numbers, int sum) {
        for (Integer a : numbers) {
            for (Integer b : numbers) {
                for (Integer c : numbers) {
                    if (a + b + c == sum) {
                        return List.of(a, b, c);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

}
