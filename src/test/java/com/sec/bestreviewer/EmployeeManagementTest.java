package com.sec.bestreviewer;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.Format;

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
    @Disabled
    public void integrationTest() throws Exception {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/integration_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/integration_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        employeeManagement.run(args);

        Approvals.verify(outputFile);
    }

    @Test
    @Disabled
    public void testOptions() throws Exception {
        final String outputFileName = "./src/test/java/com/sec/bestreviewer/option_name_test_output.txt";
        final String inputFileName = "./src/test/java/com/sec/bestreviewer/option_name_test_input.txt";

        String[] args = {inputFileName, outputFileName};
        File outputFile = new File(outputFileName);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        employeeManagement.run(args);

        Approvals.verify(outputFile);
    }
}
