package com.github.timtebeek.advent2020.day05;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class SeatTest {

    @ParameterizedTest
    @CsvSource({
            "FBFBBFFRLR,44,5,357",
            "BFFFBBFRRR,70,7,567",
            "FFFBBBFRRR,14,7,119",
            "BBFFBBFRLL,102,4,820"

    })
    void testSample1(String pattern, int row, int column, int id) throws Exception {
        Seat seat = parsePattern(pattern);
        assertThat(seat).extracting(s -> s.row).isEqualTo(row);
        assertThat(seat).extracting(s -> s.column).isEqualTo(column);
        assertThat(seat).extracting(Seat::id).isEqualTo(id);
    }

    @Test
    void testPart1() throws Exception {
        List<String> patterns = parseFile("part1.txt");
        OptionalInt max = patterns.stream().map(SeatTest::parsePattern).mapToInt(Seat::id).max();
        assertThat(max).hasValue(850);
    }

    @Test
    void testPart2() throws Exception {
        List<String> patterns = parseFile("part1.txt");
        List<Integer> seatids = patterns.stream()
                .map(SeatTest::parsePattern)
                .mapToInt(Seat::id)
                .sorted()
                .boxed()
                .collect(toList());
        List<Integer> possible = IntStream.rangeClosed(Collections.min(seatids), Collections.max(seatids))
                .boxed()
                .collect(toList());
        possible.removeIf(seatids::contains);
        assertThat(possible).containsOnly(599);
    }

    private List<String> parseFile(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.readAllLines(input);
    }

    private static Seat parsePattern(String pattern) {
        return new Seat(
                partition(pattern.substring(0, 7), 128, 'F'),
                partition(pattern.substring(7), 8, 'L'));
    }

    private static int partition(String pattern, int max, int firstHalfChar) {
        int min = 0;
        for (char c : pattern.toCharArray()) {
            int half = (max - min) / 2;
            if (c == firstHalfChar) {
                max -= half;
            } else {
                min += half;
            }
        }
        return min;
    }

}

@Data
class Seat {
    final int row, column;

    int id() {
        return row * 8 + column;
    }
}