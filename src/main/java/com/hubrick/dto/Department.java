package com.hubrick.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum Department {
    Information_Technology(1),
    Business_Development(2),
    Marketing(3),
    Public_Relations(4),
    Sales(5),
    Accounting(6),
    Human_Resources(7),
    Unknown(8);

    private static final Map<Integer, Department> DEPARTMENT_ID_MAP =
        new HashMap<>();

    private final int id;

    static {
        Arrays.stream(Department.values()).forEach(department -> {
            DEPARTMENT_ID_MAP.put(department.getId(), department);
        });
    }

    Department(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String stringify() {
        return this.name().replace("_", " ");
    }

    public static Department getDepartmentForId(int departmentId) {
        if (!DEPARTMENT_ID_MAP.containsKey(departmentId))
            throw new RuntimeException(
                String.format("No department found for id: %d", departmentId));

        return DEPARTMENT_ID_MAP.get(departmentId);
    }
}
