package com.github.timtebeek.advent2020.day02;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class PasswordPolicyTest {

    @Test
    void testSample1() throws Exception {
        List<Combination> combinations = parse("sample1.txt");
        assertThat(combinations.stream()
                .filter(Combination::isValidSledding)
                .count())
                        .isEqualByComparingTo(2L);
    }

    @Test
    void testPart1() throws Exception {
        List<Combination> combinations = parse("part1.txt");
        assertThat(combinations.stream()
                .filter(Combination::isValidSledding)
                .count())
                        .isEqualByComparingTo(515L);
    }

    @Test
    void testSample2() throws Exception {
        List<Combination> combinations = parse("sample1.txt");
        assertThat(combinations.stream()
                .filter(Combination::isValidToboggan)
                .count())
                        .isEqualByComparingTo(1L);
    }

    @Test
    void testPart2() throws Exception {
        List<Combination> combinations = parse("part1.txt");
        assertThat(combinations.stream()
                .filter(Combination::isValidToboggan)
                .count())
                        .isEqualByComparingTo(711L);
    }

    public List<Combination> parse(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.lines(input)
                .map(Combination::new)
                .collect(toList());
    }

}

@ToString
class Combination {
    private static final Pattern pattern = Pattern.compile("(\\d+)-(\\d+) (.): (.+)");

    Policy policy;
    String password;

    Combination(String line) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(line);
        }
        int min = Integer.parseInt(matcher.group(1));
        int max = Integer.parseInt(matcher.group(2));
        char c = matcher.group(3).charAt(0);
        this.policy = new Policy(min, max, c);
        this.password = matcher.group(4);
    }

    boolean isValidSledding() {
        long count = password.chars()
                .filter(c -> c == policy.c)
                .count();
        return policy.min <= count && count <= policy.max;
    }

    boolean isValidToboggan() {
        return password.charAt(policy.min - 1) == policy.c ^
                password.charAt(policy.max - 1) == policy.c;
    }

}

@AllArgsConstructor
class Policy {
    int min, max;
    char c;
}