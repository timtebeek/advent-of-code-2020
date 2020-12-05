package com.github.timtebeek.advent2020.day05;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Seat seat = parse(128, 8, pattern);
        assertThat(seat).extracting(s -> s.row).isEqualTo(row);
        assertThat(seat).extracting(s -> s.column).isEqualTo(column);
        assertThat(seat).extracting(Seat::id).isEqualTo(id);
    }

    @Test
    void testPart1() throws Exception {
        List<String> patterns = parse("part1.txt");
        OptionalInt max = patterns.stream().map(pattern -> parse(128, 8, pattern)).mapToInt(Seat::id).max();
        assertThat(max).hasValue(850);
    }

    @Test
    void testPart2() throws Exception {
        List<String> patterns = parse("part1.txt");
        List<Integer> seatids = patterns.stream()
                .map(pattern -> parse(128, 8, pattern))
                .mapToInt(Seat::id)
                .sorted()
                .boxed()
                .collect(toList());
        List<Integer> possible = IntStream.rangeClosed(11, 850).boxed().collect(toList());
        possible.removeIf(seatids::contains);
        assertThat(possible).containsOnly(599);
    }

    private List<String> parse(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.readAllLines(input);
    }

    private static Seat parse(int rows, int columns, String pattern) {
        int minRow = 0, maxRow = rows;

        for (int i = 0; i < 7; i++) {
            int half = (maxRow - minRow) / 2;
            if (pattern.charAt(i) == 'F') {
                maxRow -= half;
            } else {
                minRow += half;
            }
        }

        int minCol = 0, maxCol = columns;
        for (int i = 7; i < 10; i++) {
            int half = (maxCol - minCol) / 2;
            if (pattern.charAt(i) == 'L') {
                maxCol -= half;
            } else {
                minCol += half;
            }
        }

        return new Seat(minRow, minCol);
    }

}

@Data
@EqualsAndHashCode
class Seat {
    final int row, column;

    int id() {
        return row * 8 + column;
    }
}