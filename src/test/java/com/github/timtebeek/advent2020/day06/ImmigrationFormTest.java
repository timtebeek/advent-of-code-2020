package com.github.timtebeek.advent2020.day06;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ImmigrationFormTest {

    @Test
    void testSample1() throws Exception {
        assertThat(parseImmigrationFormsToStream("sample1.txt")
                .mapToLong(group -> group.distinct().count()).sum())
                        .isEqualByComparingTo(11L);
    }

    @Test
    void testPart1() throws Exception {
        assertThat(parseImmigrationFormsToStream("part1.txt")
                .mapToLong(group -> group.distinct().count()).sum())
                        .isEqualByComparingTo(6382L);
    }

    @Test
    void testSample2() throws Exception {
        List<List<String>> forms = parseImmigrationFormsToList("sample1.txt");
        assertThat(sumAllYesPerGroup(forms)).isEqualByComparingTo(6L);
    }

    @Test
    void testPart2() throws Exception {
        List<List<String>> forms = parseImmigrationFormsToList("part1.txt");
        assertThat(sumAllYesPerGroup(forms)).isEqualByComparingTo(3197L);
    }

    private Stream<IntStream> parseImmigrationFormsToStream(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        String batch = Files.readString(input);
        return Stream.of(batch.split("\n\n"))
                .map(str -> str.split("\n"))
                .map(Stream::of)
                .map(group -> group.flatMapToInt(String::chars));
    }

    private List<List<String>> parseImmigrationFormsToList(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        String batch = Files.readString(input);
        return Stream.of(batch.split("\n\n"))
                .map(str -> str.split("\n"))
                .map(List::of)
                .collect(toList());
    }

    private static long sumAllYesPerGroup(List<List<String>> forms) {
        return forms.stream()
                .mapToLong(ImmigrationFormTest::countAllYesPerGroup)
                .sum();
    }

    private static long countAllYesPerGroup(List<String> group) {
        return group.stream()
                .flatMapToInt(String::chars)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity()))
                .entrySet()
                .stream()
                .filter(kv -> kv.getValue().size() == group.size())
                .count();
    }

}
