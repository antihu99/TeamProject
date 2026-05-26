package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CareerLevelTest {

    private void printResult(String testName, String dataset, Object executed, Object expected) {
        System.out.println("\nTest: " + testName);
        System.out.println("Input: " + dataset);
        System.out.println("Executed Value: " + executed);
        System.out.println("Expected Value: " + expected);
    }

    @Test
    void testConstructorAndToString() {
        CareerLevel cl = new CareerLevel("CL3");
        printResult("toString", "CareerLevel(\"CL3\")", cl.toString(), "CL3");
        assertEquals("CL3", cl.toString());
    }

    @Test
    void testInvalidConstructor() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new CareerLevel("CL0"));
        printResult("invalidConstructor", "CareerLevel(\"CL0\")", ex.getClass().getSimpleName(), "IllegalArgumentException");
        ex = assertThrows(IllegalArgumentException.class, () -> new CareerLevel("CLX"));
        printResult("invalidConstructor", "CareerLevel(\"CLX\")", ex.getClass().getSimpleName(), "IllegalArgumentException");
    }

    @Test
    void testCompareAllCases() {
        CareerLevel cl1 = new CareerLevel("CL1");
        CareerLevel cl2 = new CareerLevel("CL2");
        CareerLevel cl3 = new CareerLevel("CL3");
        CareerLevel cl4 = new CareerLevel("CL4");

        assertFalse(cl2.compare(cl3, 0, TertiaryOptionEnum.G));
        assertTrue(cl3.compare(cl2, 0, TertiaryOptionEnum.G));
        assertFalse(cl3.compare(cl2, 0, TertiaryOptionEnum.S));
        assertTrue(cl4.compare(cl4, 0, TertiaryOptionEnum.GE));
        assertFalse(cl2.compare(cl1, 0, TertiaryOptionEnum.SE));
        assertTrue(cl2.compare(cl2, 0, TertiaryOptionEnum.NONE));

        assertFalse(cl1.compare(cl1, 0, TertiaryOptionEnum.G));
        assertFalse(cl4.compare(cl4, 0, TertiaryOptionEnum.S));
    }

    @Test
    void testCompareMethod_invalidArgs() {
        CareerLevel cl = new CareerLevel("CL2");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> cl.compare(new CareerLevel("CL3"), 1, TertiaryOptionEnum.G));
        printResult("invalid subfieldIndex", "CareerLevel(\"CL2\"), subfieldIndex=1", ex.getClass().getSimpleName(), "IllegalArgumentException");
        ex = assertThrows(IllegalArgumentException.class, () -> cl.compare(new Birthday("20010101"), 0, TertiaryOptionEnum.G));
        printResult("invalid type", "CareerLevel(\"CL2\"), Birthday", ex.getClass().getSimpleName(), "IllegalArgumentException");
    }

    @Test
    void testEqualsAndEqualsString() {
        CareerLevel cl3a = new CareerLevel("CL3");
        CareerLevel cl3b = new CareerLevel("CL3");
        CareerLevel cl2 = new CareerLevel("CL2");

        assertNotEquals(cl3a, new Object());

        printResult("equals(Object)", "CL3 vs CL3, equal?", cl3a.equals(cl3b), true);
        assertTrue(cl3a.equals(cl3b));

        printResult("equals(Object, false)", "CL3 vs CL2, equal?", cl3a.equals(cl2), false);
        assertFalse(cl3a.equals(cl2));

        printResult("equals(String)", "CL3 vs \"CL3\", equal?", cl3a.equals("CL3"), true);
        assertTrue(cl3a.equals("CL3"));

        printResult("equals(String, false)", "CL3 vs \"CL2\", equal?", cl3a.equals("CL2"), false);
        assertFalse(cl3a.equals("CL2"));
    }

    @Test
    void testCompareTo() {
        CareerLevel cl2 = new CareerLevel("CL2");
        CareerLevel cl3 = new CareerLevel("CL3");
        assertThrows(IllegalArgumentException.class, () -> cl2.compareTo(new Name("ABC DEF")));
        printResult("compareTo(smaller)", "CL2 vs CL3", cl2.compareTo(cl3), -1);
        assertTrue(cl2.compareTo(cl3) < 0);
        printResult("compareTo(greater)", "CL3 vs CL2", cl3.compareTo(cl2), 1);
        assertTrue(cl3.compareTo(cl2) > 0);
        printResult("compareTo(equal)", "CL3 vs CL3", cl3.compareTo(cl3), 0);
        assertEquals(0, cl3.compareTo(cl3));
    }

    @Test
    void testGetterMethods() {
        CareerLevel cl = new CareerLevel("CL3");
        printResult("getValue", "CareerLevel(\"CL3\")", cl.getValue(), "CL3");
        assertEquals("CL3", cl.getValue());

        printResult("getLevelNum", "CareerLevel(\"CL3\")", cl.getLevelNum(), 3);
        assertEquals(3, cl.getLevelNum());
    }

}
