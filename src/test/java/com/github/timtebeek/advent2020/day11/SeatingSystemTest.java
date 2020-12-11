package com.github.timtebeek.advent2020.day11;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SeatingSystemTest {

    @Test
    void testSample1() throws Exception {
        Map<Point, State> map = parse("sample1.txt");
        assertThat(seatsTakenAtEquilibrium(map, SeatingSystemTest::tickAdjacent)).isEqualByComparingTo(37L);
    }

    @Test
    void testPart1() throws Exception {
        Map<Point, State> map = parse("part1.txt");
        assertThat(seatsTakenAtEquilibrium(map, SeatingSystemTest::tickAdjacent)).isEqualByComparingTo(2406L);
    }

    @Test
    void testSample2() throws Exception {
        Map<Point, State> map = parse("sample1.txt");
        assertThat(seatsTakenAtEquilibrium(map, SeatingSystemTest::tickVisible)).isEqualByComparingTo(26L);
    }

    @Test
    void testPart2() throws Exception {
        Map<Point, State> map = parse("part1.txt");
        assertThat(seatsTakenAtEquilibrium(map, SeatingSystemTest::tickVisible)).isEqualByComparingTo(2149L);
    }

    private Map<Point, State> parse(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        List<String> parsed = Files.readAllLines(input);
        Map<Point, State> map = new HashMap<>();
        for (int x = 0; x < parsed.get(0).length(); x++) {
            for (int y = 0; y < parsed.size(); y++) {
                map.put(new Point(x, y), State.of(parsed.get(y).charAt(x)));
            }
        }
        print(map);
        return map;
    }

    private static void print(Map<Point, State> map) {
        Point max = map.keySet().stream().max(Comparator.comparing(Point::getX).thenComparing(Point::getY)).get();
        for (int y = 0; y <= max.y; y++) {
            for (int x = 0; x <= max.x; x++) {
                System.out.print(map.get(new Point(x, y)).chr);
            }
            System.out.println();
        }
    }

    private static long seatsTakenAtEquilibrium(Map<Point, State> map,
            Function<Map<Point, State>, Map<Point, State>> tick) {
        long taken = countSeatsTaken(map);
        while (true) {
            map = tick.apply(map);
            long after = countSeatsTaken(map);
            if (taken == after) {
                return after;
            }
            taken = after;
        }
    }

    private static long countSeatsTaken(Map<Point, State> map) {
        return map.values().stream().filter(s -> s == State.OCCUPIED).count();
    }

    private static Map<Point, State> tickAdjacent(Map<Point, State> map) {
        Map<Point, State> copy = new HashMap<>();
        map.forEach((point, state) -> {
            long seatsTaken = countAdjacentSeatsTaken(map, point.x, point.y);
            if (state == State.EMPTY && seatsTaken == 0) {
                copy.put(point, State.OCCUPIED);
            } else if (state == State.OCCUPIED && 4 <= seatsTaken) {
                copy.put(point, State.EMPTY);
            } else {
                copy.put(point, state);
            }
        });
        return copy;
    }

    private static long countAdjacentSeatsTaken(Map<Point, State> map, int x, int y) {
        return Stream.of(
                new Point(x - 1, y - 1), new Point(x, y - 1), new Point(x + 1, y - 1),
                new Point(x - 1, y), /*                    */ new Point(x + 1, y),
                new Point(x - 1, y + 1), new Point(x, y + 1), new Point(x + 1, y + 1))
                .map(map::get)
                .filter(s -> s == State.OCCUPIED)
                .count();
    }

    private static Map<Point, State> tickVisible(Map<Point, State> map) {
        Map<Point, State> copy = new HashMap<>();
        map.forEach((point, state) -> {
            long seatsTaken = countVisibleSeatsTaken(map, point.x, point.y);
            if (state == State.EMPTY && seatsTaken == 0) {
                copy.put(point, State.OCCUPIED);
            } else if (state == State.OCCUPIED && 5 <= seatsTaken) {
                copy.put(point, State.EMPTY);
            } else {
                copy.put(point, state);
            }
        });
        return copy;
    }

    private static long countVisibleSeatsTaken(Map<Point, State> map, int x, int y) {
        Point point = new Point(x, y);
        return Stream.of(
                look(map, point, -1, -1), look(map, point, 0, -1), look(map, point, +1, -1),
                look(map, point, -1, 0), /*                     */ look(map, point, +1, 0),
                look(map, point, -1, +1), look(map, point, 0, +1), look(map, point, +1, +1))
                .filter(s -> s == State.OCCUPIED)
                .count();
    }

    private static State look(Map<Point, State> map, Point point, int dx, int dy) {
        State state;
        do {
            point = new Point(point.x + dx, point.y + dy);
            state = map.get(point);
            if (state != State.FLOOR) {
                return state;
            }
        } while (state != null);
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