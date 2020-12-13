package com.github.timtebeek.advent2020.day13;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ShuttleSearch {

    @Test
    void testSample1() throws Exception {
        var leaveAt = 939;
        var buslines = parseNotes("7,13,x,x,59,x,31,19");
        assertThat(findEarliestBusMultipliedByWaitingTime(leaveAt, buslines)).isEqualByComparingTo(295);
    }

    @Test
    void testPart1() throws Exception {
        var leaveAt = 1011416;
        var buslines = parseNotes("41,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,"
                + "37,x,x,x,x,x,911,x,x,x,x,x,x,x,x,x,x,x,x,13,17,x,x,x,x,x,x,x,x,23,x,x,x,x,x,29,x,827,x,"
                + "x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,19");
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
}
