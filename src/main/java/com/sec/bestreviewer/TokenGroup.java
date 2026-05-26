package com.sec.bestreviewer;

import com.sec.bestreviewer.command.CombinationEnum;

import java.util.List;

public class TokenGroup {
    private String type;
    private List<String> firstOptions;
    private List<String> firstParams;
    private String combination; // -a, -o
    private List<String> secondOptions;
    private List<String> secondParams;

    // Single
    TokenGroup(String type, List<String> options, List<String> params) {
        this.type = type;
        this.firstOptions = options;
        this.firstParams = params;
        this.combination = " ";
    }

    // AndOr
    TokenGroup(String type,
               List<String> firstOptions, List<String> firstParams,
               String combination,
               List<String> secondOptions, List<String> secondParams) {

        this.type = type;
        this.firstOptions = firstOptions;
        this.firstParams = firstParams;
        this.combination = combination;
        this.secondOptions = secondOptions;
        this.secondParams = secondParams;
    }

    String getType() {
        return type;
    }

    List<String> getOptions() {
        return getFirstOptions();
    }

    List<String> getParams() {
        return getFirstParams();
    }

    CombinationEnum getCombinationEnum() {
        return CombinationEnum.fromCombination(combination);
    }


    List<String> getFirstOptions() {
        return firstOptions;
    }

    List<String> getFirstParams() {
        return firstParams;
    }

    String getCombination() {
        return combination;
    }

    List<String> getSecondOptions() {
        return secondOptions;
    }

    List<String> getSecondParams() {
        return secondParams;
    }





}
