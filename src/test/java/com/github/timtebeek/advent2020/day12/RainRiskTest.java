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
        assertThat(navigate(map).getManhattanDistance()).isEqualByComparingTo(25);
    }

    @Test
    void testPart1() throws Exception {
        var map = parse("part1.txt");
        assertThat(navigate(map).getManhattanDistance()).isEqualByComparingTo(2297);
    }

    private List<String> parse(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.readAllLines(input);
    }

    private static Point navigate(List<String> instructions) {
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

    int getManhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}

enum Direction {
    N, E, S, W;

    Direction rotate(int value) {
        return Stream.of(Direction.values())
                .filter(d -> d.ordinal() == (value / 90 + 4 + ordinal()) % 4)
                .findFirst()
                .orElseThrow();
    }
}
