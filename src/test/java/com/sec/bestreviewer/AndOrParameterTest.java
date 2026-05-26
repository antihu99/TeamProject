package com.sec.bestreviewer;

import com.sec.bestreviewer.command.Command;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
        assertDoesNotThrow(() -> runCommand(line));
    }

    @Test
    void MOD_매개변수_테스트() {
        String line = "MOD, ,-d, ,birthday,06,-o, , ,certi,PRO,birthday,19901225";
        assertDoesNotThrow(() -> runCommand(line));
    }

    @Test
    void SCH_매개변수_테스트() {
        String line = "SCH, ,-m, ,phoneNum,0970,-o,-y, ,birthday,1990";
        assertDoesNotThrow(() -> runCommand(line));
    }

    @Test
    public void testAndOrCommand() throws Exception {
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/and_or_command_test_input.txt";
        final Path outputFile = Files.createTempFile("and-or-command-output", ".txt");
        final String expected = Files.readString(
                Path.of("./src/test/java/com/sec/bestreviewer/AndOrParameterTest.testAndOrCommand.approved.txt"));

        new EmployeeManagement().run(new String[] {inputFileName, outputFile.toString()});

        assertEquals(normalizeLineSeparators(expected).stripTrailing(),
                normalizeLineSeparators(Files.readString(outputFile)).stripTrailing());
    }

    private String normalizeLineSeparators(String text) {
        return text.replace("\r\n", "\n");
    }
}
