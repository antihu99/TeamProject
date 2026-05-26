package com.sec.bestreviewer.util;

public enum PrimaryOptionEnum {
    NONE(0, " "),
    PRINT(1, "-p");

    private final int type;
    private final String option;

    PrimaryOptionEnum(int type, String option) {
        this.type = type;
        this.option = option;
    }

    public int getType() {
        return type;
    }

    public String getOption() {
        return option;
    }

    public static PrimaryOptionEnum fromOption(String option) {
        for (PrimaryOptionEnum primary : PrimaryOptionEnum.values()) {
            if (primary.getOption().equals(option)) {
                return primary;
            }
        }
        throw new IllegalArgumentException("Invalid primary option: " + option);
    }

}
