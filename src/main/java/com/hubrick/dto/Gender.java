package com.hubrick.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public enum Gender {
    FEMALE("f"),
    MALE("m"),
    NEUTER("n"),
    TRANSITIONING("t");

    private static final Map<String, Gender> GENDER_MAP = new HashMap<>();
    private final String abbreviation;

    static {
        GENDER_MAP.putAll(Arrays.stream(Gender.values()).collect(
            Collectors.toMap(g -> g.getAbbreviation(), g -> g))
        );
    }

    Gender(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Gender getGenderForAbbreviation(String genderAbbreviation) {
        if (!GENDER_MAP.containsKey(genderAbbreviation))
            throw new RuntimeException(
                String.format("No Gender found for abbreviation: %s",
                    genderAbbreviation));

        return GENDER_MAP.get(genderAbbreviation);
    }
}
