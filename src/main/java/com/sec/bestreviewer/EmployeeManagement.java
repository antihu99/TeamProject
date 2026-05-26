package com.sec.bestreviewer;

import com.sec.bestreviewer.command.Command;

import java.util.Arrays;
import java.util.List;

public class EmployeeManagement  {
    public EmployeeManagement() {
    }

    public void run(String[] args) {
        if (args.length < 2) {
            printUsage();
            throw new IllegalArgumentException("wrong arguments count");
        }

        String inputFileName = args[0];
        String outputFilename = args[1];

        CommandReader commandReader = new CommandReader(inputFileName);
        CommandParser commandParser = new CommandParser();
        CommandFactory commandFactory = new CommandFactory();
        CommandExecutor commandExecutor = new CommandExecutor();
        Printer printer = new Printer(outputFilename);

        List<String> input = commandReader.readFile();

        for (String line : input) {
            try {
                TokenGroup tokenGroup = commandParser.parse(line);
                Command command = commandFactory.buildCommand(tokenGroup);
                List<String> result = commandExecutor.execute(command);
                printer.add(result);
            } catch (IllegalArgumentException exception) {
                printer.add(Arrays.asList("wrong command : " + line));
            }
        }

        printer.printOutputFile();
    }


    private void printUsage() {
        System.out.println("input / output 형태는 txt 파일이며, 아래 command 로 input file 을 read 하여, output file 을 생성한다.");
        System.out.println("Usage : EmployeeManagement input.txt output.txt");
    }

    public static void main(String[] args) {
        EmployeeManagement employeeManagement = new EmployeeManagement();
        employeeManagement.run(args);
    }
}
