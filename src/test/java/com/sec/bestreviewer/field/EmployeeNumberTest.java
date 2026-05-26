package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EmployeeNumberTest {

    private void printResult(String testName, String input, Object expected, Object actual) {
        System.out.println("\nTest: " + testName);
        System.out.println("Input: " + input);
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
    }

    @Test
    void testParsingFromString() {
        EmployeeNumber employeeNumber1 = new EmployeeNumber("99123456");
        printResult("getYear", "대상 EmployeeNumber(\"99123456\")", 99, employeeNumber1.getYear());
        assertEquals(99, employeeNumber1.getYear());

        printResult("getMod", "대상 EmployeeNumber(\"99123456\")", 123456, employeeNumber1.getMod());
        assertEquals(123456, employeeNumber1.getMod());

        printResult("toString", "대상 EmployeeNumber(\"99123456\")", "99123456", employeeNumber1.toString());
        assertEquals("99123456", employeeNumber1.toString());
    }

    @Test
    void testCompare() {
        EmployeeNumber employeeNumber1 = new EmployeeNumber("99000000");
        EmployeeNumber employeeNumber2 = new EmployeeNumber("99000001");
        EmployeeNumber employeeNumber3 = new EmployeeNumber("98000000");
        EmployeeNumber employeeNumber4 = new EmployeeNumber("19000000");

        printResult("compare (-s)", "대상 EmployeeNumber(\"99000000\"), 비교 EmployeeNumber(\"99000001\")",
                true, employeeNumber1.compare(employeeNumber2,0, TertiaryOptionEnum.S));
        assertEquals(true, employeeNumber1.compare(employeeNumber2,0, TertiaryOptionEnum.S));

        printResult("compare (-s)", "대상 EmployeeNumber(\"99000000\"), 비교 EmployeeNumber(\"99000001\")",
                true, employeeNumber1.compare(employeeNumber3,0, TertiaryOptionEnum.G));
        assertEquals(true, employeeNumber1.compare(employeeNumber3,0, TertiaryOptionEnum.G));

        printResult("compare (-s)", "대상 EmployeeNumber(\"99000000\"), 비교 EmployeeNumber(\"99000001\")",
                false, employeeNumber1.compare(employeeNumber4,0, TertiaryOptionEnum.G));
        assertEquals(false, employeeNumber1.compare(employeeNumber4,0, TertiaryOptionEnum.G));

        printResult("compare (-s)", "대상 EmployeeNumber(\"99000000\"), 비교 EmployeeNumber(\"99000001\")",
                true, employeeNumber4.compare(employeeNumber1,0, TertiaryOptionEnum.G));
        assertEquals(true, employeeNumber4.compare(employeeNumber1,0, TertiaryOptionEnum.G));
    }

    @Test
    void testCompareTo() {
        EmployeeNumber employeeNumber1 = new EmployeeNumber("99000000");
        EmployeeNumber employeeNumber2 = new EmployeeNumber("99000000");
        EmployeeNumber employeeNumber3 = new EmployeeNumber("99000001");
        EmployeeNumber employeeNumber4 = new EmployeeNumber("98000000");

        assertTrue(employeeNumber1.compareTo(employeeNumber3) < 0);
        assertTrue(employeeNumber1.compareTo(employeeNumber2) == 0);
        assertTrue(employeeNumber1.compareTo(employeeNumber4) > 0);
    }

    @Test
    void testEquals() {
        EmployeeNumber employeeNumber1 = new EmployeeNumber("99000000");
        EmployeeNumber employeeNumber2 = new EmployeeNumber("99000001");

        assertTrue(employeeNumber1.equals("99000000"));
        assertNotEquals(employeeNumber1, new Name("ABC DEF"));
        assertNotEquals(employeeNumber1, employeeNumber2);
    }
}
