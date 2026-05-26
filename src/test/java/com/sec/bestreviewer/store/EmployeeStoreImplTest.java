package com.sec.bestreviewer.store;

import com.sec.bestreviewer.command.CombinationEnum;
import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeStoreImplTest {

    private EmployeeStoreImpl store;

    @BeforeEach
    void setUp() {
        store = new EmployeeStoreImpl();
        store.add(new Employee("90000001", "YUJIN LEE", "CL1", "010-1111-1111", "19900101", "PRO"));
        store.add(new Employee("90000002", "JISU PARK", "CL2", "010-2222-2222", "19910101", "ADV"));
        store.add(new Employee("90000003", "SOYEON JUNG", "CL3", "010-3333-3333", "19920101", "EX"));
        store.add(new Employee("90000004", "SEUNGHUN CHOI", "CL4", "010-4444-4444", "19930101", "PRO"));
    }

    @Test
    void testAddAndCount() {
        assertEquals(4, store.count());
        store.add(new Employee("90000005", "MINHO SONG", "CL2", "010-5555-5555", "19940101", "ADV"));
        assertEquals(5, store.count());
    }

    @Test
    void testSearchByEmployeeNumber() {
        List<Employee> result = store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000001", TertiaryOptionEnum.NONE);
        assertEquals(1, result.size());
        assertEquals("YUJIN LEE", result.get(0).getName().toString());
    }

    @Test
    void testSearchByCareerLevelGreater() {
        List<Employee> result = store.search(FieldEnum.FIELD_CAREER_LEVEL, "CL2", TertiaryOptionEnum.G);
        assertEquals(2, result.size()); // CL3, CL4
    }

    @Test
    void testDeleteByCerti() {
        List<Employee> deleted = store.delete(FieldEnum.FIELD_CERTI, "PRO");
        assertEquals(2, deleted.size());
        assertEquals(2, store.count());
    }

    @Test
    void testModifyPhoneNumber() {
        List<Employee> modified = store.modify(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000002", FieldEnum.FIELD_PHONE_NUMBER, "010-9999-8888");
        assertEquals(1, modified.size());
        List<Employee> check = store.search(FieldEnum.FIELD_EMPLOYEE_NUMBER, "90000002", TertiaryOptionEnum.NONE);
        assertEquals("010-9999-8888", check.get(0).getPhoneNumber().toString());
    }

    @Test
    void testSearchWithCombination_OR() {
        List<Employee> result = store.search(
                FieldEnum.FIELD_CAREER_LEVEL, "CL1", TertiaryOptionEnum.NONE, 0,
                CombinationEnum.OR,
                FieldEnum.FIELD_CERTI, "ADV", TertiaryOptionEnum.NONE, 0);
        assertEquals(2, result.size()); // CL1(PRO) + any(ADV)
    }

    @Test
    void testSearchWithCombination_AND() {
        List<Employee> result = store.search(
                FieldEnum.FIELD_CAREER_LEVEL, "CL2", TertiaryOptionEnum.NONE, 0,
                CombinationEnum.AND,
                FieldEnum.FIELD_CERTI, "ADV", TertiaryOptionEnum.NONE, 0);
        assertEquals(1, result.size()); // Only JISU PARK (CL2, ADV)
    }
}
