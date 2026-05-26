package com.sec.bestreviewer;

import com.sec.bestreviewer.command.CombinationEnum;
import com.sec.bestreviewer.command.Command;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AndOrParameterTest {

    void runCommand(String line) {
        CommandParser commandParser = new CommandParser();
        CommandExecutor commandExecutor = new CommandExecutor();

        TokenGroup tokens = commandParser.parse(line);
        Command command = CommandFactory.buildCommand(tokens);
        List<String> result = commandExecutor.execute(command);
    }

    @Test
    void DEL_매개변수_테스트() {
        String line = "DEL, ,-m, ,phoneNum,0970,-o,-y, ,birthday,1990";
        runCommand(line);
    }

    @Test
    void MOD_매개변수_테스트() {
        String line = "MOD, ,-d, ,birthday,06,-o, , ,certi,PRO,birthday,19901225";
        runCommand(line);
    }

    @Test
    void SCH_매개변수_테스트() {
        String line = "SCH, ,-m, ,phoneNum,0970,-o,-y, ,birthday,1990";
        runCommand(line);
    }

    @Test
    public void testAndOrCommand() throws Exception {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/and_or_command_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/and_or_command_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        employeeManagement.run(args);

        //Approvals.verify(outputFile);
    }
}
