package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;

public class Name extends Field {
    final private String first;
    final private String second;


    public Name(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public Name(String name) {
        String[] splitName = name.split(" ");
        this.first = splitName[0];
        this.second = splitName[1];
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return first + " " + second;
    }

    @Override
    public boolean equals(Object obj) {
        if(getClass() != obj.getClass()) throw new IllegalArgumentException("Object is not a Name object!");
        return this.first.equals(((Name) obj).getFirst()) && this.second.equals(((Name) obj).getSecond());
    }

    @Override
    public boolean equals(String string) {
        Name name = new Name(string);
        return this.equals(name);
    }

    @Override
    public boolean compare(Field field, int subfieldIndex, TertiaryOptionEnum tertiaryOptionEnum) {
        switch (subfieldIndex) {
            case 0: {
                if (this.first.equals(((Name) field).getFirst())) {
                    return compareString(this.second, ((Name) field).getSecond(), tertiaryOptionEnum);
                } else {
                    return compareString(this.first, ((Name) field).getFirst(), tertiaryOptionEnum);
                }
            }
            case 1:
                return compareString(this.first, ((Name) field).getFirst(), tertiaryOptionEnum);
            case 2:
                return compareString(this.second, ((Name) field).getSecond(), tertiaryOptionEnum);
            default:
                throw new IllegalArgumentException("Subfield index out of bounds! index=" + subfieldIndex);

        }
    }

    @Override
    public int compareTo(Field field) {
        if (this.first.equals(((Name) field).getFirst())) {
            return this.second.compareTo(((Name) field).getSecond());
        } else {
            return this.first.compareTo(((Name) field).getFirst());
        }
    }
}
