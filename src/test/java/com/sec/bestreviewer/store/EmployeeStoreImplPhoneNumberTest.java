package com.sec.bestreviewer.store;

import com.sec.bestreviewer.CommandExecutor;
import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.CommandParser;
import com.sec.bestreviewer.TokenGroup;
import com.sec.bestreviewer.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeStoreImplPhoneNumberTest {

    private final CommandParser commandParser = new CommandParser();
    private final CommandFactory commandFactory = new CommandFactory();
    private final CommandExecutor commandExecutor = new CommandExecutor();

    @BeforeEach
    void setUp() {
        commandExecutor.execute(build("ADD, , , ,90000001,YUJIN LEE,CL1,010-0970-1111,19900101,ADV"));
        commandExecutor.execute(build("ADD, , , ,90000002,JISU PARK,CL2,010-2222-0055,19910101,PRO"));
        commandExecutor.execute(build("ADD, , , ,90000003,SOYEON JUNG,CL3,010-3333-3333,19920101,EX"));
        commandExecutor.execute(build("ADD, , , ,90000004,MINHO KIM,CL4,010-0970-0055,19930101,PRO"));
    }

    private List<String> execute(String line) {
        return commandExecutor.execute(build(line));
    }

    private Command build(String line) {
        TokenGroup tokenGroup = commandParser.parse(line);
        return commandFactory.buildCommand(tokenGroup);
    }

    private static String sch(String empNo, String name, String cl, String phone, String birth, String certi) {
        return String.join(",", "SCH", empNo, name, cl, phone, birth, certi);
    }

    @Test
    void searchByPhoneMiddleDigits() {
        List<String> expected = List.of(
                sch("90000001", "YUJIN LEE", "CL1", "010-0970-1111", "19900101", "ADV"),
                sch("90000004", "MINHO KIM", "CL4", "010-0970-0055", "19930101", "PRO")
        );

        List<String> actual = execute("SCH,-p,-m, ,phoneNum,0970");

        assertEquals(expected, actual);
    }

    @Test
    void searchByPhoneLastDigits() {
        List<String> expected = List.of(
                sch("90000002", "JISU PARK", "CL2", "010-2222-0055", "19910101", "PRO"),
                sch("90000004", "MINHO KIM", "CL4", "010-0970-0055", "19930101", "PRO")
        );

        List<String> actual = execute("SCH,-p,-l, ,phoneNum,0055");

        assertEquals(expected, actual);
    }

    @Test
    void searchByPhoneMiddleWithComparison() {
        List<String> expected = List.of(
                sch("90000001", "YUJIN LEE", "CL1", "010-0970-1111", "19900101", "ADV"),
                sch("90000004", "MINHO KIM", "CL4", "010-0970-0055", "19930101", "PRO")
        );

        List<String> actual = execute("SCH,-p,-m,-se,phoneNum,0970");

        assertEquals(expected, actual);
    }

    @Test
    void deleteByPhoneMiddleDigits() {
        List<String> expected = List.of(
                "DEL,90000001,YUJIN LEE,CL1,010-0970-1111,19900101,ADV",
                "DEL,90000004,MINHO KIM,CL4,010-0970-0055,19930101,PRO"
        );

        List<String> actual = execute("DEL,-p,-m, ,phoneNum,0970");

        assertEquals(expected, actual);
        assertEquals("SCH,1", execute("SCH, , , ,cl,CL2").get(0));
        assertEquals("SCH,1", execute("SCH, , , ,cl,CL3").get(0));
    }

    @Test
    void modifyCertiByPhoneLastDigits() {
        List<String> expected = List.of(
                "MOD,90000002,JISU PARK,CL2,010-2222-0055,19910101,PRO",
                "MOD,90000004,MINHO KIM,CL4,010-0970-0055,19930101,PRO"
        );

        List<String> actual = execute("MOD,-p,-l, ,phoneNum,0055,certi,EX");

        assertEquals(expected, actual);
        assertEquals("EX", execute("SCH,-p, , ,certi,EX").get(0).split(",")[6]);
    }

    @Test
    void searchPhoneMiddleOrBirthYear() {
        List<String> expected = List.of(
                sch("90000001", "YUJIN LEE", "CL1", "010-0970-1111", "19900101", "ADV"),
                sch("90000004", "MINHO KIM", "CL4", "010-0970-0055", "19930101", "PRO")
        );

        List<String> actual = execute("SCH,-p,-m, ,phoneNum,0970,-o,-y, ,birthday,1990");

        assertEquals(expected, actual);
    }
}
