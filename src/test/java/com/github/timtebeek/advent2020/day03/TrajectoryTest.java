package com.github.timtebeek.advent2020.day03;

import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class TrajectoryTest {

    @Test
    void testSample1() throws Exception {
        Topology topology = parse("sample1.txt");
        assertThat(topology.countTrees(new Point(0, 0), 3, 1))
                .isEqualByComparingTo(7);
    }

    @Test
    void testPart1() throws Exception {
        Topology topology = parse("part1.txt");
        assertThat(topology.countTrees(new Point(0, 0), 3, 1))
                .isEqualByComparingTo(193);
    }

    @Test
    void testSample2() throws Exception {
        Topology topology = parse("sample1.txt");
        assertThat(IntStream.of(
                topology.countTrees(new Point(0, 0), 1, 1),
                topology.countTrees(new Point(0, 0), 3, 1),
                topology.countTrees(new Point(0, 0), 5, 1),
                topology.countTrees(new Point(0, 0), 7, 1),
                topology.countTrees(new Point(0, 0), 1, 2))
                .reduce((a, b) -> a * b))
                        .hasValue(336);
    }

    @Test
    void testPart2() throws Exception {
        Topology topology = parse("part1.txt");
        assertThat(IntStream.of(
                topology.countTrees(new Point(0, 0), 1, 1),
                topology.countTrees(new Point(0, 0), 3, 1),
                topology.countTrees(new Point(0, 0), 5, 1),
                topology.countTrees(new Point(0, 0), 7, 1),
                topology.countTrees(new Point(0, 0), 1, 2))
                .reduce((a, b) -> a * b))
                        .hasValue(336);
    }

    public Topology parse(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return new Topology(Files.readAllLines(input));
    }

}

@ToString
class Topology {

    private final int rows, columns;
    private final Map<Point, Feature> points = new HashMap<>();

    Topology(List<String> lines) {
        rows = lines.size();
        columns = lines.get(0).length();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int column = 0; column < charArray.length; column++) {
                char c = charArray[column];
                Point point = new Point(row, column);
                points.put(point, Feature.parse(c));
            }
        }
    }

    public int countTrees(Point point, int stepRight, int stepDown) {
        if (rows < point.row) {
            return 0;
        }
        Feature feature = points.get(point);
        Point moved = new Point(
                point.row + stepDown,
                (point.column + stepRight) % columns);
        return (feature == Feature.TREE ? 1 : 0)
                + countTrees(moved, stepRight, stepDown);
    }

}

enum Feature {
    OPEN, TREE;

    static Feature parse(char c) {
        return c == '.' ? OPEN : TREE;
    }

}

@Data
class Point {
    final int row, column;
}