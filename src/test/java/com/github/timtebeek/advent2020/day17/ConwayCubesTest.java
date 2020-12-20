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
        Set<Coordinate> map = parse(SAMPLE1, THREE);
        Set<Coordinate> endState = cycle(map, 6, THREE);
        assertThat(endState).hasSize(112);
    }

    @Test
    void testPart1() {
        Set<Coordinate> map = parse(PART1, THREE);
        Set<Coordinate> endState = cycle(map, 6, THREE);
        assertThat(endState).hasSize(284);
    }

    @Test
    void testSample2() {
        Set<Coordinate> map = parse(SAMPLE1, FOUR);
        Set<Coordinate> endState = cycle(map, 6, FOUR);
        assertThat(endState).hasSize(848);
    }

    @Test
    void testPart2() {
        Set<Coordinate> map = parse(PART1, FOUR);
        Set<Coordinate> endState = cycle(map, 6, FOUR);
        assertThat(endState).hasSize(2240);
    }

    private static Set<Coordinate> parse(String slice, Dimensions dimensions) {
        Set<Coordinate> map = new HashSet<>();
        String[] split = slice.split("\n");
        for (int y = 0; y < split.length; y++) {
            for (int x = 0; x < split[0].length(); x++) {
                if (split[y].charAt(x) == '#') {
                    map.add(new Coordinate(x, y, dimensions));
                }
            }
        }
//        Coordinate.print(0, map);
        return map;
    }

    private static Set<Coordinate> cycle(Set<Coordinate> initialState, int cycles,
            Dimensions dimensions) {
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
    final Dimensions dimensions;

    public Coordinate(int x, int y, Dimensions dimensions) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.w = 0;
        this.dimensions = dimensions;
    }

    private int countActiveNeighbours(Set<Coordinate> state) {
        int activeNeighbours = 0;
        for (int otherX = x - 1; otherX <= x + 1; otherX++) {
            for (int otherY = y - 1; otherY <= y + 1; otherY++) {
                for (int otherZ = z - 1; otherZ <= z + 1; otherZ++) {
                    if (dimensions == THREE) {
                        Coordinate f = new Coordinate(otherX, otherY, otherZ, 0, dimensions);
                        if (!this.equals(f) && state.contains(f)) {
                            activeNeighbours++;
                        }
                    } else {
                        for (int otherW = w - 1; otherW <= w + 1; otherW++) {
                            Coordinate f = new Coordinate(otherX, otherY, otherZ, otherW, dimensions);
                            if (!this.equals(f) && state.contains(f)) {
                                activeNeighbours++;
                            }
                        }
                    }
                }
            }
        }
        return activeNeighbours;
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
                        Coordinate coordinate = new Coordinate(x, y, z, 0, dimensions);
                        if (coordinateActiveNextCycle(coordinate, currentState)) {
                            nextState.add(coordinate);
                        }
                    } else {
                        for (int w = min(ws) - 1; w <= max(ws) + 1; w++) {
                            Coordinate coordinate = new Coordinate(x, y, z, w, dimensions);
                            if (coordinateActiveNextCycle(coordinate, currentState)) {
                                nextState.add(coordinate);
                            }
                        }
                    }
                }
            }
        }
        return nextState;
    }

    static boolean coordinateActiveNextCycle(Coordinate coordinate, Set<Coordinate> state) {
        boolean currentlyActive = state.contains(coordinate);
        int activeNeighbours = coordinate.countActiveNeighbours(state);
        return currentlyActive && activeNeighbours == 2 || activeNeighbours == 3;
    }
}
