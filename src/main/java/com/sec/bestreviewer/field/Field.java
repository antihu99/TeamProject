package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;

public abstract class Field implements Comparable<Field> {
    abstract public boolean compare(Field field, int subfieldIndex, TertiaryOptionEnum tertiaryOptionEnum);
    abstract boolean equals(String string);

    protected boolean compareString(String firstString, String secondString, TertiaryOptionEnum tertiaryOptionEnum) {
        int compareResult = firstString.compareTo(secondString);
        System.out.println("first:" + firstString + ", second:"+secondString+ ", result: "+compareResult);
        if (tertiaryOptionEnum == TertiaryOptionEnum.G) {
            return compareResult > 0;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.GE) {
            return compareResult >= 0;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.NONE) {
            return compareResult == 0;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.SE) {
            return compareResult <= 0;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.S) {
            return compareResult < 0;
        }
        return false;
    }

    protected boolean compareInt(int firstInt, int secondInt, TertiaryOptionEnum tertiaryOptionEnum) {
        if (tertiaryOptionEnum == TertiaryOptionEnum.G) {
            return firstInt > secondInt;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.GE) {
            return firstInt >= secondInt;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.NONE) {
            return firstInt == secondInt;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.SE) {
            return firstInt <= secondInt;
        } else if (tertiaryOptionEnum == TertiaryOptionEnum.S) {
            return firstInt < secondInt;
        }
        return false;
    }
}
