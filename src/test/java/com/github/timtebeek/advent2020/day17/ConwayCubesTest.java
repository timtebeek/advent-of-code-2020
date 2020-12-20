package com.github.timtebeek.advent2020.day17;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.github.timtebeek.advent2020.day17.Dimensions.FOUR;
import static com.github.timtebeek.advent2020.day17.Dimensions.THREE;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class ConwayCubesTest {

    private static final String PART1 = """
            ##..#.#.
            ###.#.##
            ..###..#
            .#....##
            .#..####
            #####...
            #######.
            #.##.#.#""";
    private static final String SAMPLE1 = """
            .#.
            ..#
            ###""";

    @Test
    void testSample1() {
        Set<Coordinate> map = parse(SAMPLE1);
        Set<Coordinate> endState = cycle(map, 6, THREE);
        assertThat(endState).hasSize(112);
    }

    @Test
    void testPart1() {
        Set<Coordinate> map = parse(PART1);
        Set<Coordinate> endState = cycle(map, 6, THREE);
        assertThat(endState).hasSize(284);
    }

    @Test
    void testSample2() {
        Set<Coordinate> map = parse(SAMPLE1);
        Set<Coordinate> endState = cycle(map, 6, FOUR);
        assertThat(endState).hasSize(848);
    }

    @Test
    void testPart2() {
        Set<Coordinate> map = parse(PART1);
        Set<Coordinate> endState = cycle(map, 6, FOUR);
        assertThat(endState).hasSize(2240);
    }

    private static Set<Coordinate> parse(String slice) {
        Set<Coordinate> map = new HashSet<>();
        String[] split = slice.split("\n");
        for (int y = 0; y < split.length; y++) {
            for (int x = 0; x < split[0].length(); x++) {
                if (split[y].charAt(x) == '#') {
                    map.add(new Coordinate(x, y));
                }
            }
        }
        return map;
    }

    private static Set<Coordinate> cycle(Set<Coordinate> initialState, int cycles, Dimensions dimensions) {
        Set<Coordinate> currentState = new HashSet<>(initialState);
        for (int cycle = 0; cycle < cycles; cycle++) {
            currentState = Coordinate.cycleOnce(currentState, dimensions);
        }
        return currentState;
    }
}

enum Dimensions {
    THREE, FOUR
}

@Value
@AllArgsConstructor
class Coordinate {

    final int x, y, z, w;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.w = 0;
    }

    public static Set<Coordinate> cycleOnce(Set<Coordinate> currentState, Dimensions dimensions) {
        var xs = currentState.stream().map(Coordinate::getX).collect(toSet());
        var ys = currentState.stream().map(Coordinate::getY).collect(toSet());
        var zs = currentState.stream().map(Coordinate::getZ).collect(toSet());
        var ws = currentState.stream().map(Coordinate::getW).collect(toSet());
        Set<Coordinate> nextState = new HashSet<>();
        for (int z = min(zs) - 1; z <= max(zs) + 1; z++) {
            for (int y = min(ys) - 1; y <= max(ys) + 1; y++) {
                for (int x = min(xs) - 1; x <= max(xs) + 1; x++) {
                    if (dimensions == THREE) {
                        Coordinate coordinate = new Coordinate(x, y, z, 0);
                        if (coordinateActiveNextCycle(coordinate, currentState, dimensions)) {
                            nextState.add(coordinate);
                        }
                    } else {
                        for (int w = min(ws) - 1; w <= max(ws) + 1; w++) {
                            Coordinate coordinate = new Coordinate(x, y, z, w);
                            if (coordinateActiveNextCycle(coordinate, currentState, dimensions)) {
                                nextState.add(coordinate);
                            }
                        }
                    }
                }
            }
        }
        return nextState;
    }

    private static boolean coordinateActiveNextCycle(Coordinate coordinate, Set<Coordinate> state,
            Dimensions dimensions) {
        boolean currentlyActive = state.contains(coordinate);
        int activeNeighbours = countActiveNeighbours(coordinate, state, dimensions);
        return currentlyActive && activeNeighbours == 2 || activeNeighbours == 3;
    }

    private static int countActiveNeighbours(Coordinate c, Set<Coordinate> state, Dimensions dimensions) {
        int activeNeighbours = 0;
        for (int otherX = c.x - 1; otherX <= c.x + 1; otherX++) {
            for (int otherY = c.y - 1; otherY <= c.y + 1; otherY++) {
                for (int otherZ = c.z - 1; otherZ <= c.z + 1; otherZ++) {
                    if (dimensions == THREE) {
                        Coordinate f = new Coordinate(otherX, otherY, otherZ, 0);
                        if (!c.equals(f) && state.contains(f)) {
                            activeNeighbours++;
                        }
                    } else {
                        for (int otherW = c.w - 1; otherW <= c.w + 1; otherW++) {
                            Coordinate f = new Coordinate(otherX, otherY, otherZ, otherW);
                            if (!c.equals(f) && state.contains(f)) {
                                activeNeighbours++;
                            }
                        }
                    }
                }
            }
        }
        return activeNeighbours;
    }
}
