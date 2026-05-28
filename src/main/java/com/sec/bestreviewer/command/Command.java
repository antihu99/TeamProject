package com.sec.bestreviewer.command;

import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import com.sec.bestreviewer.util.SearchCondition;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    protected OptionParser firstOptionParser;
    protected SearchCondition firstCondition;
    protected CombinationEnum combinationEnum;
    protected OptionParser secondOptionParser;
    protected SearchCondition secondCondition;

    public Command() {}

    // Single
    public Command(OptionParser firstOptionParser, SearchCondition firstCondition) {
        this.firstOptionParser = firstOptionParser;
        this.firstCondition = firstCondition;

        // If Single Line, below data is Empty
        this.combinationEnum = CombinationEnum.NONE;
        this.secondOptionParser = new OptionParser(Arrays.asList(" ", " "));
        this.secondCondition = new SearchCondition(
                com.sec.bestreviewer.store.FieldEnum.FIELD_NAME, " ", 0);
    }

    // AndOr
    public Command(
            OptionParser firstOptionParser, SearchCondition firstCondition, CombinationEnum combinationEnum,
            OptionParser secondOptionParser, SearchCondition secondCondition) {
        this.firstOptionParser = firstOptionParser;
        this.firstCondition = firstCondition;
        this.combinationEnum = combinationEnum;
        this.secondOptionParser = secondOptionParser;
        this.secondCondition = secondCondition;
    }

    public List<String> execute(EmployeeStore employeeStore) {
        if(combinationEnum == CombinationEnum.AND || combinationEnum == CombinationEnum.OR) {
            return executeAndOr(employeeStore);
        }
        return executeSingle(employeeStore);
    }

    // Debug
    protected void printAndOrValue() {
        System.out.printf(
                firstOptionParser.getPrimaryOption() + ", " +
                firstOptionParser.getSecondaryOption() + ", " +
                firstOptionParser.getTertiaryOption());

        System.out.printf(", " + firstCondition.getField().getField() + " " + firstCondition.getValue());

        System.out.printf(", " + combinationEnum);

        System.out.printf(", " + secondOptionParser.getPrimaryOption() + ", " +
                secondOptionParser.getSecondaryOption() + ", " +
                secondOptionParser.getTertiaryOption());

        System.out.printf(", " + secondCondition.getField().getField() + ", " + secondCondition.getValue());
        System.out.println("");
    }

    /**
     * Template Method Pattern
     */
    protected List<String> executeSingle(EmployeeStore employeeStore) {
        // Not Use
        return List.of();
    }

    protected List<String> executeAndOr(EmployeeStore employeeStore) {
        // Not Use
        return List.of();
    }
}
