package org.arqarq.util;

import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    private Utils() {
    }

    public static List<Triple<Integer, BigDecimal, BigDecimal>> calculateThresholds(String data) throws ConfigMalformedException {
        List<Triple<Integer, BigDecimal, BigDecimal>> triples = new LinkedList<>();

        String[] split = data.split(";");
        for (String s : split) {
            String[] rangeAndPriority = s.split(":");
            if (rangeAndPriority.length != 2) {
                throw new ConfigMalformedException();
            }
            String[] minMax = rangeAndPriority[0].split("-");
            if (minMax.length != 2) {
                throw new ConfigMalformedException();
            }
            triples.add(Triple.of(Integer.valueOf(rangeAndPriority[1]), new BigDecimal(switchToDot(minMax[0])), new BigDecimal(switchToDot(minMax[1]))));
        }
        return triples;
    }

    public static String switchToDot(String withComma) {
        return withComma.replace(',', '.');
    }

    public static String switchToComma(String withDot) {
        return withDot.replace('.', ',');
    }

    public static class ConfigMalformedException extends Exception {
    }
}