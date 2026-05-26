package com.sec.bestreviewer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeManagementTest {
    @Test
    public void testArgumentsEmptyInputFile() throws Exception {
        String[] args = {"./src/test/java/com/sec/bestreviewer/empty.txt", "./src/test/java/com/sec/bestreviewer/output.txt"};

        EmployeeManagement employeeManagement = new EmployeeManagement();
        employeeManagement.run(args);

        File file = new File("./src/test/java/com/sec/bestreviewer/output.txt");

        assertTrue(file.exists());
    }

    @Test
    public void testArgumentsWrongArgsCount() throws Exception {
        String[] args = {"input.txt"};

        EmployeeManagement employeeManagement = new EmployeeManagement();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            employeeManagement.run(args);
        });
    }

    @Test
    public void testArgumentsNotExistInputFile() throws Exception {
        String[] args = {"notexist.txt", "output.txt"};

        EmployeeManagement employeeManagement = new EmployeeManagement();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            employeeManagement.run(args);
        });
    }

    @Test
    public void testOptions() throws Exception {
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/option_name_test_input.txt";
        final Path outputFile = Files.createTempFile("employee-management-options", ".txt");
        final String expected = Files.readString(
                Path.of("./src/test/java/com/sec/bestreviewer/EmployeeManagementTest.testOptions.approved.txt"));

        EmployeeManagement employeeManagement = new EmployeeManagement();

        employeeManagement.run(new String[] {inputFileName, outputFile.toString()});

        assertEquals(normalizeLineSeparators(expected).stripTrailing(),
                normalizeLineSeparators(Files.readString(outputFile)).stripTrailing());
    }

    @Test
    public void malformedCommandIsWrittenToOutputFile() throws Exception {
        final Path inputFile = Files.createTempFile("employee-management-malformed-input", ".txt");
        final Path outputFile = Files.createTempFile("employee-management-malformed-output", ".txt");
        final String malformedCommand = "BROKEN, , , ,name,ABC";

        Files.writeString(inputFile, malformedCommand);

        EmployeeManagement employeeManagement = new EmployeeManagement();
        employeeManagement.run(new String[] {inputFile.toString(), outputFile.toString()});

        assertEquals("wrong command : " + malformedCommand,
                normalizeLineSeparators(Files.readString(outputFile)).stripTrailing());
    }

    private String normalizeLineSeparators(String text) {
        return text.replace("\r\n", "\n");
    }
}
