package com.github.timtebeek.advent2020.day04;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

class PassportTest {

    @Test
    void testSample1() throws Exception {
        List<Map<String, String>> passports = parse("sample1.txt");
        passports.removeIf(Predicate.not(PassportTest::keysPresent));
        assertThat(passports).hasSize(2);
    }

    @Test
    void testPart1() throws Exception {
        List<Map<String, String>> passports = parse("part1.txt");
        passports.removeIf(Predicate.not(PassportTest::keysPresent));
        assertThat(passports).hasSize(190);
    }

    @Test
    void testSample2() throws Exception {
        List<Map<String, String>> passports = parse("sample2.txt");
        passports.removeIf(Predicate.not(PassportTest::keysPresent));
        passports.removeIf(Predicate.not(PassportTest::valuesValid));
        assertThat(passports).hasSize(4);
    }

    @Test
    void testPart2() throws Exception {
        List<Map<String, String>> passports = parse("part1.txt");
        passports.removeIf(Predicate.not(PassportTest::keysPresent));
        passports.removeIf(Predicate.not(PassportTest::valuesValid));
        assertThat(passports).hasSize(121);
    }

    private List<Map<String, String>> parse(String filename) throws Exception {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        String batch = Files.readString(input);
        return Stream.of(batch.split("\n\n"))
                .map(str -> str.split("\\s+"))
                .map(Stream::of)
                .map(kvs -> kvs.map(kv -> kv.split(":"))
                        .collect(toMap(kv -> kv[0], kv -> kv[1])))
                .collect(toList());
    }

    private static boolean keysPresent(Map<String, String> passport) {
        var keySet = passport.keySet();
        return keySet.size() == 8 || keySet.size() == 7 && !keySet.contains("cid");
    }

    private static final Pattern heightPattern = Pattern.compile("(\\d+)(in|cm)");
    private static final Set<String> eyeColors = Set.of("amb blu brn gry grn hzl oth".split(" "));

    private static boolean valuesValid(Map<String, String> passport) {
        String hgt = passport.get("hgt");
        var matcher = heightPattern.matcher(hgt);
        if (!matcher.matches()) {
            return false;
        }
        int height = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);
        int byr = Integer.parseInt(passport.get("byr"));
        int iyr = Integer.parseInt(passport.get("iyr"));
        int eyr = Integer.parseInt(passport.get("eyr"));
        return (unit.equals("in") && 59 <= height && height <= 76
                || unit.equals("cm") && 150 <= height && height <= 193)
                && 1920 <= byr && byr <= 2002
                && 2010 <= iyr && iyr <= 2020
                && 2020 <= eyr && eyr <= 2030
                && passport.get("hcl").matches("#[0-9a-f]{6}")
                && eyeColors.contains(passport.get("ecl"))
                && passport.get("pid").matches("\\d{9}");
    }

}
