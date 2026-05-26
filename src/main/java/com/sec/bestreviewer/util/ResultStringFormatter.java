package com.sec.bestreviewer.util;

import com.sec.bestreviewer.store.Employee;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ResultStringFormatter {
    private static final int YEAR_1990_EMPLOYEE_NUMBER = 90_000000;
    private static final int YEAR_PREFIX_19 = 1900_000000;
    private static final int YEAR_PREFIX_20 = 2000_000000;

    public static List<String> getEmployeeListToFormattedString(List<Employee> employeeList,
                                                                String commandType, int count) {
        if (employeeList == null || employeeList.isEmpty()) {
            return Collections.singletonList(commandType + ",NONE");
        }
        return employeeList.stream()
                .sorted(Comparator.comparing(
                        employee -> getEmployeeNumber(Integer.parseInt(employee.getEmployeeNumber().toString()))))
                .limit(count)
                .map(employee -> getEmployeeToFormattedString(commandType, employee))
                .collect(Collectors.toList());
    }

    private static Integer getEmployeeNumber(int employeeNumber) {
        return employeeNumber >= YEAR_1990_EMPLOYEE_NUMBER ?
                YEAR_PREFIX_19 + employeeNumber : YEAR_PREFIX_20 + employeeNumber;
    }

    public static String getEmployeeToFormattedString(String commandType, Employee employee) {
        return commandType + "," + employee.getEmployeeNumber() + "," +
                employee.getName().toString() + "," +
                employee.getCareerLevel() + "," +
                employee.getPhoneNumber() + "," +
                employee.getBirthday() + "," +
                employee.getCerti();
    }

    public static List<String> getEmployeeListToFormattedString(List<Employee> employeeList, String commandType) {
        final String formattedString = employeeList.size() == 0 ? "NONE" : Integer.toString(employeeList.size());
        return Collections.singletonList(commandType + "," + formattedString);
    }
}