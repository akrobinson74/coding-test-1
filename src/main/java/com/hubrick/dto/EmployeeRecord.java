package com.hubrick.dto;

import com.hubrick.dao.EmployeeAgesRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class EmployeeRecord {
    private static final Logger LOGGER =
        Logger.getLogger("EmployeeRecordLogger");

    private final Integer age;
    private final Department department;
    private final String name;
    private final Gender gender;
    private final Double monthlySalary;

    public EmployeeRecord(
        final Integer age,
        final Department department,
        final String name,
        final Gender gender,
        final Double monthlySalary) {

        this.age = age;
        this.department = department;
        this.name = name;
        this.gender = gender;
        this.monthlySalary = monthlySalary;
    }


    public Integer getAge() {
        return age;
    }

    public Department getDepartment() {
        return department;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Double getMonthlySalary() {
        return monthlySalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof EmployeeRecord)) return false;

        EmployeeRecord that = (EmployeeRecord) o;

        if (age != that.age) return false;
        if (department != that.department) return false;
        if (! name.equals(that.name)) return false;
        if (gender != that.gender) return false;
        return monthlySalary.equals(that.monthlySalary);
    }

    @Override
    public int hashCode() {
        int result = age.hashCode();
        result = 31 * result + department.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + monthlySalary.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EmployeeRecord{" +
            "age=" + age +
            ", department=" + department +
            ", name='" + name + '\'' +
            ", gender=" + gender +
            ", monthlySalary=" + monthlySalary +
            '}';
    }

    public static EmployeeRecord getEmployeeRecordForList(List<String> data) {
        String name = data.get(1).trim();
        String genderStr = data.get(2).trim();

        EmployeeRecord employeeRecord = null;

        try {
            employeeRecord = new EmployeeRecord(
                EmployeeAgesRepository.getAgeForName(name),
                Department.getDepartmentForId(
                    Integer.valueOf(data.get(0).trim())),
                name,
                Gender.getGenderForAbbreviation(
                    genderStr != null && genderStr.equals("") ? "t" : genderStr
                ),
                Double.valueOf(data.get(3).trim())
            );
        }
        catch (Exception e) {
            LOGGER.log(
                Level.WARNING,
                String.format("Invalid Employee input data %s: %s",
                    data, e.getMessage())
            );
        }

        return employeeRecord;
    }
}
