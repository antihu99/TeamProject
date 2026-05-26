package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;


/**
 * CareerLevel 필드 클래스
 * - subfieldIndex: 0만 사용 (직급 전체)
 * - tertiaryOptionEnum: TertiaryOptionEnum (G, GE, S, SE, NONE)
 */
public class CareerLevel extends Field {
    private final String value; // 예: "CL3"
    private final int levelNum; // 예: 3

    public CareerLevel(String value) {
        if (!value.matches("CL[1-4]")) {
            throw new IllegalArgumentException("Invalid career level: " + value);
        }
        this.value = value;
        this.levelNum = Integer.parseInt(value.substring(2)); // "CL3" -> 3
    }

    public String getValue() { return value; }
    public int getLevelNum() { return levelNum; }

    @Override
    public int compareTo(Field o) {
        if (!(o instanceof CareerLevel)) throw new IllegalArgumentException("Invalid type");
        return Integer.compare(this.levelNum, ((CareerLevel) o).levelNum);
    }

    /**
     * compare: 직급 전체(0)만 지원, compareInt 유틸 활용
     */
    @Override
    public boolean compare(Field field, int subfieldIndex, TertiaryOptionEnum tertiaryOptionEnum) {
        if (!(field instanceof CareerLevel))
            throw new IllegalArgumentException("field must be CareerLevel");
        if (subfieldIndex != 0)
            throw new IllegalArgumentException("CareerLevel only supports subfieldIndex == 0");

        return compareInt(this.levelNum, ((CareerLevel) field).levelNum,tertiaryOptionEnum);
    }

    /**
     * equals(String): value("CL3")와 비교
     */
    @Override
    public boolean equals(String string) {
        return this.value.equals(string);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CareerLevel)) return false;
        CareerLevel other = (CareerLevel) obj;
        return this.levelNum == other.levelNum;
    }

    @Override
    public String toString() {
        return value;
    }
}