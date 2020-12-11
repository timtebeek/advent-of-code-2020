package com.github.timtebeek.advent2020.day11;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SeatingSystemTest {

    @Test
    void testSample1() throws Exception {
        State[][] map = parse("sample1.txt");
        assertThat(seatsTakenAtAdjacentEquilibrium(map)).isEqualByComparingTo(37L);
    }

    @Test
    void testPart1() throws Exception {
        State[][] map = parse("part1.txt");
        assertThat(seatsTakenAtAdjacentEquilibrium(map)).isEqualByComparingTo(2406L);
    }

    @Test
    void testSample2() throws Exception {
        State[][] map = parse("sample1.txt");
        assertThat(seatsTakenAtVisibleEquilibrium(map)).isEqualByComparingTo(26L);
    }

    @Test
    void testPart2() throws Exception {
        State[][] map = parse("part1.txt");
        assertThat(seatsTakenAtVisibleEquilibrium(map)).isEqualByComparingTo(2149L);
    }

    private State[][] parse(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        List<String> parsed = Files.readAllLines(input);
        State[][] map = new State[parsed.get(0).length()][parsed.size()];
        for (int x = 0; x < parsed.get(0).length(); x++) {
            for (int y = 0; y < parsed.size(); y++) {
                map[x][y] = State.of(parsed.get(y).charAt(x));
            }
        }
        print(map);
        return map;
    }

    private static void print(State[][] map) {
        for (int y = 0; y < map[0].length; y++) {
            for (State[] element : map) {
                System.out.print(element[y].chr);
            }
            System.out.println();
        }
    }

    private static long seatsTakenAtAdjacentEquilibrium(State[][] map) {
        long taken = countSeatsTaken(map);
        while (true) {
            map = tickAdjacent(map);
            long after = countSeatsTaken(map);
            if (taken == after) {
                return after;
            }
            taken = after;
        }
    }

    private static long countSeatsTaken(State[][] map) {
        return Stream.of(map).flatMap(Stream::of).filter(s -> s == State.OCCUPIED).count();
    }

    private static State[][] tickAdjacent(State[][] map) {
        State[][] copy = deepCopy(map);
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                long countAdjacentSeatsTaken = countAdjacentSeatsTaken(map, x, y);
                if (map[x][y] == State.EMPTY && countAdjacentSeatsTaken == 0) {
                    copy[x][y] = State.OCCUPIED;
                } else if (map[x][y] == State.OCCUPIED && 4 <= countAdjacentSeatsTaken) {
                    copy[x][y] = State.EMPTY;
                }
            }
        }
        return copy;
    }

    private static long countAdjacentSeatsTaken(State[][] map, int x, int y) {
        int maxY = map[0].length - 1;
        int maxX = map.length - 1;
        return Stream.of(
                new Point(x - 1, y - 1), new Point(x, y - 1), new Point(x + 1, y - 1),
                new Point(x - 1, y), /*                    */ new Point(x + 1, y),
                new Point(x - 1, y + 1), new Point(x, y + 1), new Point(x + 1, y + 1))
                .filter(p -> 0 <= p.x && p.x <= maxX)
                .filter(p -> 0 <= p.y && p.y <= maxY)
                .map(p -> map[p.x][p.y])
                .filter(s -> s == State.OCCUPIED)
                .count();
    }

    private static <T> T[][] deepCopy(T[][] matrix) {
        return Arrays.stream(matrix)
                .map(Object[]::clone)
                .toArray($ -> matrix.clone());
    }

    private static long seatsTakenAtVisibleEquilibrium(State[][] map) {
        long taken = countSeatsTaken(map);
        while (true) {
            map = tickVisible(map);
            long after = countSeatsTaken(map);
            if (taken == after) {
                return after;
            }
            taken = after;
        }
    }

    private static State[][] tickVisible(State[][] map) {
        State[][] copy = deepCopy(map);
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                long countAdjacentSeatsTaken = countVisibleSeatsTaken2(map, x, y);
                if (map[x][y] == State.EMPTY && countAdjacentSeatsTaken == 0) {
                    copy[x][y] = State.OCCUPIED;
                } else if (map[x][y] == State.OCCUPIED && 5 <= countAdjacentSeatsTaken) {
                    copy[x][y] = State.EMPTY;
                }
            }
        }
        return copy;
    }

    private static long countVisibleSeatsTaken2(State[][] map, int x, int y) {
        return Stream.of(
                look(map, x, -1, y, -1), look(map, x, 0, y, -1), look(map, x, +1, y, -1),
                look(map, x, -1, y, 0), /*                    */ look(map, x, +1, y, 0),
                look(map, x, -1, y, +1), look(map, x, 0, y, +1), look(map, x, +1, y, +1))
                .filter(s -> s == State.OCCUPIED)
                .count();
    }

    private static State look(State[][] map, int x, int dx, int y, int dy) {
        int maxY = map[0].length - 1;
        int maxX = map.length - 1;
        x += dx;
        y += dy;
        while (0 <= x && x <= maxX &&
                0 <= y && y <= maxY) {
            State state = map[x][y];
            if (state != State.FLOOR) {
                return state;
            }
            x += dx;
            y += dy;
        }
        // Treat the edges as FLOOR
        return State.FLOOR;
    }
}

@RequiredArgsConstructor
enum State {
    FLOOR('.'), EMPTY('L'), OCCUPIED('#');

    final char chr;

    static State of(char charAt) {
        return Stream.of(State.values())
                .filter(s -> s.chr == charAt)
                .findFirst()
                .orElseThrow();
    }
}

@Data
class Point {
    final int x, y;
}