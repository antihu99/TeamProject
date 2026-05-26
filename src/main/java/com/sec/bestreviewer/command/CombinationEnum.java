package com.sec.bestreviewer.command;

import com.sec.bestreviewer.store.FieldEnum;

public enum CombinationEnum {
    NONE(" "),
    OR("-o"),
    AND("-a");

    private final String combination;

    CombinationEnum(String combination) {
        this.combination = combination;
    }

    public String getCombination() {
        return combination;
    }

    public static CombinationEnum fromCombination(String combination) {
        for (CombinationEnum combinationEnum : CombinationEnum.values()) {
            if (combinationEnum.getCombination().equals(combination)) {
                return combinationEnum;
            }
        }
        throw new IllegalArgumentException("Invalid combination name: " + combination);
    }
}
