package com.sec.bestreviewer.store;


import com.sec.bestreviewer.command.CombinationEnum;
import com.sec.bestreviewer.field.*;

import com.sec.bestreviewer.util.TertiaryOptionEnum;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmployeeStoreImpl implements EmployeeStore {
    private final List<Employee> employees = new ArrayList<>();

    private final Map<FieldEnum, Function<String, Field>> fieldCreators = new HashMap<>();
    private final Map<FieldEnum, Function<Employee, Field>> fieldExtractors = new HashMap<>();

    public EmployeeStoreImpl() {
        initializeFieldMaps();
    }

    private void initializeFieldMaps() {
        // Field creators
        fieldCreators.put(FieldEnum.FIELD_EMPLOYEE_NUMBER, (value) -> new EmployeeNumber(value));
        fieldCreators.put(FieldEnum.FIELD_NAME, (value) -> new Name(value));
        fieldCreators.put(FieldEnum.FIELD_CAREER_LEVEL, (value) -> new CareerLevel(value)); // String field, handled separately
        fieldCreators.put(FieldEnum.FIELD_PHONE_NUMBER, (value) -> new PhoneNumber(value));
        fieldCreators.put(FieldEnum.FIELD_BIRTH_DAY, (value) -> new Birthday(value));
        fieldCreators.put(FieldEnum.FIELD_CERTI, (value) -> new Certi(value));

        // Field extractors
        fieldExtractors.put(FieldEnum.FIELD_EMPLOYEE_NUMBER, Employee::getEmployeeNumber);
        fieldExtractors.put(FieldEnum.FIELD_NAME, Employee::getName);
        fieldExtractors.put(FieldEnum.FIELD_CAREER_LEVEL, Employee::getCareerLevel);
        fieldExtractors.put(FieldEnum.FIELD_PHONE_NUMBER, Employee::getPhoneNumber);
        fieldExtractors.put(FieldEnum.FIELD_BIRTH_DAY, Employee::getBirthday);
        fieldExtractors.put(FieldEnum.FIELD_CERTI, Employee::getCerti);
    }

    @Override
    public List<Employee> search(FieldEnum fieldEnum, String value, TertiaryOptionEnum tertiaryOptionEnum) {
        return search(fieldEnum, value, tertiaryOptionEnum, 0);
    }

    @Override
    public List<Employee> search(FieldEnum fieldEnum, String value, TertiaryOptionEnum tertiaryOptionEnum, int subfieldIndex) {

        Function<String, Field> creator = fieldCreators.get(fieldEnum);
        Function<Employee, Field> extractor = fieldExtractors.get(fieldEnum);

        if (creator == null || extractor == null) {
            return Collections.emptyList();
        }

        Field searchField = creator.apply(value);
        final int finalSubfieldIndex = subfieldIndex;

        return employees.stream()
                .filter(employee -> {
                    Field targetField = extractor.apply(employee);

                    return targetField.compare(searchField, finalSubfieldIndex, tertiaryOptionEnum);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> search(FieldEnum fieldEnum1, String value1, TertiaryOptionEnum tertiaryOptionEnum1, int subfieldIndex1,
                                 CombinationEnum conditionEnum,
                                 FieldEnum fieldEnum2, String value2, TertiaryOptionEnum tertiaryOptionEnum2, int subfieldIndex2
                                 ) {

        Field firstSearchField = fieldCreators.get(fieldEnum1).apply(value1);
        Field secondSearchField = fieldCreators.get(fieldEnum2).apply(value2);

        final int finalSubfieldIndex1 = subfieldIndex1;
        final int finalSubfieldIndex2 = subfieldIndex2;

        return employees.stream()
                .filter(employee -> {
                    Field targetField1 = fieldExtractors.get(fieldEnum1).apply(employee);
                    Field targetField2 = fieldExtractors.get(fieldEnum2).apply(employee);
                    if (conditionEnum.equals(CombinationEnum.OR)) {
                        return targetField1.compare(firstSearchField, finalSubfieldIndex1, tertiaryOptionEnum1) ||
                                targetField2.compare(secondSearchField, finalSubfieldIndex2, tertiaryOptionEnum2);
                    } else {
                        return targetField1.compare(firstSearchField, finalSubfieldIndex1, tertiaryOptionEnum1) &&
                                targetField2.compare(secondSearchField, finalSubfieldIndex2, tertiaryOptionEnum2);
                    }

                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Employee> delete(FieldEnum field, String value) {
        return delete(field, value, 0);
    }

    @Override
    public List<Employee> delete(FieldEnum field, String value, int subfieldIndex) {
        List<Employee> searchedEmployees = search(field, value, TertiaryOptionEnum.NONE, subfieldIndex);
        employees.removeAll(searchedEmployees);
        return searchedEmployees;
    }

    @Override
    public List<Employee> delete(FieldEnum field, String value, int subfieldIndex,
                          CombinationEnum combinationEnum,
                          FieldEnum fieldEnum2, String value2,int subfieldIndex2) {
        List<Employee> searchedEmployees = search(field, value, TertiaryOptionEnum.NONE, subfieldIndex,
                combinationEnum,
                fieldEnum2, value2, TertiaryOptionEnum.NONE, subfieldIndex2);
        employees.removeAll(searchedEmployees);
        return searchedEmployees;
    }


    @Override
    public List<Employee> modify(FieldEnum field, String value, FieldEnum modifyField, String modifyValue) {
        return modify(field, value, modifyField, modifyValue, 0);
    }

    @Override
    public List<Employee> modify(FieldEnum field, String value, FieldEnum modifyField, String modifyValue, int subfieldIndex) {
        if (FieldEnum.FIELD_EMPLOYEE_NUMBER.equals(modifyField)) {
            throw new IllegalArgumentException("사원번호는 수정할 수 없습니다");
        }

        List<Employee> searchedEmployees = search(field, value,TertiaryOptionEnum.NONE, subfieldIndex);

        employees.removeAll(searchedEmployees);

        List<Employee> searchedModifyEmployees = modifyFieldOfList(modifyField, modifyValue, searchedEmployees);
        employees.addAll(searchedModifyEmployees);

        return searchedEmployees;
    }

    @Override
    public List<Employee>  modify(FieldEnum field, String value, int subfieldIndex,
                                  CombinationEnum combinationEnum,
                                  FieldEnum fieldEnum2, String value2, int subfieldIndex2,
                                  FieldEnum modifyField, String modifyValue) {
        if (FieldEnum.FIELD_EMPLOYEE_NUMBER.equals(modifyField)) {
            throw new IllegalArgumentException("사원번호는 수정할 수 없습니다");
        }

        List<Employee> searchedEmployees = search(field, value, TertiaryOptionEnum.NONE, subfieldIndex,
                combinationEnum,
                fieldEnum2, value2, TertiaryOptionEnum.NONE, subfieldIndex2);

        employees.removeAll(searchedEmployees);

        List<Employee> searchedModifyEmployees = modifyFieldOfList(modifyField, modifyValue, searchedEmployees);
        employees.addAll(searchedModifyEmployees);

        return searchedEmployees;
    }

    private static List<Employee> modifyFieldOfList(FieldEnum modifyField, String modifyValue, List<Employee> searchedEmployees) {
        List<Employee> searchedModifyEmployees = new ArrayList<>();
        for (Employee employee : searchedEmployees) {
            Employee pastEmployee = new Employee(employee.getEmployeeNumber().toString(), employee.getName().toString()
                    , employee.getCareerLevel().toString(), employee.getPhoneNumber().toString()
                    , employee.getBirthday().toString(), employee.getCerti().toString());
            searchedModifyEmployees.add(pastEmployee);
        }

        for (Employee searchedModifyEmployee : searchedModifyEmployees) {
            if (modifyField.equals(FieldEnum.FIELD_EMPLOYEE_NUMBER))
                searchedModifyEmployee.setEmployeeNumber(new EmployeeNumber(modifyValue));
            else if (modifyField.equals(FieldEnum.FIELD_NAME))
                searchedModifyEmployee.setName(new Name(modifyValue));
            else if (modifyField.equals(FieldEnum.FIELD_CAREER_LEVEL))
                searchedModifyEmployee.setCareerLevel( new CareerLevel(modifyValue));
            else if (modifyField.equals(FieldEnum.FIELD_PHONE_NUMBER))
                searchedModifyEmployee.setPhoneNumber(new PhoneNumber(modifyValue));
            else if (modifyField.equals(FieldEnum.FIELD_BIRTH_DAY))
                searchedModifyEmployee.setBirthday(new Birthday(modifyValue));
            else if (modifyField.equals(FieldEnum.FIELD_CERTI))
                searchedModifyEmployee.setCerti(new Certi(modifyValue));
        }

        return searchedModifyEmployees;
    }

    @Override
    public void add(Employee employee) {
        employees.add(employee);
    }

    @Override
    public int count() {
        return employees.size();
    }
}
