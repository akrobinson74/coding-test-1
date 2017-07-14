package com.hubrick.processors;

import com.hubrick.dto.Department;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ReportGenerator {
    private static final File NINETYFIFTH_PCT_INCOME_BY_DEPT =
        new File("95th_percentile_income_by_department.csv");
    private static final File AVERAGE_INCOME_BY_AGE =
        new File("average_income_by_age.csv");
    private static final File MEDIAN_AGE_BY_DEPT =
        new File("median_age_by_department.csv");
    private static final File MEDIAN_INCOME_BY_DEPT =
        new File("median_income_by_department.csv");

    public static void outputFiles(
        final Map<Department, List<Integer>> ageByDepartmentMap,
        final Map<Integer, List<Double>> incomeByAgeMap,
        final Map<Department, List<Double>> incomeByDepartmentMap) {
        outputAverageIncomeByAge(AVERAGE_INCOME_BY_AGE, incomeByAgeMap);

        String ninetyFifthPercentileOutput =
            outputMedianIncomeByDept(MEDIAN_INCOME_BY_DEPT, incomeByDepartmentMap);

        output95PercentIncomeByDept(NINETYFIFTH_PCT_INCOME_BY_DEPT,
            ninetyFifthPercentileOutput);

        outputMedianAgeByDept(MEDIAN_AGE_BY_DEPT, ageByDepartmentMap);
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
                            entry.getValue().stream().mapToDouble(d -> d).sum() /
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

