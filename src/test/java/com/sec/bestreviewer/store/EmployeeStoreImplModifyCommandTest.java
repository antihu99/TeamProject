package com.sec.bestreviewer.store;

import com.sec.bestreviewer.command.CombinationEnum;
import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeStoreImplModifyCommandTest {

    private EmployeeStoreImpl store;

    @BeforeEach
    void setUp() {
        store = new EmployeeStoreImpl();
        store.add(new Employee("90000001", "YUJIN KIM", "CL1", "010-1111-1111", "19900101", "PRO"));
        store.add(new Employee("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "ADV"));
        store.add(new Employee("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"));
    }

    private static String employeeToMODString(Employee e) {
        return String.join(",",
                "MOD",
                e.getEmployeeNumber().toString(),
                e.getName().toString(),
                e.getCareerLevel().toString(),
                e.getPhoneNumber().toString(),
                e.getBirthday().toString()
        );
    }

    @Test
    void testModifyName() {
        String testName = "성명이 YUJIN KIM인 record의 성명을 YUJIN LEE로 변경";
        // 조건에 해당하는 record 변경 전 값이 출력되어야 함
        List<Employee> beforeModify = store.search(FieldEnum.FIELD_NAME, "YUJIN KIM", TertiaryOptionEnum.NONE, 0);
        List<String> expected = beforeModify.stream()
                .map(EmployeeStoreImplModifyCommandTest::employeeToMODString)
                .collect(Collectors.toList());

        // 실제로 이름을 수정
        List<Employee> modified = store.modify(FieldEnum.FIELD_NAME, "YUJIN KIM", FieldEnum.FIELD_NAME, "YUJIN LEE", 0);
        List<String> actual = modified.stream()
                .map(EmployeeStoreImplModifyCommandTest::employeeToMODString)
                .collect(Collectors.toList());

        assertEquals(expected, actual); // 변경 전 데이터가 맞는지

        // 변경 후 데이터 확인
        List<Employee> afterModify = store.search(FieldEnum.FIELD_NAME, "YUJIN LEE", TertiaryOptionEnum.NONE, 0);
        assertEquals(1, afterModify.size());
        assertEquals("YUJIN LEE", afterModify.get(0).getName().toString());
    }

    @Test
    void testModifyPhoneNumberByCareerLevel() {
        String testName = "경력개발단계가 CL3인 record의 전화번호를 010-0970-0055로 변경";
        List<Employee> beforeModify = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL3", TertiaryOptionEnum.NONE, 0);
        List<String> expected = beforeModify.stream()
                .map(EmployeeStoreImplModifyCommandTest::employeeToMODString)
                .collect(Collectors.toList());

        List<Employee> modified = store.modify(FieldEnum.FIELD_CAREER_LEVEL, "CL3", FieldEnum.FIELD_PHONE_NUMBER, "010-0970-0055", 0);
        List<String> actual = modified.stream()
                .map(EmployeeStoreImplModifyCommandTest::employeeToMODString)
                .collect(Collectors.toList());

        assertEquals(expected, actual);

        // 변경 후 데이터 확인
        List<Employee> afterModify = store.search(FieldEnum.FIELD_PHONE_NUMBER, "010-0970-0055", TertiaryOptionEnum.NONE, 0);
        assertEquals(1, afterModify.size());
        assertEquals("010-0970-0055", afterModify.get(0).getPhoneNumber().toString());
        assertEquals("CL3", afterModify.get(0).getCareerLevel().toString());
    }

    @Test
    void testModifyBirthdayByPhoneNumber() {
        String testName = "전화번호가 010-0970-0055인 record의 생년월일을 20001225로 변경";
        // 먼저 전화번호를 가진 사원이 없으므로 하나 추가
        store.add(new Employee("90000010", "TESTER KIM", "CL2", "010-0970-0055", "19880101", "ADV"));

        List<Employee> beforeModify = store.search(FieldEnum.FIELD_PHONE_NUMBER, "010-0970-0055", TertiaryOptionEnum.NONE, 0);
        List<String> expected = beforeModify.stream()
                .map(EmployeeStoreImplModifyCommandTest::employeeToMODString)
                .collect(Collectors.toList());

        List<Employee> modified = store.modify(FieldEnum.FIELD_PHONE_NUMBER, "010-0970-0055", FieldEnum.FIELD_BIRTH_DAY, "20001225", 0);
        List<String> actual = modified.stream()
                .map(EmployeeStoreImplModifyCommandTest::employeeToMODString)
                .collect(Collectors.toList());

        assertEquals(expected, actual);

        List<Employee> afterModify = store.search(FieldEnum.FIELD_BIRTH_DAY, "20001225", TertiaryOptionEnum.NONE, 0);
        assertEquals(1, afterModify.size());
        assertEquals("20001225", afterModify.get(0).getBirthday().toString());
    }

    @Test
    void testModifyNoMatch() {
        // 조건에 맞는 사원이 없을 때
        List<Employee> modified = store.modify(FieldEnum.FIELD_NAME, "NO EXIST", FieldEnum.FIELD_PHONE_NUMBER, "010-9999-9999", 0);
        assertTrue(modified.isEmpty());

        // 실제 실행에서는 반환값이 empty면 결과 출력 코드에서 "NONE"을 출력해야 합니다.
        String result = modified.isEmpty() ? "NONE" : modified.stream()
                .map(EmployeeStoreImplModifyCommandTest::employeeToMODString)
                .collect(Collectors.joining("\n"));

        assertEquals("NONE", result);
    }

    @Test
    void testModifyNotAllowedEmployeeNumber_ThrowsException() {
        // 단일 조건 수정 시도
        assertThrows(IllegalArgumentException.class, () -> {
            store.modify(FieldEnum.FIELD_NAME, "JISU PARK", FieldEnum.FIELD_EMPLOYEE_NUMBER, "99999999", 0);
        });

        // and/or 복합조건 수정 시도
        assertThrows(IllegalArgumentException.class, () -> {
            store.modify(
                    FieldEnum.FIELD_NAME, "JISU PARK", 0,
                    CombinationEnum.AND,
                    FieldEnum.FIELD_CAREER_LEVEL, "CL2", 0,
                    FieldEnum.FIELD_EMPLOYEE_NUMBER, "99999999"
            );
        });
    }


}
