package com.hubrick;

import com.hubrick.dao.EmployeeAgesRepository;
import com.hubrick.dao.EmployeeRepository;
import com.hubrick.processors.InputAggregator;
import com.hubrick.processors.ReportGenerator;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class CSVGeneratingApp {
    public static void main(String[] args) throws IOException {
        EmployeeAgesRepository.loadEmployeAgeData(new File(args[1]));

        EmployeeRepository.getAllEmployeeRecords(new File(args[0])).forEach(
            InputAggregator::aggregateRecord
        );

        ReportGenerator.outputFiles(
            InputAggregator.getAgeByDepartment(),
            InputAggregator.getIncomeByAge(),
            InputAggregator.getIncomeByDepartment()
        );
    }
}