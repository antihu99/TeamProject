package com.sec.bestreviewer.util;

import com.sec.bestreviewer.store.FieldEnum;

/**
 * SCH/DEL/MOD 검색 조건: 필드, 원문 값, 부분 필드 인덱스를 한 곳에서 표현한다.
 */
public class SearchCondition {
    private final FieldEnum field;
    private final String value;
    private final int subfieldIndex;

    public SearchCondition(FieldEnum field, String value, int subfieldIndex) {
        this.field = field;
        this.value = value;
        this.subfieldIndex = subfieldIndex;
    }

    public static SearchCondition from(String internalFieldName, String rawValue, OptionParser optionParser) {
        FieldEnum field = FieldEnum.fromFieldName(internalFieldName);
        int subfieldIndex = SecondaryOptionEnum.getFieldIndexFromOption(optionParser, field);
        return new SearchCondition(field, rawValue, subfieldIndex);
    }

    public FieldEnum getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public int getSubfieldIndex() {
        return subfieldIndex;
    }
}
