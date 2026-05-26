package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CertiTest {

    private void printResult(String testName, String input, Object expected, Object actual) {
        System.out.println("\nTest: " + testName);
        System.out.println("Input: " + input);
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
    }

    @Test
    void testParsingFromString() {
        Certi certi1 = new Certi("ADV");
        printResult("getCerti", "대상 Certi(\"ADV\")", 99, certi1.getCerti());
        assertEquals(0, certi1.getCerti());

        Certi certi2 = new Certi("PRO");
        printResult("getCerti", "대상 Certi(\"ADV\")", 99, certi2.getCerti());
        assertEquals(1, certi2.getCerti());

        Certi certi3 = new Certi("EX");
        printResult("getCerti", "대상 Certi(\"ADV\")", 99, certi3.getCerti());
        assertEquals(2, certi3.getCerti());
    }

    @Test
    void testToString() {
        Certi certi = new Certi("ADV");
        assertEquals("ADV", certi.toString());
    }

    @Test
    void testCompare() {
        Certi certiAdv = new Certi("ADV");
        Certi certiPro = new Certi("PRO");
        assertTrue(certiPro.compare(certiAdv, 0, TertiaryOptionEnum.G));
    }

    @Test
    void testCompareTo() {
        Certi certiAdv = new Certi("ADV");
        Certi certiPro = new Certi("PRO");
        Certi certiProCompare = new Certi("PRO");
        Certi certiEx = new Certi("EX");
        assertTrue(certiPro.compareTo(certiAdv) > 0);
        assertEquals(0, certiPro.compareTo(certiProCompare));
        assertTrue(certiPro.compareTo(certiEx) < 0);
    }

    @Test
    void testEquals() {
        Certi certiAdv = new Certi("ADV");
        Certi certiPro = new Certi("PRO");
        assertFalse(certiAdv.equals(new Name("ABC DEF")));
        assertTrue(certiAdv.equals("ADV"));
        assertNotEquals(certiAdv, certiPro);
    }

}
