package com.sec.bestreviewer.util;

public enum TertiaryOptionEnum {
    NONE(0, " "),
    G(1, "-g"),
    GE(2, "-ge"),
    S(3, "-s"),
    SE(4, "-se");


    private final int type;
    private final String option;

    TertiaryOptionEnum(int type, String option) {
        this.type = type;
        this.option = option;
    }

    public int getType() {
        return type;
    }

    public String getOption() {
        return option;
    }

    public static TertiaryOptionEnum fromOption(String option) {
        for (TertiaryOptionEnum tertiary : TertiaryOptionEnum.values()) {
            if (tertiary.getOption().equals(option)) {
                return tertiary;
            }
        }
        throw new IllegalArgumentException("Invalid tertiary option: " + option);
    }

}
