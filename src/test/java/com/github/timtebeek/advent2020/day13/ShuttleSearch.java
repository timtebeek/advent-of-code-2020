package com.github.timtebeek.advent2020.day13;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ShuttleSearch {

    private static final String NOTES_SAMPLE1 = "7,13,x,x,59,x,31,19";
    private static final String NOTES_PART1 = "41,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,"
            + "37,x,x,x,x,x,911,x,x,x,x,x,x,x,x,x,x,x,x,13,17,x,x,x,x,x,x,x,x,23,x,x,x,x,x,29,x,827,x,"
            + "x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,19";

    @Test
    void testSample1() throws Exception {
        var leaveAt = 939;
        var buslines = parseNotes(NOTES_SAMPLE1);
        assertThat(findEarliestBusMultipliedByWaitingTime(leaveAt, buslines)).isEqualByComparingTo(295);
    }

    @Test
    void testPart1() throws Exception {
        var leaveAt = 1011416;
        var buslines = parseNotes(NOTES_PART1);
        assertThat(findEarliestBusMultipliedByWaitingTime(leaveAt, buslines)).isEqualByComparingTo(4135);
    }

    private static List<Integer> parseNotes(String notes) {
        return Stream.of(notes.split(","))
                .filter(StringUtils::isNumeric)
                .map(Integer::valueOf)
                .collect(toList());
    }

    private static int findEarliestBusMultipliedByWaitingTime(int leaveAt, List<Integer> buslines) {
        int earliestBus = -1;
        int earliestTime = Integer.MAX_VALUE;
        for (Integer bus : buslines) {
            int nextTime = leaveAt + bus - leaveAt % bus;
            if (nextTime < earliestTime) {
                earliestBus = bus;
                earliestTime = nextTime;
            }
        }
        return earliestBus * (earliestTime - leaveAt);
    }

    @ParameterizedTest
    @CsvSource(delimiterString = " first occurs at timestamp ", value = {
            "17,x,13,19 first occurs at timestamp 3417",
            "67,7,59,61 first occurs at timestamp 754018",
            "67,x,7,59,61 first occurs at timestamp 779210",
            "67,7,x,59,61 first occurs at timestamp 1261476",
            "7,13,x,x,59,x,31,19 first occurs at timestamp 1068781",
            "1789,37,47,1889 first occurs at timestamp 1202161486"
    })
    void testSample2(String notes, long expectedTimestamp) {
        var constraints = parseConstraints(notes);
        assertThat(findFirstTimestamp(constraints, 0)).isEqualByComparingTo(expectedTimestamp);
    }

    @Test
    @Disabled
    void testPart2() {
        var constraints = parseConstraints(NOTES_PART1);
        assertThat(findFirstTimestamp(constraints, 100000000000000L)).isEqualByComparingTo(-1L);
    }

    private static Map<Long, Long> parseConstraints(String notes) {
        String[] split = notes.split(",");
        Map<Long, Long> constraints = new LinkedHashMap<>();
        for (int i = 0; i < split.length; i++) {
            String bus = split[i];
            if (!StringUtils.isNumeric(bus)) {
                continue;
            }
            constraints.put((long) i, Long.valueOf(split[i]));
        }
        return constraints;
    }

    private static Long findFirstTimestamp(Map<Long, Long> constraints, long startAt) {
        long firstBus = constraints.remove(0L);
        return Stream.generate(new Supplier<Long>() {
            private long timestamp = startAt;

            @Override
            public Long get() {
                return timestamp += firstBus;
            }
        })
                .filter(timestamp -> constraints.entrySet().stream()
                        .allMatch(entry -> {
                            Long offset = entry.getKey();
                            long bus = entry.getValue();
                            return (timestamp + offset) % bus == 0;
                        }))
                .findFirst()
                .get();
    }
}
