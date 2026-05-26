package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;

import java.util.Objects;

/**
 * Birthday 필드 클래스
 * - subfieldIndex:
 *     0: 전체 (YYYYMMDD)
 *     1: 연도(year)
 *     2: 월(month)
 *     3: 일(day)
 * - tertiaryOptionEnum: TertiaryOptionEnum (G, GE, S, SE, NONE=동등)
 */
public class Birthday extends Field {
    private final int year;
    private final int month;
    private final int day;

    public Birthday(String birthday) {
        if (birthday == null || birthday.length() != 8) {
            throw new IllegalArgumentException("Invalid birthday format: " + birthday);
        }
        this.year = Integer.parseInt(birthday.substring(0, 4));
        this.month = Integer.parseInt(birthday.substring(4, 6));
        this.day = Integer.parseInt(birthday.substring(6, 8));
    }

    public Birthday(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() { return this.year; }
    public int getMonth() { return this.month; }
    public int getDay() { return this.day; }

    /**
     * compareTo: 전체 날짜 비교 (Comparable<Field> 구현)
     */
    @Override
    public int compareTo(Field o) {
        if (!(o instanceof Birthday)) {
            throw new IllegalArgumentException("Cannot compare Birthday with " + o.getClass().getSimpleName());
        }
        Birthday other = (Birthday) o;
        if (year != other.year) return Integer.compare(year, other.year);
        if (month != other.month) return Integer.compare(month, other.month);
        return Integer.compare(day, other.day);
    }

    /**
     * compare: 옵션(subfieldIndex, tertiaryOptionEnum)에 따라 부등호 비교
     */
    @Override
    public boolean compare(Field field, int subfieldIndex, TertiaryOptionEnum tertiaryOptionEnum) {
        if (!(field instanceof Birthday)) throw new IllegalArgumentException("타입 불일치");
        Birthday conditionValue = (Birthday) field;

        int dataValue, searchValue;
        switch (subfieldIndex) {
            case 0: // 전체 날짜(YYYYMMDD)
                dataValue = this.toInt();               // DB에 저장된 값(YYYYMMDD)
                searchValue = conditionValue.toInt();   // 검색 조건 값(YYYYMMDD)
                break;
            case 1: // 연도
                dataValue = this.year;
                searchValue = conditionValue.year;
                break;
            case 2: // 월
                dataValue = this.month;
                searchValue = conditionValue.month;
                break;
            case 3: // 일
                dataValue = this.day;
                searchValue = conditionValue.day;
                break;
            default:
                throw new IllegalArgumentException("잘못된 subfieldIndex: " + subfieldIndex);
        }
        return compareInt(dataValue, searchValue, tertiaryOptionEnum);
    }

    /**
     * equals(String): 스트링과 값 비교 (ex. "19981206")
     */
    @Override
    public boolean equals(String string) {
        return this.toString().equals(string);
    }

    /**
     * equals(Object): 오버라이드 (year, month, day 값이 모두 동일하면 true)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Birthday)) return false;
        Birthday other = (Birthday) obj;
        return year == other.year && month == other.month && day == other.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    /**
     * YYYYMMDD int 값으로 변환
     */
    private int toInt() {
        return year * 10000 + month * 100 + day;
    }

    @Override
    public String toString() {
        return String.format("%04d%02d%02d", year, month, day);
    }
}