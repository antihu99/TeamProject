package com.sec.bestreviewer;

import com.sec.bestreviewer.command.Command;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.store.FieldEnum;
import com.sec.bestreviewer.util.ResultStringFormatter;
import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.sec.bestreviewer.CommandExecutor.MAX_RESULT_NUMBER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandExecutorTest {

    private EmployeeStore employeeStore;

    @BeforeEach
    void createMockEmployeeStore() {
        employeeStore = mock(EmployeeStore.class);
    }

    // 공통 출력 유틸리티
    private void printTestResult(String methodName, Object executed, Object expected) {
        System.out.println("\n===== [" + methodName + "] =====");
        System.out.println("==== Executed ====");
        System.out.println(executed);
        System.out.println("==== Expected ====");
        System.out.println(expected);
    }

    /**
     * [input] ADD 명령을 실행 (employee 정보 1명, -p 옵션)
     * [expected] 반환 리스트가 null이 아니어야 함 (실제로는 추가 성공/실패에 따른 값)
     */
    @Test
    void queryExecutorReturnsResultString() {
        final List<String> options = Collections.singletonList("-p");
        final List<String> params = Arrays.asList("18064527", "ANDY KIM", "CL2", "010-9623-6213", "19890803", "ADV");
        final Command command = CommandFactory.buildSingleCommand("ADD", options, params);
        final List<String> res = (new CommandExecutor()).execute(command);

        printTestResult("queryExecutorReturnsResultString", res, "not null");
        assertNotNull(res);
    }

    /**
     * [input] ADD 명령 실행 (옵션 없이, employee 정보 1명)
     * [expected] 반환 리스트가 비어 있어야 함 (ADD 명령 자체는 출력 없음)
     */
    @Test
    void testAddCommandReturnsEmptyList() {
        final List<String> options = Collections.emptyList();
        final List<String> params = Arrays.asList("18064527", "ANDY KIM", "CL2", "010-9623-6213", "19890803", "ADV");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_ADD, options, params);
        final List<String> res = (new CommandExecutor()).execute(command);

        printTestResult("testAddCommandReturnsEmptyList", res, "[] (empty list)");
        assertNotNull(res);
        assertEquals(0, res.size());
    }

    /**
     * [input] DEL 명령 실행 (옵션 -p, 삭제 대상 employee 개수 1/6)
     * [expected] 삭제된 사원의 리스트를 최대 5개까지 출력, 각 줄은 DEL,사원정보 형식
     */
    @Test
    void testDeleteCommandWithPrintOption() {
        printTestResult("testDeleteCommandWithPrintOption", "see below", "삭제된 사원 리스트 최대 5개 출력, 형식 맞게");
        deleteCommandWithPrintOption(1);
        deleteCommandWithPrintOption(6);
    }

    private void deleteCommandWithPrintOption(int count) {
        final List<Employee> employeeList = getEmployees(count);
        when(employeeStore.delete( FieldEnum.fromFieldName("name"), "ANDY KIM")).thenReturn(employeeList);

        final List<String> options = Collections.singletonList("-p");
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("deleteCommandWithPrintOption_" + count, resList, "DEL,사원정보 최대 5개");
        for (int i = 0; i < Math.min(count, MAX_RESULT_NUMBER); i++) {
            assertEquals(ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_DEL, employeeList.get(i)),
                    resList.get(i));
        }
    }

    private List<Employee> getEmployees(int count) {
        final List<Employee> employeeList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final String employeeNumber = Integer.toString(90_000000 + (i * 10_000000));
            employeeList.add(
                    new Employee(employeeNumber, "SEO KFI", "CL1", "010-1234-5678", "20190101", "ADV"));
        }
        return employeeList;
    }

    /**
     * [input] DEL 명령 실행 (옵션 -p, 삭제 대상 없음)
     * [expected] [DEL,NONE] 반환
     */
    @Test
    void testDeleteCommandWithPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "ANDY KIM")).thenReturn(employeeList);

        final List<String> options = Collections.singletonList("-p");
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testDeleteCommandWithPrintOption_NoneResult", resList, "[DEL,NONE]");
        assertEquals(CommandFactory.CMD_DEL + ",NONE", resList.get(0));
    }

    /**
     * [input] DEL 명령 실행 (옵션 없음, 삭제 대상 10개)
     * [expected] [DEL,10] 반환 (삭제된 사원 수)
     */
    @Test
    void testDeleteCommandWithOutPrintOption() {
        final int deletedCount = 10;
        final List<Employee> employeeList = getEmployees(deletedCount);
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "ANDY KIM")).thenReturn(employeeList);

        final List<String> options = Collections.emptyList();
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testDeleteCommandWithOutPrintOption", resList, "[DEL," + deletedCount + "]");
        assertEquals(CommandFactory.CMD_DEL + "," + deletedCount, resList.get(0));
    }

    /**
     * [input] DEL 명령 실행 (옵션 없음, 삭제 대상 없음)
     * [expected] [DEL,NONE] 반환
     */
    @Test
    void testDeleteCommandWithOutPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "ANDY KIM")).thenReturn(employeeList);

        final List<String> options = Collections.emptyList();
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testDeleteCommandWithOutPrintOption_NoneResult", resList, "[DEL,NONE]");
        assertEquals(CommandFactory.CMD_DEL + ",NONE", resList.get(0));
    }

    /**
     * [input] SCH 명령 실행 (옵션 -p, 검색 결과 1/6)
     * [expected] 검색된 사원 최대 5개, 각 줄은 SCH,사원정보 형식
     */
    @Test
    void testSearchCommandWithPrintOption() {
        printTestResult("testSearchCommandWithPrintOption", "see below", "검색된 사원 리스트 최대 5개 출력, 형식 맞게");
        searchCommandWithPrintOption(1);
        searchCommandWithPrintOption(6);
    }

    void searchCommandWithPrintOption(int count) {
        final List<Employee> employeeList = getEmployees(count);
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.search(FieldEnum.fromFieldName(fieldName), "ANDY KIM", TertiaryOptionEnum.NONE)).thenReturn(employeeList);

        final List<String> options = Collections.singletonList("-p");
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("searchCommandWithPrintOption_" + count, resList, "SCH,사원정보 최대 5개");
        for (int i = 0; i < Math.min(count, MAX_RESULT_NUMBER); i++) {
            assertEquals(
                    ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_SCH, employeeList.get(i)),
                    resList.get(i));
        }
    }

    /**
     * [input] SCH 명령 실행 (옵션 -p, 검색 결과 없음)
     * [expected] [SCH,NONE] 반환
     */
    @Test
    void testSearchCommandWithPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.search(FieldEnum.fromFieldName(fieldName), "ANDY KIM", TertiaryOptionEnum.NONE)).thenReturn(employeeList);

        final List<String> options = Collections.singletonList("-p");
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testSearchCommandWithPrintOption_NoneResult", resList, "[SCH,NONE]");
        assertEquals(CommandFactory.CMD_SCH + ",NONE", resList.get(0));
    }

    /**
     * [input] SCH 명령 실행 (옵션 없음, 검색 결과 10개)
     * [expected] [SCH,10] 반환 (검색된 사원 수)
     */
    @Test
    void testSearchCommandWithOutPrintOption() {
        final int deletedCount = 10;
        final List<Employee> employeeList = getEmployees(deletedCount);
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.search(FieldEnum.fromFieldName(fieldName), "ANDY KIM", TertiaryOptionEnum.NONE)).thenReturn(employeeList);

        final List<String> options = Collections.emptyList();
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testSearchCommandWithOutPrintOption", resList, "[SCH," + deletedCount + "]");
        assertEquals(CommandFactory.CMD_SCH + "," + deletedCount, resList.get(0));
    }

    /**
     * [input] SCH 명령 실행 (옵션 없음, 검색 결과 없음)
     * [expected] [SCH,NONE] 반환
     */
    @Test
    void testSearchCommandWithOutPrintOption_NoneResult() {
        final List<Employee> employeeList = getEmployees(0);
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.search(FieldEnum.fromFieldName(fieldName), "ANDY KIM", TertiaryOptionEnum.NONE)).thenReturn(employeeList);

        final List<String> options = Collections.emptyList();
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testSearchCommandWithOutPrintOption_NoneResult", resList, "[SCH,NONE]");
        assertEquals(CommandFactory.CMD_SCH + ",NONE", resList.get(0));
    }

    /**
     * [input] CNT 명령 실행 (employeeStore에 사원 1명 있을 때)
     * [expected] [CNT,1] 반환
     */
    @Test
    void testCountCommandShouldReturnCountNumberString() {
        when(employeeStore.count()).thenReturn(1);

        final List<String> options = Collections.emptyList();
        final List<String> params = Collections.emptyList();
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_CNT, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testCountCommandShouldReturnCountNumberString", resList, "[CNT,1]");
        assertEquals(CommandFactory.CMD_CNT + ",1", resList.get(0));
    }

    /**
     * [input] DEL 명령 실행 (옵션 -p, 삭제 대상 사원번호 내림차순으로 반환됨)
     * [expected] DEL,사원정보가 사원번호 오름차순(입사년 빠른 순)으로 5개까지 출력
     */
    @Test
    void testDeleteCommandReturnsSortedEmployeeList() {
        final int deletedCount = 10;
        final List<Employee> employeeList = getEmployees(deletedCount);
        final List<Employee> reversedEmployeeList = employeeList.stream()
                .sorted(Comparator.comparing(Employee::getEmployeeNumber).reversed())
                .collect(Collectors.toList());
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "ANDY KIM")).thenReturn(reversedEmployeeList);

        final List<String> options = Collections.singletonList("-p");
        final List<String> params = Arrays.asList("name", "ANDY KIM");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, options, params);
        final List<String> resList = (new CommandExecutor(employeeStore)).execute(command);

        printTestResult("testDeleteCommandReturnsSortedEmployeeList", resList, "삭제된 사원 리스트가 사원번호 오름차순(입사년 빠른순)으로 5개까지 출력되어야 함");
        for (int i = 0; i < Math.min(deletedCount, MAX_RESULT_NUMBER); i++) {
            assertEquals(
                    ResultStringFormatter.getEmployeeToFormattedString(CommandFactory.CMD_DEL, employeeList.get(i)),
                    resList.get(i));
        }
    }

    /**
     * [input] DEL,-p, , ,name,YUJIN KIM 명령을 실행 (전체 6명 중 동명이인 2명)
     *         → DEL 명령으로 YUJIN KIM 2명이 삭제됨
     *         → 이후 SCH 명령으로 남은 사원(4명)을 검색(-p 옵션)
     * [expected] DEL 이후 남은 4명의 사번만을 실제 결과와 기대 결과로 각각 출력/비교
     */
    @Test
    void testDeleteByNameWithDuplicateNames_RemainingListPrintBySCH() {
        List<Employee> allEmployees = Arrays.asList(
                new Employee("14000301", "YUJIN KIM", "CL2", "010-0977-0000", "19981206", "ADV"),
                new Employee("15000402", "YUJIN KIM", "CL3", "010-1111-2222", "19980301", "PRO"),
                new Employee("16000600", "MICHAEL OWEN", "CL1", "010-2222-3333", "19810630", "EX"),
                new Employee("17000777", "CHRIS OH", "CL1", "010-3333-4444", "19880716", "ADV"),
                new Employee("18000888", "HEUNGMIN SON", "CL2", "010-4444-5555", "19940929", "PRO"),
                new Employee("19000999", "SUZIE LEE", "CL4", "010-5555-6666", "19920521", "EX")
        );
        List<Employee> expectedRemaining = Arrays.asList(
                allEmployees.get(2),
                allEmployees.get(3),
                allEmployees.get(4),
                allEmployees.get(5)
        );

        List<Employee> toDelete = Arrays.asList(allEmployees.get(0), allEmployees.get(1));
        String fieldName = CommandFactory.getFieldMapParam("name");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "YUJIN KIM")).thenReturn(toDelete);
        List<String> delOptions = Arrays.asList("-p", "", "", "");
        List<String> delParams = Arrays.asList("name", "YUJIN KIM");
        Command delCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, delOptions, delParams);
        (new CommandExecutor(employeeStore)).execute(delCommand);

        when(employeeStore.search(any(), anyString(),any())).thenReturn(expectedRemaining);
        Command schCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH,
                Arrays.asList("-p", "", "", ""),
                Arrays.asList("cl", "")); // 조건은 예시

        List<String> searchResult = (new CommandExecutor(employeeStore)).execute(schCommand);

        String expected = employeesToEmployeeNums(expectedRemaining);
        String actual = searchResult.stream()
                .map(s -> s.split(",")[1])
                .reduce((a, b) -> a + "\n" + b).orElse("");

        printTestResult("testDeleteByNameWithDuplicateNames_RemainingListPrintBySCH", actual, expected);

        assertEquals(expected.trim(), actual.trim());
    }

    /**
     * [input] DEL,-p, , ,cl,CL3 명령을 실행 (전체 4명 중 경력개발단계 CL3인 2명)
     *         → DEL 명령으로 CL3 2명이 삭제됨
     *         → 이후 SCH 명령으로 남은 사원(2명)을 검색(-p 옵션)
     * [expected] DEL 이후 남은 2명의 사번만을 실제 결과와 기대 결과로 각각 출력/비교
     */
    @Test
    void testDeleteByClWithDuplicateCl_RemainingListPrintBySCH() {
        List<Employee> allEmployees = Arrays.asList(
                new Employee("21000111", "YUJIN KIM", "CL3", "010-1234-1111", "19981206", "EX"),
                new Employee("21000222", "MINHO LEE", "CL3", "010-5678-2222", "19981010", "PRO"),
                new Employee("21000333", "JISOO PARK", "CL1", "010-3333-3333", "19920701", "ADV"),
                new Employee("21000444", "BOB CHOI", "CL2", "010-4444-4444", "19930909", "ADV")
        );
        List<Employee> expectedRemaining = Arrays.asList(
                allEmployees.get(2),
                allEmployees.get(3)
        );

        List<Employee> toDelete = Arrays.asList(allEmployees.get(0), allEmployees.get(1));
        String fieldName = CommandFactory.getFieldMapParam("cl");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "CL3")).thenReturn(toDelete);

        List<String> delOptions = Arrays.asList("-p", "", "", "");
        List<String> delParams = Arrays.asList("cl", "CL3");
        Command delCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, delOptions, delParams);
        (new CommandExecutor(employeeStore)).execute(delCommand);

        when(employeeStore.search(any(), anyString(),any())).thenReturn(expectedRemaining);

        Command schCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH,
                Arrays.asList("-p", "", "", ""),
                Arrays.asList("cl", "")); // 조건은 예시

        List<String> searchResult = (new CommandExecutor(employeeStore)).execute(schCommand);

        String expected = employeesToEmployeeNums(expectedRemaining);
        String actual = searchResult.stream()
                .map(s -> s.split(",")[1])
                .reduce((a, b) -> a + "\n" + b).orElse("");

        printTestResult("testDeleteByClWithDuplicateCl_RemainingListPrintBySCH", actual, expected);

        assertEquals(expected.trim(), actual.trim());
    }

    /**
     * [input] DEL,-p, , ,phoneNum,010-8888-9999 명령을 실행 (전체 4명 중 전화번호가 같은 2명)
     *         → DEL 명령으로 phoneNum이 010-8888-9999인 2명이 삭제됨
     *         → 이후 SCH 명령으로 남은 사원(2명)을 검색(-p 옵션)
     * [expected] DEL 이후 남은 2명의 사번만을 실제 결과와 기대 결과로 각각 출력/비교
     */
    @Test
    void testDeleteByPhoneNumWithDuplicatePhoneNum_RemainingListPrintBySCH() {
        List<Employee> allEmployees = Arrays.asList(
                new Employee("30000111", "EUNJI PARK", "CL3", "010-8888-9999", "19981206", "ADV"),
                new Employee("30000222", "MINHO LEE", "CL2", "010-8888-9999", "19981010", "EX"),
                new Employee("30000333", "HANA KIM", "CL1", "010-2222-3333", "19920701", "PRO"),
                new Employee("30000444", "BOB CHOI", "CL4", "010-4444-4444", "19930909", "PRO")
        );
        List<Employee> expectedRemaining = Arrays.asList(
                allEmployees.get(2),
                allEmployees.get(3)
        );
        List<Employee> toDelete = Arrays.asList(allEmployees.get(0), allEmployees.get(1));
        String fieldName = CommandFactory.getFieldMapParam("phoneNum");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "010-8888-9999")).thenReturn(toDelete);

        List<String> delOptions = Arrays.asList("-p", "", "", "");
        List<String> delParams = Arrays.asList("phoneNum", "010-8888-9999");
        Command delCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, delOptions, delParams);
        (new CommandExecutor(employeeStore)).execute(delCommand);

        when(employeeStore.search(any(), anyString(),any())).thenReturn(expectedRemaining);
        Command schCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH,
                Arrays.asList("-p", "", "", ""),
                Arrays.asList("cl", "")); // 조건은 예시

        List<String> searchResult = (new CommandExecutor(employeeStore)).execute(schCommand);

        String expected = employeesToEmployeeNums(expectedRemaining);
        String actual = searchResult.stream()
                .map(s -> s.split(",")[1])
                .reduce((a, b) -> a + "\n" + b).orElse("");

        printTestResult("testDeleteByPhoneNumWithDuplicatePhoneNum_RemainingListPrintBySCH", actual, expected);

        assertEquals(expected.trim(), actual.trim());
    }

    /**
     * [input] DEL,-p, , ,birthday,19981206 명령을 실행 (전체 4명 중 생일이 같은 2명)
     *         → DEL 명령으로 birthday가 19981206인 2명이 삭제됨
     *         → 이후 SCH 명령으로 남은 사원(2명)을 검색(-p 옵션)
     * [expected] DEL 이후 남은 2명의 사번만을 실제 결과와 기대 결과로 각각 출력/비교
     */
    @Test
    void testDeleteByBirthdayWithDuplicateBirthday_RemainingListPrintBySCH() {
        List<Employee> allEmployees = Arrays.asList(
                new Employee("40000111", "JAEHYUN LEE", "CL3", "010-8888-9999", "19981206", "PRO"),
                new Employee("40000222", "MINA PARK", "CL2", "010-1212-3333", "19981206", "ADV"),
                new Employee("40000333", "SUNGHO KIM", "CL1", "010-1111-3333", "20000701", "EX"),
                new Employee("40000444", "SANDRA CHOI", "CL4", "010-4444-5555", "20010309", "EX")
        );
        List<Employee> expectedRemaining = Arrays.asList(
                allEmployees.get(2),
                allEmployees.get(3)
        );
        List<Employee> toDelete = Arrays.asList(allEmployees.get(0), allEmployees.get(1));
        String fieldName = CommandFactory.getFieldMapParam("birthday");
        when(employeeStore.delete(FieldEnum.fromFieldName(fieldName), "19981206")).thenReturn(toDelete);

        List<String> delOptions = Arrays.asList("-p", "", "", "");
        List<String> delParams = Arrays.asList("birthday", "19981206");
        Command delCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_DEL, delOptions, delParams);
        (new CommandExecutor(employeeStore)).execute(delCommand);

        when(employeeStore.search(any(), anyString(),any())).thenReturn(expectedRemaining);
        Command schCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH,
                Arrays.asList("-p", "", "", ""),
                Arrays.asList("cl", "")); // 조건은 예시

        List<String> searchResult = (new CommandExecutor(employeeStore)).execute(schCommand);

        String expected = employeesToEmployeeNums(expectedRemaining);
        String actual = searchResult.stream()
                .map(s -> s.split(",")[1])
                .reduce((a, b) -> a + "\n" + b).orElse("");

        printTestResult("testDeleteByBirthdayWithDuplicateBirthday_RemainingListPrintBySCH", actual, expected);

        assertEquals(expected.trim(), actual.trim());
    }

    // 사번만 출력하는 유틸
    private String employeesToEmployeeNums(List<Employee> list) {
        StringBuilder sb = new StringBuilder();
        for (Employee e : list) {
            sb.append(e.getEmployeeNumber()).append("\n");
        }
        return sb.toString();
    }

}
