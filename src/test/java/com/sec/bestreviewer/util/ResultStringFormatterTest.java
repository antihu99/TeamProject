package com.sec.bestreviewer.util;

import com.sec.bestreviewer.store.Employee;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultStringFormatterTest {

    @Test
    void getEmployeeToFormattedStringFormatsAllFields() {
        Employee employee = employee("19000999", "SUZIE LEE", "CL4", "010-5555-6666", "19920521", "EX");

        assertEquals("SCH,19000999,SUZIE LEE,CL4,010-5555-6666,19920521,EX",
                ResultStringFormatter.getEmployeeToFormattedString("SCH", employee));
    }

    @Test
    void getEmployeeListToFormattedStringReturnsNoneWhenPrintedResultIsEmpty() {
        assertEquals(Collections.singletonList("SCH,NONE"),
                ResultStringFormatter.getEmployeeListToFormattedString(Collections.emptyList(), "SCH", 5));
    }

    @Test
    void getEmployeeListToFormattedStringSortsAcrossCenturiesAndLimitsResultCount() {
        List<Employee> employees = Arrays.asList(
                employee("12000000", "TWELVE YEAR", "CL2", "010-1200-0000", "20120101", "ADV"),
                employee("99000000", "NINETY NINE", "CL2", "010-9900-0000", "19990101", "ADV"),
                employee("01000000", "ZERO ONE", "CL2", "010-0100-0000", "20010101", "ADV"),
                employee("90000001", "NINETY ZERO", "CL2", "010-9000-0001", "19900101", "ADV")
        );

        assertEquals(Arrays.asList(
                        "DEL,90000001,NINETY ZERO,CL2,010-9000-0001,19900101,ADV",
                        "DEL,99000000,NINETY NINE,CL2,010-9900-0000,19990101,ADV",
                        "DEL,01000000,ZERO ONE,CL2,010-0100-0000,20010101,ADV"
                ),
                ResultStringFormatter.getEmployeeListToFormattedString(employees, "DEL", 3));
    }

    @Test
    void getEmployeeListToFormattedStringWithoutPrintOptionReturnsCount() {
        List<Employee> employees = Arrays.asList(
                employee("90000001", "NINETY ZERO", "CL2", "010-9000-0001", "19900101", "ADV"),
                employee("01000000", "ZERO ONE", "CL2", "010-0100-0000", "20010101", "ADV")
        );

        assertEquals(Collections.singletonList("MOD,2"),
                ResultStringFormatter.getEmployeeListToFormattedString(employees, "MOD"));
    }

    private Employee employee(String employeeNumber,
                              String name,
                              String careerLevel,
                              String phoneNumber,
                              String birthday,
                              String certi) {
        return new Employee(employeeNumber, name, careerLevel, phoneNumber, birthday, certi);
    }
}
