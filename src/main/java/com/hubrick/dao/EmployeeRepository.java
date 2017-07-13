package com.hubrick.dao;

import com.hubrick.dto.EmployeeRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class EmployeeRepository {

    public static Stream<EmployeeRecord> getAllEmployeeRecords(File inputData)
        throws IOException {
        return Files.lines(inputData.toPath())
            .map(line ->
                Arrays.stream(line.split(",")).map(f -> f.trim())
                    .collect(Collectors.toList()))
            .filter(fields -> fields.size() == 4)
            .map(EmployeeRecord::getEmployeeRecordForList)
            .filter(e -> e != null);
    }
}
