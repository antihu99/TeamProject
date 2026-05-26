package com.sec.bestreviewer.util;

import com.sec.bestreviewer.store.FieldEnum;

public enum SecondaryOptionEnum {
    NONE(0, " "),
    F(1, "-f"),
    L(2, "-l"),
    M(3, "-m"),
    Y(4, "-y"),
    //MONTH(5, "-m"),
    D(5, "-d");

    private final int type;
    private final String option;

    SecondaryOptionEnum(int type, String option) {
        this.type = type;
        this.option = option;
    }

    public int getType() {
        return type;
    }

    public String getOption() {
        return option;
    }

    public static SecondaryOptionEnum fromOption(String option) {
        for (SecondaryOptionEnum secondary : SecondaryOptionEnum.values()) {
            if (secondary.getOption().equals(option)) {
                return secondary;
            }
        }
        throw new IllegalArgumentException("Invalid secondary option: " + option);
    }

    public static int getFieldIndexFromOption(OptionParser optionParser, FieldEnum inputFieldEnum ) {
        FieldEnum fieldCondition = inputFieldEnum;
        SecondaryOptionEnum secondaryOption = optionParser.getSecondaryOption();
        int fieldIndex = 0;
        switch (secondaryOption) {
            case F:
                fieldIndex = 1;
                break;
            case M:
                if (fieldCondition.equals(FieldEnum.FIELD_PHONE_NUMBER)) {
                    fieldIndex = 2;
                } else if (fieldCondition.equals(FieldEnum.FIELD_BIRTH_DAY)) {
                    fieldIndex = 2;
                }
                break;
            case L:
                if (fieldCondition.equals(FieldEnum.FIELD_PHONE_NUMBER)) {
                    fieldIndex = 3;
                } else if (fieldCondition.equals(FieldEnum.FIELD_NAME)) {
                    fieldIndex = 2;
                }
                break;
            case Y:
                if (fieldCondition.equals(FieldEnum.FIELD_BIRTH_DAY)) {
                    fieldIndex = 1;
                }
                break;
            case D:
                if (fieldCondition.equals(FieldEnum.FIELD_BIRTH_DAY)) {
                    fieldIndex = 3;
                }
                break;
            case NONE:
                break;
            default:
                System.out.println("Invalid secondary option : " + secondaryOption.getOption());
                break;
        }
        return fieldIndex;
    }

}
