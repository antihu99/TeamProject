package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;

public class EmployeeNumber extends Field{
    private String number;
    private String year;
    private String mod;

    public EmployeeNumber(String EmployeeNumber) {
        this.number = EmployeeNumber;
        this.year = EmployeeNumber.substring(0,2);
        this.mod = EmployeeNumber.substring(2,8);
    }

    public int getYear() {
        return Integer.parseInt(year);
    }

    public int getMod() {
        return Integer.parseInt(mod);
    }

    @Override
    public String toString() {
        return number;
    }

    @Override
    public boolean compare(Field field, int subfieldIndex, TertiaryOptionEnum tertiaryOptionEnum) {
        EmployeeNumber standard = (EmployeeNumber) field;
        int standardYear = standard.getYear();
        int standardMod = standard.getMod();
        int year = getYear();
        int mod = getMod();

        if(standardYear <= 19) standardYear += 100;
        if(year <= 19) year += 100;

        if (year == standardYear) {
            return compareInt(mod, standardMod, tertiaryOptionEnum);
        }
        return compareInt(year, standardYear, tertiaryOptionEnum);
    }

    @Override
    public int compareTo(Field o) {
        return number.compareTo(((EmployeeNumber) o).toString());
    }

    @Override
    public boolean equals(String string) {
        return number.equals(string);
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) return false;
        EmployeeNumber that = (EmployeeNumber) obj;
        return number.equals(that.toString());
    }
}
