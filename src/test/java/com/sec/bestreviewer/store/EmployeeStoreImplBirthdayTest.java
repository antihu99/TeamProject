package com.sec.bestreviewer.store;

import com.sec.bestreviewer.CommandExecutor;
import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.CommandParser;
import com.sec.bestreviewer.TokenGroup;
import com.sec.bestreviewer.command.Command;
import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeStoreImplBirthdayTest {
    private EmployeeStoreImpl store;

    CommandParser commandParser = new CommandParser();
    CommandFactory commandFactory = new CommandFactory();
    CommandExecutor commandExecutor = new CommandExecutor();

    @BeforeEach
    void setUp() {
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000001,YUJIN LEE,CL1,010-1111-1111,19900101,ADV")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000002,AUJIN SEE,CL2,010-1111-1111,19910202,PRO")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000003,BUJIN FEE,CL3,010-1111-1111,19920303,EX")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000004,CUJIN HEE,CL4,010-1111-1111,19930404,ADV")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000005,HUJIN EEE,CL4,010-1111-1111,19950505,EX")));
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
    void testSearchBirthYearSmaller() {
        String testName = "년도(Year)가 1992 보다 앞선 직원 찾기 (S)";
        // JISU 본인 제외, 앞서는 이름 없음
        List<String> expected = List.of(
                employeeToSCHString(new Employee("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "ADV")),
                employeeToSCHString(new Employee("90000002", "AUJIN SEE", "CL2", "010-1111-1111", "19910202", "PRO"))
        );
        String line = "SCH,-p,-y,-s,birthday,1992";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);

        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchBirthYearSmallerEqual() {
        String testName = "년도(Year)가 1992 보다 앞서거나 같은 직원 찾기 (SE)";
        // JISU 본인 제외, 앞서는 이름 없음
        List<String> expected = List.of(
                employeeToSCHString(new Employee("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "ADV")),
                employeeToSCHString(new Employee("90000002", "AUJIN SEE", "CL2", "010-1111-1111", "19910202", "PRO")),
                employeeToSCHString(new Employee("90000003", "BUJIN FEE", "CL3", "010-1111-1111", "19920303", "EX"))
        );
        String line = "SCH,-p,-y,-se,birthday,1992";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);

        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchBirthYearGreater() {
        String testName = "년도(Year)가 1992 보다 뒤인 직원 찾기 (G)";
        // JISU 본인 제외, 앞서는 이름 없음
        List<String> expected = List.of(
                employeeToSCHString(new Employee("90000004", "CUJIN HEE", "CL4", "010-1111-1111", "19930404", "ADV")),
                employeeToSCHString(new Employee("90000005", "HUJIN EEE", "CL4", "010-1111-1111", "19950505", "EX"))
        );
        String line = "SCH,-p,-y,-g,birthday,1992";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);

        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchBirthYearGreaterEqual() {
        String testName = "년도(Year)가 1992 보다 뒤거나 같은 직원 찾기 (GE)";
        // JISU 본인 제외, 앞서는 이름 없음
        List<String> expected = List.of(
                employeeToSCHString(new Employee("90000003", "BUJIN FEE", "CL3", "010-1111-1111", "19920303", "EX")),
                employeeToSCHString(new Employee("90000004", "CUJIN HEE", "CL4", "010-1111-1111", "19930404", "ADV")),
                employeeToSCHString(new Employee("90000005", "HUJIN EEE", "CL4", "010-1111-1111", "19950505", "EX"))
        );
        String line = "SCH,-p,-y,-ge,birthday,1992";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);

        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSearchBirthYearGreaterEqualAnd() {
        String testName = "년도(Year)가 1992 보다 뒤거나 같은 직원 찾기 (GE)";
        // JISU 본인 제외, 앞서는 이름 없음
        List<String> expected = List.of(
                employeeToSCHString(new Employee("90000004", "CUJIN HEE", "CL4", "010-1111-1111", "19930404", "ADV"))
        );
        String line = "SCH,-p,-y,-ge,birthday,1992,-a, , ,birthday,19930404";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);

        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }
}
