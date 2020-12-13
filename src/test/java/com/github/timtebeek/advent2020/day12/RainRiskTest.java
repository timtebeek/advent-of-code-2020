package com.github.timtebeek.advent2020.day12;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RainRiskTest {

    @Test
    void testSample1() throws Exception {
        var map = parse("sample1.txt");
        assertThat(navigatePart1(map).getManhattanDistance()).isEqualByComparingTo(25);
    }

    @Test
    void testPart1() throws Exception {
        var map = parse("part1.txt");
        assertThat(navigatePart1(map).getManhattanDistance()).isEqualByComparingTo(2297);
    }

    private List<String> parse(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.readAllLines(input);
    }

    private static Point navigatePart1(List<String> instructions) {
        Point point = new Point(0, 0);
        Direction direction = Direction.E;
        for (String instruction : instructions) {
            Character action = instruction.charAt(0);
            Integer value = Integer.valueOf(instruction.substring(1));
            switch (action) {
            case 'N', 'E', 'S', 'W' -> point.move(Direction.valueOf("" + action), value);
            case 'F' -> point.move(direction, value);
            case 'L' -> direction = direction.rotate(0 - value);
            case 'R' -> direction = direction.rotate(value);
            }
        }
        return point;
    }

    @Test
    void testSample2() throws Exception {
        var map = parse("sample1.txt");
        assertThat(navigatePart2(map).getManhattanDistance()).isEqualByComparingTo(286);
    }

    @Test
    void testPart2() throws Exception {
        var map = parse("part1.txt");
        assertThat(navigatePart2(map).getManhattanDistance()).isEqualByComparingTo(89984);
    }

    private static Point navigatePart2(List<String> instructions) {
        Point position = new Point(0, 0);
        Point waypoint = new Point(10, -1);
        for (String instruction : instructions) {
            Character action = instruction.charAt(0);
            Integer value = Integer.valueOf(instruction.substring(1));
            switch (action) {
            case 'N', 'E', 'S', 'W' -> waypoint.move(Direction.valueOf("" + action), value);
            case 'F' -> {
                for (int i = 0; i < value; i++) {
                    position.x += waypoint.x;
                    position.y += waypoint.y;
                }
            }
            case 'L' -> waypoint.rotate(0 - value);
            case 'R' -> waypoint.rotate(value);
            }
        }
        return position;
    }

}

@Data
@AllArgsConstructor
class Point {
    int x, y;

    void move(Direction direction, int value) {
        switch (direction) {
        case N -> y -= value;
        case E -> x += value;
        case S -> y += value;
        case W -> x -= value;
        }
    }

    void rotate(int degrees) {
        int turns = (degrees / 90 + 4) % 4;
        for (int i = 0; i < turns; i++) {
            int newY = x;
            x = 0 - y;
            y = newY;
        }
    }

    int getManhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}

enum Direction {
    N, E, S, W;

    Direction rotate(int degrees) {
        return Stream.of(Direction.values())
                .filter(d -> d.ordinal() == (degrees / 90 + 4 + ordinal()) % 4)
                .findFirst()
                .orElseThrow();
    }
}
