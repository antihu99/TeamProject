package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;

import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.store.FieldEnum;
import com.sec.bestreviewer.util.*;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;

import static com.sec.bestreviewer.CommandExecutor.MAX_RESULT_NUMBER;

public class DeleteCommand extends Command {
    // Single
    public DeleteCommand(OptionParser optionParser, Pair<String, String> conditionPair) {
        super(optionParser, conditionPair);
    }

    // AndOr
    public DeleteCommand(
            OptionParser firstOptionParser, Pair<String, String> firstConditionPair, CombinationEnum combinationEnum,
            OptionParser secondOptionParser, Pair<String, String> secondConditionPair) {
        super(firstOptionParser, firstConditionPair, combinationEnum, secondOptionParser, secondConditionPair);
    }

    @Override
    public List<String> executeSingle(EmployeeStore employeeStore) {
        FieldEnum fieldCondition = FieldEnum.fromFieldName(firstConditionPair.first);

        int fieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(firstOptionParser, fieldCondition);

        List<Employee> employeeList;
        if (fieldIndex == 0) {
            employeeList = employeeStore.delete(fieldCondition, firstConditionPair.second);
            if (employeeList == null || employeeList.isEmpty()) {
                employeeList = employeeStore.delete(fieldCondition, firstConditionPair.second, fieldIndex);
            }
        } else {
            employeeList = employeeStore.delete(fieldCondition, firstConditionPair.second, fieldIndex);
        }

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

        FieldEnum firstFieldCondition = FieldEnum.fromFieldName(firstConditionPair.first);
        FieldEnum secondFieldCondition = FieldEnum.fromFieldName(secondConditionPair.first);

        int firstFieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(firstOptionParser, firstFieldCondition);
        int secondFieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(secondOptionParser, secondFieldCondition);

        List<Employee> employeeList = employeeStore.delete(firstFieldCondition, firstConditionPair.second, firstFieldIndex,
                combinationEnum,
                secondFieldCondition, secondConditionPair.second, secondFieldIndex);

        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_DEL, MAX_RESULT_NUMBER);
        }

        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_DEL);
    }
}
