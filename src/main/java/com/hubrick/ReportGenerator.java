package com.hubrick;

import com.hubrick.dao.EmployeeAgesRepository;
import com.hubrick.dao.EmployeeRepository;
import com.hubrick.dto.Department;
import com.hubrick.dto.EmployeeRecord;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 */
public class ReportGenerator {
    private static final Map<Department, List<Integer>> AGE_BY_DEPARTMENT =
        new HashMap<>();
    private static final Map<Integer, List<Double>> INCOME_BY_AGE =
        new HashMap<>();
    private static final Map<Department, List<Double>> INCOME_BY_DEPARTMENT =
        new HashMap<>();

    private static final File NINETYFIFTH_PCT_INCOME_BY_DEPT =
        new File("95th_percentile_income_by_department.csv");
    private static final File AVERAGE_INCOME_BY_AGE =
        new File( "average_income_by_age.csv");
    private static final File MEDIAN_AGE_BY_DEPT =
        new File("median_age_by_department.csv");
    private static final File MEDIAN_INCOME_BY_DEPT =
        new File("median_income_by_department.csv");

    public static void main(String[] args) throws IOException {
        EmployeeAgesRepository.loadEmployeAgeData(new File(args[1]));

        EmployeeRepository.getAllEmployeeRecords(new File(args[0])).forEach(
            ReportGenerator::aggregateRecord
        );

        outputFiles();
    }

    private static void aggregateRecord(final EmployeeRecord employeeRecord) {
        Integer age = employeeRecord.getAge();
        Department department = employeeRecord.getDepartment();
        Double monthlySalary = employeeRecord.getMonthlySalary();

        if (!INCOME_BY_DEPARTMENT.containsKey(department)) {
            INCOME_BY_DEPARTMENT.put(department, new ArrayList<>());
        }
        INCOME_BY_DEPARTMENT.get(department).add(monthlySalary);

        if (!AGE_BY_DEPARTMENT.containsKey(department)) {
            AGE_BY_DEPARTMENT.put(department, new ArrayList<>());
        }
        AGE_BY_DEPARTMENT.get(department).add(age);

        aggregateIncomeByAges(age, monthlySalary);
    }

    private static void aggregateIncomeByAges(
        final Integer age,
        final Double monthlySalary) {

        final Integer ageRange = Math.floorDiv(age, 10) * 10;

        if (!INCOME_BY_AGE.containsKey(ageRange))
            INCOME_BY_AGE.put(ageRange, new ArrayList<>());
        INCOME_BY_AGE.get(ageRange).add(monthlySalary);
    }

    private static void outputFiles() {
        outputAverageIncomeByAge(AVERAGE_INCOME_BY_AGE, INCOME_BY_AGE);

        String ninetyFifthPercentileOutput =
            outputMedianIncomeByDept(MEDIAN_INCOME_BY_DEPT, INCOME_BY_DEPARTMENT);

        output95PercentIncomeByDept(NINETYFIFTH_PCT_INCOME_BY_DEPT,
            ninetyFifthPercentileOutput);

        outputMedianAgeByDept(MEDIAN_AGE_BY_DEPT, AGE_BY_DEPARTMENT);
    }

    private static void output95PercentIncomeByDept(
        final File outputFile,
        final String ninetyFifthPercentileOutput) {

        try (PrintWriter pw =
                 new PrintWriter(Files.newBufferedWriter(
                     Paths.get(outputFile.getPath())))) {

            pw.print(ninetyFifthPercentileOutput);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outputAverageIncomeByAge(
        final File outputFile,
        final Map<Integer, List<Double>> incomeByAge) {

        try (PrintWriter pw =
                 new PrintWriter(Files.newBufferedWriter(
                     Paths.get(outputFile.getPath())))) {
            incomeByAge.entrySet().stream().forEach(entry -> {
                pw.println(
                    String.join(",",
                        String.valueOf(entry.getKey()),
                        String.format("%.02f",
                            entry.getValue().stream().mapToDouble(d->d).sum() /
                                entry.getValue().size()
                        )
                    )
                );
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outputMedianAgeByDept(
        final File outputFile,
        final Map<Department, List<Integer>> ageByDepartment) {

        try (PrintWriter pw =
                 new PrintWriter(Files.newBufferedWriter(
                     Paths.get(outputFile.getPath())))) {

            ageByDepartment.entrySet().stream().forEach(entry -> {
                final List<Integer> ages = entry.getValue();
                Collections.sort(ages);
                final Integer medianAge = ages.get(ages.size() / 2);

                pw.println(
                    String.join(",", entry.getKey().stringify(),
                        String.valueOf(medianAge))
                );
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String outputMedianIncomeByDept(
        final File outputFile,
        final Map<Department, List<Double>> incomeByDepartment) {

        final StringBuffer ninetyFivePercentRecords = new StringBuffer();

        try (PrintWriter pw =
                 new PrintWriter(Files.newBufferedWriter(
                     Paths.get(outputFile.getPath())))) {
            incomeByDepartment.entrySet().stream().forEach(entry -> {
                final List<Double> incomes = entry.getValue();
                Collections.sort(incomes);
                final Double medianIncome =
                    incomes.get(Math.round(incomes.size() / 2));
                final Double nineFiveIncome =
                    incomes.get(Math.round(incomes.size() * 0.95f) - 1);
                final String departmentName = entry.getKey().stringify();

                ninetyFivePercentRecords.append(
                    String.join(",", departmentName,
                        String.format("%.02f", nineFiveIncome)) + "\n");

                pw.println(
                    String.join(",", departmentName,
                        String.format("%.02f", medianIncome))
                );
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ninetyFivePercentRecords.toString();
    }
}
