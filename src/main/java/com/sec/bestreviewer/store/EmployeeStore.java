package com.sec.bestreviewer.store;

import com.sec.bestreviewer.command.CombinationEnum;
import com.sec.bestreviewer.util.TertiaryOptionEnum;

import java.util.List;

public interface EmployeeStore {
    String FIELD_EMPLOYEE_NUMBER = "employeeNumber";
    String FIELD_NAME = "name";
    String FIELD_FIRST_NAME = "firstName";
    String FIELD_SECOND_NAME = "secondName";
    String FIELD_CAREER_LEVEL = "careerLevel";
    String FIELD_PHONE_NUMBER = "phoneNumber";
    String FIELD_BIRTH_DAY = "birthDay";
    String FIELD_CERTI = "certi";
    void add(Employee employee);
    int count();

    /* subfieldIndex 가 없는 기존 search, delete, modify 는 테스트 코드 동작을 위해 유지함 */
    List<Employee> search(FieldEnum field, String value, TertiaryOptionEnum tertiaryOptionEnum);
    List<Employee> search(FieldEnum field, String value, TertiaryOptionEnum tertiaryOptionEnum, int subfieldIndex );
    List<Employee> search(FieldEnum fieldEnum1, String value1, TertiaryOptionEnum tertiaryOptionEnum1, int subfieldIndex1,
                          CombinationEnum combinationEnum,
                          FieldEnum fieldEnum2, String value2, TertiaryOptionEnum tertiaryOptionEnum2, int subfieldIndex2)                          ;
    List<Employee> delete(FieldEnum field, String value);
    List<Employee> delete(FieldEnum field, String value, int subfieldIndex);
    List<Employee> delete(FieldEnum field, String value, int subfieldIndex,
                          CombinationEnum combinationEnum,
                          FieldEnum fieldEnum2, String value2, int subfieldIndex2);
    List<Employee> modify(FieldEnum field, String value, FieldEnum modifyField, String modifyValue);
    List<Employee> modify(FieldEnum field, String value, FieldEnum modifyField, String modifyValue, int subfieldIndex);
    List<Employee> modify(FieldEnum field, String value, int subfieldIndex,
                          CombinationEnum combinationEnum,
                          FieldEnum fieldEnum2, String value2, int subfieldIndex2,
                          FieldEnum modifyField, String modifyValue);
}
