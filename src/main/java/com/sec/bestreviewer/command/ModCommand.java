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
    public ModCommand(OptionParser optionParser, SearchCondition condition, Pair<String, String> conditionModifyPair) {
        super(optionParser, condition);
        this.conditionModifyPair = conditionModifyPair;
    }

    // AndOr
    public ModCommand(
            OptionParser firstOptionParser, SearchCondition firstCondition,
            CombinationEnum combinationEnum,
            OptionParser secondOptionParser, SearchCondition secondCondition, Pair<String, String> conditionModifyPair) {
        super(firstOptionParser, firstCondition, combinationEnum, secondOptionParser, secondCondition);
        this.conditionModifyPair = conditionModifyPair;
    }

    @Override
    public List<String> executeSingle(EmployeeStore employeeStore) {
        FieldEnum fieldModifyCondition = FieldEnum.fromFieldName(conditionModifyPair.first);

        List<Employee> employeeList = employeeStore.modify(
                firstCondition.getField(),
                firstCondition.getValue(),
                fieldModifyCondition,
                conditionModifyPair.second,
                firstCondition.getSubfieldIndex());

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

        FieldEnum fieldModifyCondition = FieldEnum.fromFieldName(conditionModifyPair.first);

        List<Employee> employeeList = employeeStore.modify(
                firstCondition.getField(), firstCondition.getValue(), firstCondition.getSubfieldIndex(),
                combinationEnum,
                secondCondition.getField(), secondCondition.getValue(), secondCondition.getSubfieldIndex(),
                fieldModifyCondition, conditionModifyPair.second);

        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_MOD, MAX_RESULT_NUMBER);
        }
        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_MOD);
    }
}
