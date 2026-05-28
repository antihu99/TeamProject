package com.sec.bestreviewer;

import com.sec.bestreviewer.command.*;
import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import com.sec.bestreviewer.util.Pair;
import com.sec.bestreviewer.util.SearchCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactory {
    static final String CMD_ADD = "ADD";
    public static final String CMD_DEL = "DEL";
    public static final String CMD_SCH = "SCH";
    public static final String CMD_CNT = "CNT";
    public static final String CMD_MOD = "MOD";

    private static final String EMPLOYEE_NUMBER = "employeeNum";
    private static final String NAME = "name";
    private static final String CAREER_LEVEL = "cl";
    private static final String PHONE_NUMBER = "phoneNum";
    private static final String BIRTHDAY = "birthday";
    private static final String CERTI = "certi";

    private static final Map<String, String> fieldMap = new HashMap<>();

    static {
        fieldMap.put(EMPLOYEE_NUMBER, EmployeeStore.FIELD_EMPLOYEE_NUMBER);
        fieldMap.put(NAME, EmployeeStore.FIELD_NAME);
        fieldMap.put(CAREER_LEVEL, EmployeeStore.FIELD_CAREER_LEVEL);
        fieldMap.put(PHONE_NUMBER, EmployeeStore.FIELD_PHONE_NUMBER);
        fieldMap.put(BIRTHDAY, EmployeeStore.FIELD_BIRTH_DAY);
        fieldMap.put(CERTI, EmployeeStore.FIELD_CERTI);
    }

    public static Command buildCommand(TokenGroup tokens) throws IllegalArgumentException {
        if(tokens.getCombinationEnum() == CombinationEnum.NONE) {
            return buildSingleCommand(tokens.getType(), tokens.getOptions(), tokens.getParams());
        }

        return buildAndOrCommand(tokens);
    }

    static Command buildSingleCommand(String cmd, List<String> options, List<String> params)
            throws IllegalArgumentException {
        final OptionParser optionParser = new OptionParser(options);

        switch (cmd) {
            case CMD_ADD:
                final Employee employee =
                        new Employee(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5));
                return new AddCommand(optionParser, employee);
            case CMD_DEL:
                return new DeleteCommand(optionParser, createSearchCondition(params, optionParser));
            case CMD_MOD:
                return new ModCommand(optionParser, createSearchCondition(params, optionParser), getConditionMapFromModifyParams(params, optionParser));
            case CMD_SCH:
                return new SearchCommand(optionParser, createSearchCondition(params, optionParser));
            case CMD_CNT:
                return new CountCommand();
        }
        throw new IllegalArgumentException("Wrong command");
    }

    static Command buildAndOrCommand(TokenGroup tokens) {
        String cmd = tokens.getType();

        // Param
        List<String> firstParams = tokens.getFirstParams();
        List<String> secondParams = tokens.getSecondParams();

        // Combination(And Or)
        CombinationEnum combination = tokens.getCombinationEnum();

        // Option
        List<String> firstOptions = tokens.getFirstOptions();
        List<String> secondOptions = tokens.getSecondOptions();
        OptionParser firstOptionParser = new OptionParser(firstOptions);
        OptionParser secondOptionParser = new OptionParser(secondOptions);
        
        switch (cmd) {
            case CMD_ADD:
                throw new IllegalArgumentException("And Or Line must not have ADD.");
            case CMD_DEL:
                return new DeleteCommand(
                        firstOptionParser, createSearchCondition(firstParams, firstOptionParser), combination,
                        secondOptionParser, createSearchCondition(secondParams, secondOptionParser)
                );
            case CMD_MOD:
                return new ModCommand(
                        firstOptionParser, createSearchCondition(firstParams, firstOptionParser), combination,
                        secondOptionParser, createSearchCondition(secondParams, secondOptionParser),
                        getConditionMapFromModifyParams(secondParams, secondOptionParser)
                );

            case CMD_SCH:
                return new SearchCommand(
                        firstOptionParser, createSearchCondition(firstParams, firstOptionParser), combination,
                        secondOptionParser, createSearchCondition(secondParams, secondOptionParser)
                );
            case CMD_CNT:
                return new CountCommand();
        }
        throw new IllegalArgumentException("Wrong command");
    }

    private static SearchCondition createSearchCondition(List<String> params, OptionParser optionParser) {
        final String fieldName = fieldMap.get(params.get(0));
        if (fieldName == null) {
            throw new IllegalArgumentException("Wrong field: " + params.get(0));
        }
        return SearchCondition.from(fieldName, params.get(1), optionParser);
    }

    private static Pair<String, String> getConditionMapFromModifyParams(List<String> params, OptionParser optionParser) {
        final String fieldName = fieldMap.get(params.get(2));
        if (fieldName == null || fieldName.equals(EmployeeStore.FIELD_EMPLOYEE_NUMBER)) {
            throw new IllegalArgumentException("Wrong field: " + params.get(2));
        }
        return Pair.create(fieldMap.get(params.get(2)), params.get(3));
    }

    public static String getFieldMapParam(String param) {
        final String fieldName = fieldMap.get(param);
        if (fieldName == null) {
            throw new IllegalArgumentException("Wrong field: " + param);
        }
        return fieldName;
    }
}
