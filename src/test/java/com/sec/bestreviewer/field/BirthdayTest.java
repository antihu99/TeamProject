package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BirthdayTest {

    private void printResult(String testName, String dataset, Object executed, Object expected) {
        System.out.println("\nTest: " + testName);
        System.out.println("Input: " + dataset);
        System.out.println("Executed Value: " + executed);
        System.out.println("Expected Value: " + expected);
    }

    @Test
    void testConstructorAndToString() {
        assertThrows(IllegalArgumentException.class, () -> new Birthday((String) null));
        assertThrows(IllegalArgumentException.class, () -> new Birthday("199812"));
        assertThrows(IllegalArgumentException.class, () -> new Birthday("199812345"));

        Birthday bd = new Birthday("19980312");
        printResult("toString", "Birthday(\"19980312\")", bd.toString(), "19980312");
        assertEquals("19980312", bd.toString());

        Birthday bd2 = new Birthday(1998, 3, 12);
        printResult("toString(int...)", "Birthday(1998,3,12)", bd2.toString(), "19980312");
        assertEquals("19980312", bd2.toString());
    }

    @Test
    void testInvalidConstructor() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new Birthday("1998-03-12"));
        printResult("invalidConstructor", "Birthday(\"1998-03-12\")", ex.getClass().getSimpleName(), "IllegalArgumentException");
        ex = assertThrows(IllegalArgumentException.class, () -> new Birthday("199803"));
        printResult("invalidConstructor", "Birthday(\"199803\")", ex.getClass().getSimpleName(), "IllegalArgumentException");
    }

    @Test
    void testCompareMethod_illegalParameter() {
        Birthday base = new Birthday("20220110");
        assertThrows(IllegalArgumentException.class,
                () -> base.compare(new CareerLevel("CL2"), 0, TertiaryOptionEnum.G));
    }

    @Test
    void testCompareMethod_fullDate() {
        Birthday base = new Birthday("20220110");
        Birthday cmp1 = new Birthday("20220109");
        Birthday cmp2 = new Birthday("20220110");
        Birthday cmp3 = new Birthday("20220111");

        // subfieldIndex 0: 전체 날짜 비교
        printResult("compare full GREATER", "20220110 > 20220109", base.compare(cmp1, 0, TertiaryOptionEnum.G), true);
        assertTrue(base.compare(cmp1, 0, TertiaryOptionEnum.G));

        printResult("compare full EQUAL", "20220110 == 20220110", base.compare(cmp2, 0, TertiaryOptionEnum.NONE), true);
        assertTrue(base.compare(cmp2, 0, TertiaryOptionEnum.NONE));

        printResult("compare full SMALLER", "20220110 < 20220111", base.compare(cmp3, 0, TertiaryOptionEnum.S), true);
        assertTrue(base.compare(cmp3, 0, TertiaryOptionEnum.S));
    }

    @Test
    void testCompareMethod_year() {
        Birthday base = new Birthday("20220110");
        Birthday cmp1 = new Birthday("20210110");
        Birthday cmp2 = new Birthday("20220110");
        Birthday cmp3 = new Birthday("20230110");

        // subfieldIndex 1: 연도 비교
        printResult("compare year GREATER", "2022 > 2021", base.compare(cmp1, 1, TertiaryOptionEnum.G), true);
        assertTrue(base.compare(cmp1, 1, TertiaryOptionEnum.G));

        printResult("compare year EQUAL", "2022 == 2022", base.compare(cmp2, 1, TertiaryOptionEnum.NONE), true);
        assertTrue(base.compare(cmp2, 1, TertiaryOptionEnum.NONE));

        printResult("compare year SMALLER", "2022 < 2023", base.compare(cmp3, 1, TertiaryOptionEnum.S), true);
        assertTrue(base.compare(cmp3, 1, TertiaryOptionEnum.S));
    }

    @Test
    void testCompareMethod_month() {
        Birthday base = new Birthday("20220110");
        Birthday cmp1 = new Birthday("20211210");
        Birthday cmp2 = new Birthday("20220110");
        Birthday cmp3 = new Birthday("20220310");

        // subfieldIndex 2: 월 비교
        printResult("compare month GREATER", "1 > 12", base.compare(cmp1, 2, TertiaryOptionEnum.G), false); // 1 > 12는 false
        assertFalse(base.compare(cmp1, 2, TertiaryOptionEnum.G));

        printResult("compare month EQUAL", "1 == 1", base.compare(cmp2, 2, TertiaryOptionEnum.NONE), true);
        assertTrue(base.compare(cmp2, 2, TertiaryOptionEnum.NONE));

        printResult("compare month SMALLER", "1 < 3", base.compare(cmp3, 2, TertiaryOptionEnum.S), true);
        assertTrue(base.compare(cmp3, 2, TertiaryOptionEnum.S));
    }

    @Test
    void testCompareMethod_day() {
        Birthday base = new Birthday("20220110");
        Birthday cmp1 = new Birthday("20220109");
        Birthday cmp2 = new Birthday("20220110");
        Birthday cmp3 = new Birthday("20220111");

        // subfieldIndex 3: 일 비교
        printResult("compare day GREATER", "10 > 9", base.compare(cmp1, 3, TertiaryOptionEnum.G), true);
        assertTrue(base.compare(cmp1, 3, TertiaryOptionEnum.G));

        printResult("compare day EQUAL", "10 == 10", base.compare(cmp2, 3, TertiaryOptionEnum.NONE), true);
        assertTrue(base.compare(cmp2, 3, TertiaryOptionEnum.NONE));

        printResult("compare day SMALLER", "10 < 11", base.compare(cmp3, 3, TertiaryOptionEnum.S), true);
        assertTrue(base.compare(cmp3, 3, TertiaryOptionEnum.S));
    }

    @Test
    void testEqualsAndEqualsString() {
        Birthday bd = new Birthday("20001231");
        Birthday bd2 = new Birthday(2000, 12, 31);

        assertNotEquals(bd, new Object());

        printResult("equals(Object)", "20001231 vs 20001231", bd.equals(bd2), true);
        assertEquals(bd, bd2);

        printResult("equals(String)", "20001231 vs '20001231'", bd.equals("20001231"), true);
        assertTrue(bd.equals("20001231"));

        printResult("equals(String)", "20001231 vs '20011231'", bd.equals("20011231"), false);
        assertNotEquals(bd, new Birthday("20011231"));

        printResult("equals(String)", "20001231 vs '20001131'", bd.equals("20001131"), false);
        assertNotEquals(bd, new Birthday("20001131"));

        printResult("equals(String)", "20001231 vs '20001230'", bd.equals("20001230"), false);
        assertNotEquals(bd, new Birthday("20001230"));
    }

    @Test
    void testCompareToMethod_illegalParameter() {
        Birthday base = new Birthday("20220110");
        assertThrows(IllegalArgumentException.class,
                () -> base.compareTo(new CareerLevel("CL2")));
    }

    @Test
    void testCompareTo() {
        Birthday bd1 = new Birthday("19981206");
        Birthday bd2 = new Birthday("19990101");
        Birthday bd3 = new Birthday("19981205");
        Birthday bd4 = new Birthday("19981105");

        printResult("compareTo(before)", "19981206 < 19990101", bd1.compareTo(bd2), -1);
        assertTrue(bd1.compareTo(bd2) < 0);

        printResult("compareTo(after)", "19981206 > 19981205", bd1.compareTo(bd3), 1);
        assertTrue(bd1.compareTo(bd3) > 0);

        printResult("compareTo(before)", "19981205 > 19981105", bd1.compareTo(bd2), -1);
        assertTrue(bd3.compareTo(bd4) > 0);

        printResult("compareTo(before)", "19981105 < 19981205", bd1.compareTo(bd2), -1);
        assertTrue(bd4.compareTo(bd3) < 0);
    }

    @Test
    void testHashCode() {
        Birthday bd1 = new Birthday("20200101");
        Birthday bd2 = new Birthday(2020, 1, 1);
        printResult("hashCode", "20200101", bd1.hashCode(), bd2.hashCode());
        assertEquals(bd1.hashCode(), bd2.hashCode());
    }

    @Test
    void testGetterMethods() {
        Birthday bd = new Birthday(2024, 7, 1);
        printResult("getYear", "Birthday(2024, 7, 1)", bd.getYear(), 2024);
        assertEquals(2024, bd.getYear());

        printResult("getMonth", "Birthday(2024, 7, 1)", bd.getMonth(), 7);
        assertEquals(7, bd.getMonth());

        printResult("getDay", "Birthday(2024, 7, 1)", bd.getDay(), 1);
        assertEquals(1, bd.getDay());
    }

    @Test
    void testCompareMethod_invalidSubfieldIndex() {
        Birthday bd = new Birthday("20220110");
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                bd.compare(new Birthday("20220110"), 4, TertiaryOptionEnum.G));
        printResult("compare invalid subfieldIndex", "subfieldIndex=4", ex.getMessage(), "잘못된 subfieldIndex: 4");
    }

    @Test
    void testCompareTo_invalidType() {
        Birthday bd = new Birthday("20220110");
        // CareerLevel로 compareTo시 예외 발생 확인
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                bd.compareTo(new CareerLevel("CL2")));
        printResult("compareTo invalidType", "Birthday vs CareerLevel", ex.getMessage(), "Cannot compare Birthday with CareerLevel");
    }

}
