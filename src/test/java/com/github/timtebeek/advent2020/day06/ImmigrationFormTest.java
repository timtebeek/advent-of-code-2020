package com.github.timtebeek.advent2020.day06;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ImmigrationFormTest {

    @Test
    void testSample1() throws Exception {
        assertThat(parseImmigrationForms("sample1.txt")
                .map(Stream::of)
                .map(group -> group.flatMapToInt(String::chars))
                .mapToLong(group -> group.distinct().count()).sum())
                        .isEqualByComparingTo(11L);
    }

    @Test
    void testPart1() throws Exception {
        assertThat(parseImmigrationForms("part1.txt")
                .map(Stream::of)
                .map(group -> group.flatMapToInt(String::chars))
                .mapToLong(group -> group.distinct().count()).sum())
                        .isEqualByComparingTo(6382L);
    }

    @Test
    void testSample2() throws Exception {
        assertThat(parseImmigrationForms("sample1.txt")
                .map(List::of)
                .mapToLong(ImmigrationFormTest::countAllYesPerGroup)
                .sum())
                        .isEqualByComparingTo(6L);
    }

    @Test
    void testPart2() throws Exception {
        assertThat(parseImmigrationForms("part1.txt")
                .map(List::of)
                .mapToLong(ImmigrationFormTest::countAllYesPerGroup)
                .sum())
                        .isEqualByComparingTo(3197L);
    }

    private Stream<String[]> parseImmigrationForms(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        String batch = Files.readString(input);
        return Stream.of(batch.split("\n\n"))
                .map(str -> str.split("\n"));
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
