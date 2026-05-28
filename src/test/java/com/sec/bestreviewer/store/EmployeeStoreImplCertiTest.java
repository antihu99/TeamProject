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

class EmployeeStoreImplCertiTest {

    private final CommandParser commandParser = new CommandParser();
    private final CommandFactory commandFactory = new CommandFactory();
    private final CommandExecutor commandExecutor = new CommandExecutor();

    @BeforeEach
    void setUp() {
        commandExecutor.execute(build("ADD, , , ,90000001,YUJIN LEE,CL1,010-1111-1111,19900101,ADV"));
        commandExecutor.execute(build("ADD, , , ,90000002,JISU PARK,CL2,010-2222-2222,19910101,PRO"));
        commandExecutor.execute(build("ADD, , , ,90000003,SOYEON JUNG,CL3,010-3333-3333,19920101,EX"));
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
    void searchCertiGreaterOrEqualPro() {
        List<String> expected = List.of(
                sch("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "PRO"),
                sch("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX")
        );

        List<String> actual = execute("SCH,-p, ,-ge,certi,PRO");

        assertEquals(expected, actual);
    }

    @Test
    void searchCertiExactMatch() {
        List<String> expected = List.of(
                sch("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "ADV")
        );

        List<String> actual = execute("SCH,-p, , ,certi,ADV");

        assertEquals(expected, actual);
    }

    @Test
    void modifyNameForCertiAdvEmployees() {
        List<String> expected = List.of(
                "MOD,90000001,YUJIN LEE,CL1,010-1111-1111,19900101,ADV"
        );

        List<String> actual = execute("MOD,-p, , ,certi,ADV,name,YUJIN CHOI");

        assertEquals(expected, actual);
        assertEquals("YUJIN CHOI", execute("SCH,-p, , ,name,YUJIN CHOI").get(0).split(",")[2]);
    }
}
