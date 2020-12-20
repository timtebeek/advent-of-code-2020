package com.github.timtebeek.advent2020.day20;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.assertj.core.api.Assertions.assertThat;

class JurassicJigsaw {

    @Test
    void testSample1() throws Exception {
        Map<Long, char[][]> tiles = parse("sample1.txt");
        assertThat(findCornersMultiplied(tiles)).isEqualByComparingTo(20899048083289L);
    }

    @Test
    void testPart1() throws Exception {
        Map<Long, char[][]> tiles = parse("part1.txt");
        assertThat(findCornersMultiplied(tiles)).isEqualByComparingTo(19955159604613L);
    }

    private Map<Long, char[][]> parse(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        List<String> readAllLines = Files.readAllLines(input);
        var map = new HashMap<Long, char[][]>();

        Long tile = null;
        int row = -1;
        char[][] image = null;
        for (String line : readAllLines) {
            if (line.startsWith("Tile ")) {
                tile = Long.parseLong(line.substring(5, line.length() - 1));
                row = 0;
                image = new char[line.length()][line.length()];
                map.put(tile, image);
            } else if (!line.isBlank()) {
                char[] charArray = line.toCharArray();
                for (int x = 0; x < charArray.length; x++) {
                    char c = charArray[x];
                    image[x][row] = c;
                }
                row++;
            }
        }
        return map;
    }

    private long findCornersMultiplied(Map<Long, char[][]> tiles) {
        return tiles.entrySet().stream()
                .filter(entry -> countAlignedTiles(entry.getValue(), tiles.values()) == 3) // Includes matching itself
                .map(Entry::getKey)
                .peek(System.out::println)
                .reduce((a, b) -> a * b)
                .get();
    }

    private long countAlignedTiles(char[][] tileA, Collection<char[][]> tiles) {
        long alignedTiles = 0;
        for (char[][] tileB : tiles) {
            if (anySidesMatch(tileA, tileB)
                    || anySidesMatch(tileA, rotate(tileB))
                    || anySidesMatch(tileA, rotate(rotate(tileB)))
                    || anySidesMatch(tileA, rotate(rotate(rotate(tileB))))
                    || anySidesMatch(tileA, flip(tileB))
                    || anySidesMatch(tileA, rotate(flip(tileB)))
                    || anySidesMatch(tileA, rotate(rotate(flip(tileB))))
                    || anySidesMatch(tileA, rotate(rotate(rotate(flip(tileB)))))) {
                alignedTiles++;
            }
        }
        System.out.println(alignedTiles);
        return alignedTiles;
    }

    private static char[][] rotate(char[][] tile) {
        final int m = tile.length;
        final int n = tile[0].length;
        char[][] rotated = new char[n][m];
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                rotated[c][m - 1 - r] = tile[r][c];
            }
        }
        return rotated;
    }

    private static char[][] flip(char[][] tile) {
        char[][] flip = new char[tile.length][tile[0].length];
        for (int y = 0; y < tile.length; y++) {
            for (int x = 0; x < flip.length; x++) {
                flip[x][y] = tile[y][x];
            }
        }
        return flip;
    }

    private static boolean anySidesMatch(char[][] tileA, char[][] tileB) {
        return matchLeftRight(tileA, tileB)
                || matchLeftRight(tileB, tileA)
                || matchTopBottom(tileA, tileB)
                || matchTopBottom(tileB, tileA);
    }

    private static boolean matchTopBottom(char[][] top, char[][] bottom) {
        for (int x = 0; x < top[0].length; x++) {
            if (top[x][top.length - 1] != bottom[x][0]) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchLeftRight(char[][] left, char[][] right) {
        for (int y = 0; y < left.length; y++) {
            if (left[left.length - 1][y] != right[0][y]) {
                return false;
            }
        }
        return true;
    }

}
