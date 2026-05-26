package com.sec.bestreviewer;

import com.sec.bestreviewer.command.Command;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.store.FieldEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandModTest {
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

    // 사번만 출력하는 유틸
    private String employeesToCareerLevel(List<Employee> list) {
        StringBuilder sb = new StringBuilder();
        for (Employee e : list) {
            sb.append(e.getCareerLevel()).append("\n");
        }
        return sb.toString();
    }

    /**
     * [input] MOD,-p, , ,name,YUJIN KIM,cl,CL4 명령을 실행 (전체 6명 중 동명이인 2명)
     *         → MOD 명령으로 YUJIN KIM 2명이 cl -> CL4 변경
     *         → 이후 SCH 명령으로 변경된 YUJIN KIM 2명을 검색(-p 옵션)
     * [expected] MOD 이후 YUJIN KIM 2명의 실제 결과와 기대 결과로 각각 출력/비교
     */
    @Test
    void testModifyByNameWithDuplicateNames_modifyListPrintBySCH() {
        List<Employee> allEmployees = Arrays.asList(
                new Employee("14000301", "YUJIN KIM", "CL2", "010-0977-0000", "19981206", "ADV"),
                new Employee("15000402", "YUJIN KIM", "CL3", "010-1111-2222", "19981206", "PRO"),
                new Employee("16000600", "MICHAEL OWEN", "CL1", "010-2222-3333", "19981206", "EX"),
                new Employee("17000777", "CHRIS OH", "CL1", "010-3333-4444", "19981206", "ADV"),
                new Employee("18000888", "HEUNGMIN SON", "CL2", "010-4444-5555", "19981206", "PRO")
        );
        List<Employee> expectedRemaining = Arrays.asList(
                new Employee("14000301", "YUJIN KIM", "CL4", "010-0977-0000", "19981206", "ADV"),
                new Employee("15000402", "YUJIN KIM", "CL4", "010-1111-2222", "19981206", "PRO"),
                new Employee("16000600", "MICHAEL OWEN", "CL1", "010-2222-3333", "19981206", "EX"),
                new Employee("17000777", "CHRIS OH", "CL1", "010-3333-4444", "19981206", "ADV"),
                new Employee("18000888", "HEUNGMIN SON", "CL2", "010-4444-5555", "19981206", "PRO")
        );

        String fieldModifyName = CommandFactory.getFieldMapParam("cl");
        when(employeeStore.modify(FieldEnum.fromFieldName("name"), "YUJIN KIM", FieldEnum.fromFieldName(fieldModifyName), "CL4")).thenReturn(allEmployees);
        List<String> modOptions = Arrays.asList("-p", "", "", "");
        List<String> modParams = Arrays.asList("name", "YUJIN KIM", "cl", "CL4");
        Command modCommand = CommandFactory.buildSingleCommand(CommandFactory.CMD_MOD, modOptions, modParams);
        (new CommandExecutor(employeeStore)).execute(modCommand);

        when(employeeStore.search(any(), anyString(),any(), anyInt())).thenReturn(expectedRemaining);
        final List<String> options = Arrays.asList("-p", " ", " ");
        final List<String> params = Arrays.asList("birthday", "19981206");
        final Command command = CommandFactory.buildSingleCommand(CommandFactory.CMD_SCH, options, params);
        final List<String> searchResult = (new CommandExecutor(employeeStore)).execute(command);

        String expected = employeesToCareerLevel(expectedRemaining);
        String actual = searchResult.stream()
                .map(s -> s.split(",")[3])
                .reduce((a, b) -> a + "\n" + b).orElse("");

        printTestResult("testDeleteByNameWithDuplicateNames_RemainingListPrintBySCH", actual, expected);

        assertEquals(expected.trim(), actual.trim());
    }
}
