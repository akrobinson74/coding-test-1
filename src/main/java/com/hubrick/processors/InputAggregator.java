package com.hubrick.processors;

import com.hubrick.dto.Department;
import com.hubrick.dto.EmployeeRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class InputAggregator {
    private static final Map<Department, List<Integer>> AGE_BY_DEPARTMENT =
        new HashMap<>();
    private static final Map<Integer, List<Double>> INCOME_BY_AGE =
        new HashMap<>();
    private static final Map<Department, List<Double>> INCOME_BY_DEPARTMENT =
        new HashMap<>();


    public static void aggregateRecord(final EmployeeRecord employeeRecord) {
        Integer age = employeeRecord.getAge();
        Department department = employeeRecord.getDepartment();
        Double monthlySalary = employeeRecord.getMonthlySalary();

        if (! INCOME_BY_DEPARTMENT.containsKey(department)) {
            INCOME_BY_DEPARTMENT.put(department, new ArrayList<>());
        }
        INCOME_BY_DEPARTMENT.get(department).add(monthlySalary);

        if (! AGE_BY_DEPARTMENT.containsKey(department)) {
            AGE_BY_DEPARTMENT.put(department, new ArrayList<>());
        }
        AGE_BY_DEPARTMENT.get(department).add(age);

        aggregateIncomeByAges(age, monthlySalary);
    }

    private static void aggregateIncomeByAges(
        final Integer age,
        final Double monthlySalary) {

        final Integer ageRange = Math.floorDiv(age, 10) * 10;

        if (! INCOME_BY_AGE.containsKey(ageRange))
            INCOME_BY_AGE.put(ageRange, new ArrayList<>());
        INCOME_BY_AGE.get(ageRange).add(monthlySalary);
    }

    public static Map<Department, List<Integer>> getAgeByDepartment() {
        return AGE_BY_DEPARTMENT;
    }

    public static Map<Integer, List<Double>> getIncomeByAge() {
        return INCOME_BY_AGE;
    }

    public static Map<Department, List<Double>> getIncomeByDepartment() {
        return INCOME_BY_DEPARTMENT;
    }
}
