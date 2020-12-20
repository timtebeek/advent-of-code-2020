package com.github.timtebeek.advent2020.day17;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ConwayCubesTest {

    @Test
    void testSample1() {
        Map<Coordinate, Boolean> map = parse("""
                .#.
                ..#
                ###""");
        Map<Coordinate, Boolean> endState = cycle(map, 6);
        assertThat(endState).hasSize(112);
    }

    private static Map<Coordinate, Boolean> parse(String slice) {
        Map<Coordinate, Boolean> map = new HashMap<Coordinate, Boolean>();
        String[] split = slice.split("\n");
        for (int y = 0; y < split.length; y++) {
            for (int x = 0; x < split[0].length(); x++) {
                if (split[y].charAt(x) == '#') {
                    map.put(new Coordinate(x, y, 0), true);
                }
            }
        }
        System.out.println(map);
        print(0, map);
        return map;
    }

    private static void print(int cycle, Map<Coordinate, Boolean> map) {
        List<Integer> xs = map.keySet().stream().map(Coordinate::getX).collect(toList());
        List<Integer> ys = map.keySet().stream().map(Coordinate::getY).collect(toList());
        List<Integer> zs = map.keySet().stream().map(Coordinate::getZ).collect(toList());
        System.out.println("\nCycle " + cycle);
        for (int z = min(zs); z <= max(zs); z++) {
            System.out.println("z=" + z);
            for (int y = min(ys); y <= max(ys); y++) {
                for (int x = min(xs); x <= max(xs); x++) {
                    Coordinate coordinate = new Coordinate(x, y, z);
                    System.out.print(map.getOrDefault(coordinate, false) ? '#' : '.');
                }
                System.out.println();
            }
        }
    }

    private static Map<Coordinate, Boolean> cycle(Map<Coordinate, Boolean> initialState, int cycles) {
        Map<Coordinate, Boolean> currentState = new HashMap<>(initialState);
        for (int cycle = 0; cycle < cycles; cycle++) {
            currentState = cycleOnce(currentState);
            print(cycle, currentState);
        }
        return currentState;
    }

    private static Map<Coordinate, Boolean> cycleOnce(Map<Coordinate, Boolean> currentState) {
        List<Integer> xs = currentState.keySet().stream().map(Coordinate::getX).collect(toList());
        List<Integer> ys = currentState.keySet().stream().map(Coordinate::getY).collect(toList());
        List<Integer> zs = currentState.keySet().stream().map(Coordinate::getZ).collect(toList());
        Map<Coordinate, Boolean> nextState = new HashMap<>();
        for (int x = min(xs) - 1; x <= max(xs) + 1; x++) {
            for (int y = min(ys) - 1; y <= max(ys) + 1; y++) {
                for (int z = min(zs) - 1; z <= max(zs) + 1; z++) {
                    Coordinate coordinate = new Coordinate(x, y, z);
                    if (coordinateActiveNextCycle(coordinate, currentState)) {
                        nextState.put(coordinate, true);
                    }
                }
            }
        }
        return nextState;
    }

    private static boolean coordinateActiveNextCycle(Coordinate coordinate, Map<Coordinate, Boolean> state) {
        if (coordinate.equals(new Coordinate(2, 1, 0))) {
            System.out.println(coordinate);
        }

        boolean currentlyActive = state.getOrDefault(state, false);
        int activeNeighbours = countActiveNeighbours(coordinate, state);
        if (currentlyActive) {
            return activeNeighbours == 2 || activeNeighbours == 3;
        }
        return activeNeighbours == 3;
    }

    private static int countActiveNeighbours(Coordinate c, Map<Coordinate, Boolean> state) {
        int activeNeighbours = 0;
        for (int x = c.x - 1; x <= c.x + 1; x++) {
            for (int y = c.y - 1; y <= c.y + 1; y++) {
                for (int z = c.z - 1; z <= c.z + 1; z++) {
                    Coordinate f = new Coordinate(x, y, z);
                    if (!c.equals(f) && state.getOrDefault(f, false)) {
                        activeNeighbours++;
                    }
                }
            }
        }
        return activeNeighbours;
    }

}

@Data
class Coordinate {
    final int x, y, z;
}
