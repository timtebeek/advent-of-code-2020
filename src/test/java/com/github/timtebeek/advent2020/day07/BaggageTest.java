package com.github.timtebeek.advent2020.day07;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class BaggageTest {

    @Test
    void testSample1() throws Exception {
        Map<String, Map<String, Integer>> rules = parseBaggageRules("sample1.txt");
        assertThat(howManyColorsCanContainAtLeastOneBagOfColor(rules, "shiny gold"))
                .hasSize(4);
    }

    @Test
    void testPart1() throws Exception {
        Map<String, Map<String, Integer>> rules = parseBaggageRules("part1.txt");
        assertThat(howManyColorsCanContainAtLeastOneBagOfColor(rules, "shiny gold"))
                .hasSize(224);
    }

    @Test
    void testSample1Contains() throws Exception {
        Map<String, Map<String, Integer>> rules = parseBaggageRules("sample1.txt");
        assertThat(howManyBagsAreContainedWithinOne(rules, "shiny gold"))
                .isEqualByComparingTo(32);
    }

    @Test
    void testSample2Contains() throws Exception {
        Map<String, Map<String, Integer>> rules = parseBaggageRules("sample2.txt");
        assertThat(howManyBagsAreContainedWithinOne(rules, "shiny gold"))
                .isEqualByComparingTo(126);
    }

    @Test
    void testPart2() throws Exception {
        Map<String, Map<String, Integer>> rules = parseBaggageRules("part1.txt");
        assertThat(howManyBagsAreContainedWithinOne(rules, "shiny gold"))
                .isEqualByComparingTo(1488);
    }

    private Map<String, Map<String, Integer>> parseBaggageRules(String filename)
            throws URISyntaxException, IOException {
        Path input = Paths.get(getClass().getResource(filename).toURI());
        return Files.readAllLines(input).stream()
                .collect(toMap(rule -> rule.split(" bags contain ")[0],
                        rule -> {
                            String[] split = rule.split(" bags contain ");
                            Map<String, Integer> contains = new HashMap<>();
                            if (!"no other bags.".equals(split[1])) {
                                for (String component : split[1].split(" bag[s, .]+")) {
                                    String[] part = component.split(" ", 2);
                                    contains.put(part[1], Integer.valueOf(part[0]));
                                }
                            }
                            return contains;
                        }));
    }

    private static Set<String> howManyColorsCanContainAtLeastOneBagOfColor(Map<String, Map<String, Integer>> rules,
            String color) {
        Set<String> canContainDirectly = rules.entrySet().stream()
                .filter(rule -> rule.getValue().containsKey(color))
                .map(Entry::getKey)
                .collect(toSet());
        Set<String> canContainIndirectly = canContainDirectly.stream()
                .flatMap(sub -> howManyColorsCanContainAtLeastOneBagOfColor(rules, sub).stream())
                .collect(toSet());
        canContainDirectly.addAll(canContainIndirectly);
        return canContainDirectly;
    }

    private static int howManyBagsAreContainedWithinOne(Map<String, Map<String, Integer>> rules, String color) {
        return rules.get(color).entrySet().stream()
                .mapToInt(entry -> entry.getValue()
                        + entry.getValue() * howManyBagsAreContainedWithinOne(rules, entry.getKey()))
                .sum();

    }

}