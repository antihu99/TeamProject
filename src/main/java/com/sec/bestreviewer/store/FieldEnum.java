package com.sec.bestreviewer.store;

public enum FieldEnum {

    FIELD_EMPLOYEE_NUMBER("employeeNumber"),
    FIELD_NAME("name"),
    FIELD_FIRST_NAME("nameFirst"),
    FIELD_SECOND_NAME("nameSecond"),
    FIELD_CAREER_LEVEL("careerLevel"),
    FIELD_PHONE_NUMBER("phoneNumber"),
    FIELD_PHONE_NUMBER_MIDDLE("phoneNumberMiddle"),
    FIELD_PHONE_NUMBER_LAST("phoneNumberLast"),
    FIELD_BIRTH_DAY("birthDay"),
    FIELD_BIRTH_DAY_YEAR("birthDayYear"),
    FIELD_BIRTH_DAY_MONTH("birthDayMonth"),
    FIELD_BIRTH_DAY_DAY("birthDayDay"),

    FIELD_CERTI("certi");

    private final String field;

    FieldEnum(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }


    public static FieldEnum fromFieldName(String field) {
        for (FieldEnum fieldEnum : FieldEnum.values()) {
            if (fieldEnum.getField().equals(field)) {
                return fieldEnum;
            }
        }
        throw new IllegalArgumentException("Invalid field name: " + field);
    }

}
