package com.hubrick.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class EmployeeAgesRepository {
    private static final Map<String, Integer> EMPLOYEE_AGES_BY_NAME =
        new HashMap<>();

    public static Integer getAgeForName(String name) {
//        System.out.println(EMPLOYEE_AGES_BY_NAME.toString());

        if (!EMPLOYEE_AGES_BY_NAME.containsKey(name))
            throw new RuntimeException(
                String.format("No age found for employee: %s", name)
            );

        return EMPLOYEE_AGES_BY_NAME.get(name);
    }

    public static void loadEmployeAgeData(File input) throws IOException {
        EMPLOYEE_AGES_BY_NAME.putAll(
            Files.lines(input.toPath())
            .map(line -> line.split(","))
            .collect(Collectors.toMap(
                fields -> fields[0].trim(),
                fields -> Integer.valueOf(fields[1].trim()))));

//            .map(fields -> fields[0], fields -> fields[1])
//            .forEach(e -> {System.out.println(e.toString());});
//            .putAll(EMPLOYEE_AGES_BY_NAME);
    }
}
