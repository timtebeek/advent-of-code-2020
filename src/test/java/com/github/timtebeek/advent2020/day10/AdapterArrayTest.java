package com.github.timtebeek.advent2020.day10;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class AdapterArrayTest {

    @Test
    void testMultipliedDifferencesSample1() throws Exception {
        List<Integer> numbers = parse("sample1.txt");
        assertThat(findMultipliedDifferences(numbers)).isEqualByComparingTo(7 * 5);
    }

    @Test
    void testMultipliedDifferencesSample2() throws Exception {
        List<Integer> numbers = parse("sample2.txt");
        assertThat(findMultipliedDifferences(numbers)).isEqualByComparingTo(22 * 10);
    }

    @Test
    void testMultipliedDifferencesPart1() throws Exception {
        List<Integer> numbers = parse("part1.txt");
        assertThat(findMultipliedDifferences(numbers)).isEqualByComparingTo(2590);
    }

    @Test
    void testDistinctArrangementsSample1() throws Exception {
        List<Integer> numbers = parse("sample1.txt");
        assertThat(findDistinctArrangements(numbers)).isEqualByComparingTo(8L);
    }

    @Test
    void testDistinctArrangementsSample2() throws Exception {
        List<Integer> numbers = parse("sample2.txt");
        assertThat(findDistinctArrangements(numbers)).isEqualByComparingTo(19208L);
    }

    @Test
    void testDistinctArrangementsPart1() throws Exception {
        List<Integer> numbers = parse("part1.txt");
        assertThat(findDistinctArrangements(numbers)).isEqualByComparingTo(226775649501184L);
    }

    private List<Integer> parse(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        List<Integer> parsed = Files.readAllLines(input).stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(toList());
        // Add outlet and device
        parsed.add(0, 0);
        parsed.add(parsed.stream().mapToInt(Integer::intValue).max().getAsInt() + 3);
        return parsed;
    }

    public static List<Integer> findDifferences(List<Integer> numbers) {
        List<Integer> differences = new ArrayList<>();
        for (int i = 0; i < numbers.size() - 1; i++) {
            differences.add(numbers.get(i + 1) - numbers.get(i));
        }
        return differences;
    }

    private static int findMultipliedDifferences(List<Integer> numbers) {
        List<Integer> differences = findDifferences(numbers);
        Map<Integer, List<Integer>> collect = differences.stream()
                .collect(groupingBy(identity()));
        return collect.get(1).size() * collect.get(3).size();
    }

    private static long findDistinctArrangements(List<Integer> numbers) {
//        return naive(numbers);
        return magic(numbers);
    }

    static long naive(List<Integer> numbers) {
        if (numbers.size() == 1) {
            return 1;
        }
        Integer jolts = numbers.get(0);
        int plusOne = numbers.indexOf(jolts + 1);
        int plusTwo = numbers.indexOf(jolts + 2);
        int plusThree = numbers.indexOf(jolts + 3);
        return (plusOne != -1 ? naive(numbers.subList(plusOne, numbers.size())) : 0)
                + (plusTwo != -1 ? naive(numbers.subList(plusTwo, numbers.size())) : 0)
                + (plusThree != -1 ? naive(numbers.subList(plusThree, numbers.size())) : 0);
    }

    static long magic(List<Integer> numbers) {
        List<Integer> differences = findDifferences(numbers);
        String string = differences.toString();
        return (long) (Math.pow(7, StringUtils.countMatches(string, "1, 1, 1, 1, "))
                * Math.pow(4, StringUtils.countMatches(string.replace("1, 1, 1, 1, ", ""), "1, 1, 1, "))
                * Math.pow(2, StringUtils.countMatches(string.replace("1, 1, 1, 1, ", "").replace("1, 1, 1, ", ""),
                        "1, 1, ")));
    }
}
