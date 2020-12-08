package com.github.timtebeek.advent2020.day08;

import lombok.Value;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BootCodeTest {

    @Test
    void testSample1() throws URISyntaxException, IOException {
        List<String> bootcode = parseBootCode("sample1.txt");
        Halt halt = accumulatorValueAtStartOfLoopOrTermination(bootcode);
        assertThat(halt.getState()).isEqualByComparingTo(State.LOOP);
        assertThat(halt.getAccumulator()).isEqualByComparingTo(5);
    }

    @Test
    void testPart1() throws URISyntaxException, IOException {
        List<String> bootcode = parseBootCode("part1.txt");
        Halt halt = accumulatorValueAtStartOfLoopOrTermination(bootcode);
        assertThat(halt.getState()).isEqualByComparingTo(State.LOOP);
        assertThat(halt.getAccumulator()).isEqualByComparingTo(1331);
    }

    @Test
    void testSample2() throws URISyntaxException, IOException {
        List<String> bootcode = parseBootCode("sample1.txt");
        assertThat(patchAndDetermineAccumulatorValueAtTermination(bootcode)).isEqualByComparingTo(8);
    }

    @Test
    void testPart2() throws URISyntaxException, IOException {
        List<String> bootcode = parseBootCode("part1.txt");
        assertThat(patchAndDetermineAccumulatorValueAtTermination(bootcode)).isEqualByComparingTo(1121);
    }

    private List<String> parseBootCode(String filename) throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.readAllLines(input);
    }

    private static Halt accumulatorValueAtStartOfLoopOrTermination(List<String> bootcode) {
        int accumulator = 0;
        int next = 0;
        List<Integer> taken = new ArrayList<>();
        while (true) {
            if (taken.contains(next)) {
                return new Halt(State.LOOP, accumulator);
            }
            if (bootcode.size() == next) {
                return new Halt(State.TERM, accumulator);
            }

            taken.add(next);
            String instruction = bootcode.get(next);
            int value = Integer.parseInt(instruction.substring(4));
            String op = instruction.substring(0, 3);
            switch (op) {
            case "nop" -> {
                next++;
            }
            case "acc" -> {
                next++;
                accumulator += value;
            }
            case "jmp" -> {
                next += value;
            }
            }
        }
    }

    private static int patchAndDetermineAccumulatorValueAtTermination(List<String> bootcode) {
        for (int i = 0; i < bootcode.size(); i++) {
            String instruction = bootcode.get(i);
            String swapped = swap(instruction);
            if (swapped.equals(instruction)) {
                continue;
            }
            // Replace in place
            bootcode.set(i, swapped);
            Halt accumulatorValueAtStartOfLoop = accumulatorValueAtStartOfLoopOrTermination(bootcode);
            if (accumulatorValueAtStartOfLoop.getState() == State.TERM) {
                return accumulatorValueAtStartOfLoop.getAccumulator();
            }
            // Restore
            bootcode.set(i, instruction);
        }
        throw new IllegalStateException("None terminated");
    }

    private static String swap(String instruction) {
        String op = instruction.substring(0, 3);
        if (op.equals("nop")) {
            return instruction.replace("nop", "jmp");
        } else if (op.equals("jmp")) {
            return instruction.replace("jmp", "nop");
        } else {
            return instruction;
        }
    }
}

@Value
class Halt {
    State state;
    int accumulator;
}

enum State {
    LOOP, TERM
}