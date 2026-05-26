package com.sec.bestreviewer.store;

import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeStoreImplCareerLevelTest {

    private EmployeeStoreImpl store;

    @BeforeEach
    void setUp() {
        store = new EmployeeStoreImpl();
        store.add(new Employee("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "PRO"));
        store.add(new Employee("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "ADV"));
        store.add(new Employee("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"));
        store.add(new Employee("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO"));
    }

    private static void printTestResult(String testName, List<String> expected, List<String> actual) {
        System.out.println("테스트 항목 : " + testName);
        System.out.println("예상값:");
        expected.forEach(System.out::println);
        System.out.println("실제값:");
        actual.forEach(System.out::println);
        boolean pass = expected.equals(actual);
        System.out.println("통과 여부 : " + (pass ? "통과" : "실패"));
        System.out.println("=".repeat(40));
    }

    private static String employeeToSCHString(Employee e) {
        return String.join(",",
                "SCH",
                e.getEmployeeNumber().toString(),
                e.getName().toString(),
                e.getCareerLevel().toString(),
                e.getPhoneNumber().toString(),
                e.getBirthday().toString(),
                e.getCerti().toString()
        );
    }

    @Test
    void testSearchCareerLevelGreater() {
        String testName = "경력단계 CL2보다 큰 직원 찾기 (G)";
        List<String> expected = List.of(
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000003", TertiaryOptionEnum.NONE, 0).get(0)),
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000004", TertiaryOptionEnum.NONE, 0).get(0))
        );
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL2", TertiaryOptionEnum.G, 0);
        List<String> actual = result.stream().map(EmployeeStoreImplCareerLevelTest::employeeToSCHString).collect(Collectors.toList());
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchCareerLevelGreaterEqual() {
        String testName = "경력단계 CL3보다 크거나 같은 직원 찾기 (GE)";
        List<String> expected = List.of(
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000003", TertiaryOptionEnum.NONE, 0).get(0)),
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000004", TertiaryOptionEnum.NONE, 0).get(0))
        );
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL3", TertiaryOptionEnum.GE, 0);
        List<String> actual = result.stream().map(EmployeeStoreImplCareerLevelTest::employeeToSCHString).collect(Collectors.toList());
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchCareerLevelSmaller() {
        String testName = "경력단계 CL3보다 작은 직원 찾기 (S)";
        List<String> expected = List.of(
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000001", TertiaryOptionEnum.NONE, 0).get(0)),
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000002", TertiaryOptionEnum.NONE, 0).get(0))
        );
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL3", TertiaryOptionEnum.S, 0);
        List<String> actual = result.stream().map(EmployeeStoreImplCareerLevelTest::employeeToSCHString).collect(Collectors.toList());
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchCareerLevelSmallerEqual() {
        String testName = "경력단계 CL2보다 작거나 같은 직원 찾기 (SE)";
        List<String> expected = List.of(
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000001", TertiaryOptionEnum.NONE, 0).get(0)),
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000002", TertiaryOptionEnum.NONE, 0).get(0))
        );
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL2", TertiaryOptionEnum.SE, 0);
        List<String> actual = result.stream().map(EmployeeStoreImplCareerLevelTest::employeeToSCHString).collect(Collectors.toList());
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchCareerLevelEqual() {
        String testName = "경력단계 CL3인 직원 찾기 (NONE/EQUAL)";
        List<String> expected = List.of(
                employeeToSCHString(store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000003", TertiaryOptionEnum.NONE, 0).get(0))
        );
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL3", TertiaryOptionEnum.NONE, 0);
        List<String> actual = result.stream().map(EmployeeStoreImplCareerLevelTest::employeeToSCHString).collect(Collectors.toList());
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchCareerLevelSmallerThanCL1() {
        String testName = "경력단계 CL1보다 작은 직원 찾기 (S, 없음)";
        List<String> expected = List.of();
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL1", TertiaryOptionEnum.S, 0);
        List<String> actual = result.stream().map(EmployeeStoreImplCareerLevelTest::employeeToSCHString).collect(Collectors.toList());
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchCareerLevelGreaterThanCL4() {
        String testName = "경력단계 CL4보다 큰 직원 찾기 (G, 없음)";
        List<String> expected = List.of();
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL4", TertiaryOptionEnum.G, 0);
        List<String> actual = result.stream().map(EmployeeStoreImplCareerLevelTest::employeeToSCHString).collect(Collectors.toList());
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }
}
