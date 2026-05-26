package com.sec.bestreviewer.store;

import com.sec.bestreviewer.*;
import com.sec.bestreviewer.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeStoreImplNameTest {

    CommandParser commandParser = new CommandParser();
    CommandFactory commandFactory = new CommandFactory();
    CommandExecutor commandExecutor = new CommandExecutor();

    @BeforeEach
    void setUp() {
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000001,YUJIN LEE,CL1,010-1111-1111,19900101,ADV")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000002,JISU PARK,CL2,010-2222-2222,19910101,PRO")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000003,SOYEON JUNG,CL3,010-3333-3333,19920101,EX")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000004,SEUNGHUN CHOI,CL4,010-4444-4444,19930101,PRO")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000005,ARAM HAN,CL1,010-5555-5555,19940101,PRO")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000006,HOHAN KIM,CL2,010-6666-6666,19950101,ADV")));
        commandExecutor.execute(commandFactory.buildCommand(commandParser.parse("ADD, , , ,90000007,BRIAN MOON,CL2,010-7777-7777,19960101,ADV")));
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

    private static String employeeToSCHString(String empNo, String name, String cl, String phone, String birth, String certi) {
        return String.join(",", "SCH", empNo, name, cl, phone, birth, certi);
    }

    // --------- FirstName (이름) 테스트 ---------
    @Test
    void firstNameSmallerThanTarget() {
        String testName = "이름(FirstName)이 TARGET보다 사전적으로 작은 직원 찾기 (-s)";
        // 프로그램 실제 반환 순서(사번 오름차순): JISU, SEUNGHUN, ARAM, HOHAN, BRIAN
        List<String> expected = List.of(
                employeeToSCHString("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "PRO"),
                employeeToSCHString("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO"),
                employeeToSCHString("90000005", "ARAM HAN", "CL1", "010-5555-5555", "19940101", "PRO"),
                employeeToSCHString("90000006", "HOHAN KIM", "CL2", "010-6666-6666", "19950101", "ADV"),
                employeeToSCHString("90000007", "BRIAN MOON", "CL2", "010-7777-7777", "19960101", "ADV")
        );
        String line = "SCH,-p,-f,-s,name,SOYEON";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void firstNameSmallerEqualThanTarget() {
        String testName = "이름(FirstName)이 TARGET보다 작거나 같은 직원 찾기 (-se)";
        // 프로그램 실제 반환 순서(사번 오름차순): JISU, SOYEON, SEUNGHUN, ARAM, HOHAN
        List<String> expected = List.of(
                employeeToSCHString("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "PRO"),
                employeeToSCHString("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"),
                employeeToSCHString("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO"),
                employeeToSCHString("90000005", "ARAM HAN", "CL1", "010-5555-5555", "19940101", "PRO"),
                employeeToSCHString("90000006", "HOHAN KIM", "CL2", "010-6666-6666", "19950101", "ADV")
        );
        String line = "SCH,-p,-f,-se,name,SOYEON";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void firstNameGreaterThanTarget() {
        String testName = "이름(FirstName)이 TARGET보다 사전적으로 큰 직원 찾기 (-g)";
        // 실제 반환: YUJIN, SOYEON, SEUNGHUN (사번 오름차순)
        List<String> expected = List.of(
                employeeToSCHString("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "ADV"),
                employeeToSCHString("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"),
                employeeToSCHString("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO")
        );
        String line = "SCH,-p,-f,-g,name,JISU";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void firstNameGreaterEqualThanTarget() {
        String testName = "이름(FirstName)이 TARGET보다 크거나 같은 직원 찾기 (-ge)";
        // 실제 반환: YUJIN, JISU, SOYEON, SEUNGHUN (사번 오름차순)
        List<String> expected = List.of(
                employeeToSCHString("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "ADV"),
                employeeToSCHString("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "PRO"),
                employeeToSCHString("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"),
                employeeToSCHString("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO")
        );
        String line = "SCH,-p,-f,-ge,name,JISU";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    // --------- LastName (성) 테스트 ---------
    @Test
    void lastNameSmallerThanTarget() {
        String testName = "성(LastName)이 TARGET보다 사전적으로 작은 직원 찾기 (-s)";
        // 실제 반환: SOYEON JUNG, SEUNGHUN CHOI, ARAM HAN
        List<String> expected = List.of(
                employeeToSCHString("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"),
                employeeToSCHString("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO"),
                employeeToSCHString("90000005", "ARAM HAN", "CL1", "010-5555-5555", "19940101", "PRO")
        );
        String line = "SCH,-p,-l,-s,name,KIM";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void lastNameSmallerEqualThanTarget() {
        String testName = "성(LastName)이 TARGET보다 작거나 같은 직원 찾기 (-se)";
        // 실제 반환: SOYEON JUNG, SEUNGHUN CHOI, ARAM HAN, HOHAN KIM
        List<String> expected = List.of(
                employeeToSCHString("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"),
                employeeToSCHString("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO"),
                employeeToSCHString("90000005", "ARAM HAN", "CL1", "010-5555-5555", "19940101", "PRO"),
                employeeToSCHString("90000006", "HOHAN KIM", "CL2", "010-6666-6666", "19950101", "ADV")
        );
        String line = "SCH,-p,-l,-se,name,KIM";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void lastNameGreaterThanTarget() {
        String testName = "성(LastName)이 TARGET보다 사전적으로 큰 직원 찾기 (-g)";
        // 실제 반환: YUJIN LEE, JISU PARK, BRIAN MOON (사번 오름차순)
        List<String> expected = List.of(
                employeeToSCHString("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "ADV"),
                employeeToSCHString("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "PRO"),
                employeeToSCHString("90000007", "BRIAN MOON", "CL2", "010-7777-7777", "19960101", "ADV")
        );
        String line = "SCH,-p,-l,-g,name,KIM";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void lastNameGreaterEqualThanTarget() {
        String testName = "성(LastName)이 TARGET보다 크거나 같은 직원 찾기 (-ge)";
        // 실제 반환: YUJIN LEE, JISU PARK, HOHAN KIM, BRIAN MOON (사번 오름차순)
        List<String> expected = List.of(
                employeeToSCHString("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "ADV"),
                employeeToSCHString("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "PRO"),
                employeeToSCHString("90000006", "HOHAN KIM", "CL2", "010-6666-6666", "19950101", "ADV"),
                employeeToSCHString("90000007", "BRIAN MOON", "CL2", "010-7777-7777", "19960101", "ADV")
        );
        String line = "SCH,-p,-l,-ge,name,KIM";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }

    // --------- FullName (일치) ---------
    @Test
    void fullNameEqual() {
        String testName = "전체 이름이 TARGET과 일치하는 직원 찾기";
        List<String> expected = List.of(
                employeeToSCHString("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX")
        );
        String line = "SCH,-p, , ,name,SOYEON JUNG";
        TokenGroup tokenGroup = commandParser.parse(line);
        Command command = commandFactory.buildCommand(tokenGroup);
        List<String> actual = commandExecutor.execute(command);
        printTestResult(testName, expected, actual);
        assertEquals(expected, actual);
    }
}
