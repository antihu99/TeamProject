package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.store.FieldEnum;
import com.sec.bestreviewer.util.*;

import java.util.List;

import static com.sec.bestreviewer.CommandExecutor.MAX_RESULT_NUMBER;

public class ModCommand extends Command {
    private final Pair<String, String> conditionModifyPair;

    // Single
    public ModCommand(OptionParser optionParser, Pair<String, String> conditionPair, Pair<String, String> conditionModifyPair) {
        super(optionParser, conditionPair);
        this.conditionModifyPair = conditionModifyPair;
    }

    // AndOr
    public ModCommand(
            OptionParser firstOptionParser, Pair<String, String> firstConditionPair,
            CombinationEnum combinationEnum,
            OptionParser secondOptionParser, Pair<String, String> secondConditionPair, Pair<String, String> conditionModifyPair) {
        super(firstOptionParser, firstConditionPair, combinationEnum, secondOptionParser, secondConditionPair);
        this.conditionModifyPair = conditionModifyPair;
    }

    @Override
    public List<String> executeSingle(EmployeeStore employeeStore) {

        FieldEnum fieldCondition = FieldEnum.fromFieldName(firstConditionPair.first);
        FieldEnum fieldModifyCondition = FieldEnum.fromFieldName(conditionModifyPair.first);

        int fieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(firstOptionParser, fieldCondition);

        List<Employee> employeeList;
        if (fieldIndex == 0) {
            employeeList = employeeStore.modify(fieldCondition, firstConditionPair.second, fieldModifyCondition, conditionModifyPair.second);
            if (employeeList == null || employeeList.isEmpty()) {
                employeeList = employeeStore.modify(fieldCondition, firstConditionPair.second, fieldModifyCondition, conditionModifyPair.second, fieldIndex);
            }
        } else {
            employeeList = employeeStore.modify(fieldCondition, firstConditionPair.second, fieldModifyCondition, conditionModifyPair.second, fieldIndex);
        }

        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_MOD, MAX_RESULT_NUMBER);
        }
        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_MOD);
    }

    @Override
    protected List<String> executeAndOr(EmployeeStore employeeStore) {
        /**
         * AndOr인 경우를 이곳에 구현하세요.
         * 이 매소드가 호출된다면, Command 클래스에 있는 모든 매개변수를 호출할 수 있습니다.
         */
        System.out.println("[MODCommand]");
        printAndOrValue();
        System.out.printf(", " + conditionModifyPair.first + ", " + conditionModifyPair.second);

        FieldEnum firstFieldCondition = FieldEnum.fromFieldName(firstConditionPair.first);
        FieldEnum secondFieldCondition = FieldEnum.fromFieldName(secondConditionPair.first);
        FieldEnum fieldModifyCondition = FieldEnum.fromFieldName(conditionModifyPair.first);

        int firstFieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(firstOptionParser, firstFieldCondition);
        int secondFieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(secondOptionParser, secondFieldCondition);

        List<Employee> employeeList = employeeStore.modify(firstFieldCondition, firstConditionPair.second, firstFieldIndex,
                combinationEnum,
                secondFieldCondition, secondConditionPair.second, secondFieldIndex, fieldModifyCondition, conditionModifyPair.second);


        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_MOD, MAX_RESULT_NUMBER);
        }
        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_MOD);
    }
}
