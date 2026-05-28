package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;

public class PhoneNumber extends Field {
    private String phoneNumber;
    private String first;
    private String middle;
    private String last;

    public PhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 13) {
            throw new IllegalArgumentException("Invalid PhoneNumber format: " + phoneNumber);
        }
        this.phoneNumber = phoneNumber;
        String[] split = phoneNumber.split("-");
        if (split.length != 3) {
            throw new IllegalArgumentException("Invalid PhoneNumber split: " + phoneNumber);
        }
        this.first = split[0];
        this.middle = split[1];
        this.last = split[2];
    }

    public String getFirst() {
        return first;
    }

    public String getMiddle() {
        return middle;
    }

    public String getLast() {
        return last;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return first + "-" + middle + "-" + last;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) return false;
        PhoneNumber that = (PhoneNumber) obj;
        return first.equals(that.first) && middle.equals(that.middle) && last.equals(that.last);
    }

    @Override
    public boolean equals(String number) {
        PhoneNumber that = new PhoneNumber(number);
        return first.equals(that.first) && middle.equals(that.middle) && last.equals(that.last);
    }

    @Override
    public int compareTo(Field o) {
        if (!(o instanceof PhoneNumber)) throw new IllegalArgumentException("Invalid type");
        PhoneNumber target = (PhoneNumber) o;
        return phoneNumber.compareTo(target.getPhoneNumber());
    }

    public boolean equalsMiddle(String number) {
        return middle.equals(number);
    }

    public boolean equalsLast(String number) {
        return last.equals(number);
    }

    public boolean matchesSubfield(int subfieldIndex, String rawValue, TertiaryOptionEnum tertiaryOptionEnum) {
        if (subfieldIndex == 2) {
            return compareString(this.middle, rawValue, tertiaryOptionEnum);
        }
        if (subfieldIndex == 3) {
            return compareString(this.last, rawValue, tertiaryOptionEnum);
        }
        throw new IllegalArgumentException("Subfield index out of bounds! index=" + subfieldIndex);
    }

    public boolean compare(Field field, int subfieldIndex, TertiaryOptionEnum tertiaryOptionEnum) {
        PhoneNumber fieldPhoneNumber = (PhoneNumber) field;
        if (subfieldIndex == 2) {
            return compareString(this.middle, fieldPhoneNumber.getMiddle(), tertiaryOptionEnum);
        } else if (subfieldIndex == 3) {
            return compareString(this.last, fieldPhoneNumber.getLast(), tertiaryOptionEnum);
        } else {
            return compareString(this.phoneNumber, fieldPhoneNumber.getPhoneNumber(), tertiaryOptionEnum);
        }
    }

}
