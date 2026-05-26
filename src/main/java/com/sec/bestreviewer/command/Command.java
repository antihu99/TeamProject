package com.sec.bestreviewer.command;

import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import com.sec.bestreviewer.util.Pair;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    protected OptionParser firstOptionParser;
    protected Pair<String, String> firstConditionPair;
    protected CombinationEnum combinationEnum;
    protected OptionParser secondOptionParser;
    protected Pair<String, String> secondConditionPair;

    public Command() {}

    // Single
    public Command(OptionParser firstOptionParser, Pair<String, String> firstConditionPair) {
        this.firstOptionParser = firstOptionParser;
        this.firstConditionPair = firstConditionPair;

        // If Single Line, below data is Empty
        this.combinationEnum = CombinationEnum.NONE;
        this.secondOptionParser = new OptionParser(Arrays.asList(" ", " "));
        this.secondConditionPair = Pair.create(" ", " ");
    }

    // AndOr
    public Command(
            OptionParser firstOptionParser, Pair<String, String> firstConditionPair, CombinationEnum combinationEnum,
            OptionParser secondOptionParser, Pair<String, String> secondConditionPair) {
        this.firstOptionParser = firstOptionParser;
        this.firstConditionPair = firstConditionPair;
        this.combinationEnum = combinationEnum;
        this.secondOptionParser = secondOptionParser;
        this.secondConditionPair = secondConditionPair;
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

        System.out.printf(", " + firstConditionPair.first + " " +firstConditionPair.second);

        System.out.printf(", " + combinationEnum);

        System.out.printf(", " + secondOptionParser.getPrimaryOption() + ", " +
                secondOptionParser.getSecondaryOption() + ", " +
                secondOptionParser.getTertiaryOption());

        System.out.printf(", " + secondConditionPair.first + ", " +secondConditionPair.second);
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
