package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;

import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.*;

import java.util.List;

import static com.sec.bestreviewer.CommandExecutor.MAX_RESULT_NUMBER;

public class DeleteCommand extends Command {
    // Single
    public DeleteCommand(OptionParser optionParser, SearchCondition condition) {
        super(optionParser, condition);
    }

    // AndOr
    public DeleteCommand(
            OptionParser firstOptionParser, SearchCondition firstCondition, CombinationEnum combinationEnum,
            OptionParser secondOptionParser, SearchCondition secondCondition) {
        super(firstOptionParser, firstCondition, combinationEnum, secondOptionParser, secondCondition);
    }

    @Override
    public List<String> executeSingle(EmployeeStore employeeStore) {
        List<Employee> employeeList = employeeStore.delete(
                firstCondition.getField(),
                firstCondition.getValue(),
                firstCondition.getSubfieldIndex());

        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_DEL, MAX_RESULT_NUMBER);
        }

        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_DEL);
    }

    @Override
    protected List<String> executeAndOr(EmployeeStore employeeStore) {
        /**
         * AndOr인 경우를 이곳에 구현하세요.
         * 이 매소드가 호출된다면, Command 클래스에 있는 모든 매개변수를 호출할 수 있습니다.
         */
        System.out.println("[DeleteCommand]");
        printAndOrValue();

        List<Employee> employeeList = employeeStore.delete(
                firstCondition.getField(), firstCondition.getValue(), firstCondition.getSubfieldIndex(),
                combinationEnum,
                secondCondition.getField(), secondCondition.getValue(), secondCondition.getSubfieldIndex());

        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_DEL, MAX_RESULT_NUMBER);
        }

        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_DEL);
    }
}
