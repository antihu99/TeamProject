package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.store.FieldEnum;
import com.sec.bestreviewer.util.*;

import java.util.List;

import static com.sec.bestreviewer.CommandExecutor.MAX_RESULT_NUMBER;

public class SearchCommand extends Command {
    // Single
    public SearchCommand(OptionParser optionParser, Pair<String, String> conditionPair) {
        super(optionParser, conditionPair);
    }

    // AndOr
    public SearchCommand(
            OptionParser firstOptionParser, Pair<String, String> firstConditionPair, CombinationEnum combinationEnum,
            OptionParser secondOptionParser, Pair<String, String> secondConditionPair) {
        super(firstOptionParser, firstConditionPair, combinationEnum, secondOptionParser, secondConditionPair);
    }

    @Override
    public List<String> executeSingle(EmployeeStore employeeStore) {

        FieldEnum fieldCondition = FieldEnum.fromFieldName(firstConditionPair.first);

        int fieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(firstOptionParser, fieldCondition);

        TertiaryOptionEnum tertiaryOptionEnum = firstOptionParser.getTertiaryOption();
        List<Employee> employeeList = employeeStore.search(fieldCondition, firstConditionPair.second, tertiaryOptionEnum, fieldIndex);

        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_SCH, MAX_RESULT_NUMBER);
        }

        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_SCH);
    }

    @Override
    protected List<String> executeAndOr(EmployeeStore employeeStore) {
        /**
         * AndOr인 경우를 이곳에 구현하세요.
         * 이 매소드가 호출된다면, Command 클래스에 있는 모든 매개변수를 호출할 수 있습니다.
         */

        System.out.println("[SearchCommand]");
        printAndOrValue();


        FieldEnum firstFieldCondition = FieldEnum.fromFieldName(firstConditionPair.first);
        FieldEnum secondFieldCondition = FieldEnum.fromFieldName(secondConditionPair.first);

        int firstFieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(firstOptionParser, firstFieldCondition);
        int secondFieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(secondOptionParser, secondFieldCondition);
        TertiaryOptionEnum firstTertiaryOptionEnum = firstOptionParser.getTertiaryOption();
        TertiaryOptionEnum secondTertiaryOptionEnum = secondOptionParser.getTertiaryOption();

        List<Employee> employeeList = employeeStore.search(firstFieldCondition, firstConditionPair.second, firstTertiaryOptionEnum, firstFieldIndex,
                combinationEnum,
                secondFieldCondition, secondConditionPair.second, secondTertiaryOptionEnum, secondFieldIndex );

        if (firstOptionParser.getPrimaryOption() == PrimaryOptionEnum.PRINT) {
            return ResultStringFormatter.getEmployeeListToFormattedString(
                    employeeList, CommandFactory.CMD_SCH, MAX_RESULT_NUMBER);
        }

        return ResultStringFormatter.getEmployeeListToFormattedString(employeeList, CommandFactory.CMD_SCH);
    }
}
