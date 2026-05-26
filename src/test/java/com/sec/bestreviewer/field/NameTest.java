package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class NameTest {
    Name name1, name2, name3, name4, name5;

    private Name name;
    @BeforeEach
    void setup() {
        name1 = new Name("ABC", "LMN");
        name2 = new Name("ABC LMN");
        name3 = new Name("ABC OPQ");
        name4 = new Name("DEF LMN");
        name5 = new Name("DEF OPQ");
    }

    @Test
    void testToString() {
        assertEquals("ABC LMN", name1.toString());
    }

    @Test
    void testEquals() {
        assertTrue(name1.equals("ABC LMN"));
        assertTrue(name1.equals(name2));
        assertFalse(name1.equals(name3));
        assertFalse(name1.equals(name4));
        assertFalse(name1.equals(name5));
        assertThrows(IllegalArgumentException.class, () -> name1.equals(new Object()));
    }

    @Test
    void testCompareNone() {
        assertThrows(IllegalArgumentException.class, () -> name1.compare(name1, 3, TertiaryOptionEnum.NONE));

        assertTrue(name1.compare(name2, 0, TertiaryOptionEnum.NONE));
        assertTrue(name1.compare(name2, 1, TertiaryOptionEnum.NONE));
        assertTrue(name1.compare(name2, 2, TertiaryOptionEnum.NONE));
        assertTrue(name2.compare(name3, 1, TertiaryOptionEnum.NONE));
        assertTrue(name2.compare(name4, 2, TertiaryOptionEnum.NONE));
        assertTrue(name5.compare(name4, 1, TertiaryOptionEnum.NONE));
        assertTrue(name5.compare(name3, 2, TertiaryOptionEnum.NONE));

    }

    @Test
    void testCompareSmaller() {
        assertTrue(name2.compare(name3, 0, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name3, 0, TertiaryOptionEnum.S));
        assertTrue(name2.compare(name3, 1, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name3, 2, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name3, 2, TertiaryOptionEnum.S));

        assertTrue(name2.compare(name4, 0, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name4, 0, TertiaryOptionEnum.S));
        assertTrue(name2.compare(name4, 1, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name4, 1, TertiaryOptionEnum.S));
        assertTrue(name2.compare(name4, 2, TertiaryOptionEnum.SE));

        assertTrue(name2.compare(name5, 0, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name5, 0, TertiaryOptionEnum.S));
        assertTrue(name2.compare(name5, 1, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name5, 1, TertiaryOptionEnum.S));
        assertTrue(name2.compare(name5, 2, TertiaryOptionEnum.SE));
        assertTrue(name2.compare(name5, 2, TertiaryOptionEnum.S));
    }

    @Test
    void testCompareGreater() {
        assertTrue(name5.compare(name4, 0, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name4, 0, TertiaryOptionEnum.G));
        assertTrue(name5.compare(name4, 1, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name4, 2, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name4, 2, TertiaryOptionEnum.G));

        assertTrue(name5.compare(name3, 0, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name3, 0, TertiaryOptionEnum.G));
        assertTrue(name5.compare(name3, 1, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name3, 1, TertiaryOptionEnum.G));
        assertTrue(name5.compare(name3, 2, TertiaryOptionEnum.GE));

        assertTrue(name5.compare(name2, 0, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name2, 0, TertiaryOptionEnum.G));
        assertTrue(name5.compare(name2, 1, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name2, 1, TertiaryOptionEnum.G));
        assertTrue(name5.compare(name2, 2, TertiaryOptionEnum.GE));
        assertTrue(name5.compare(name2, 2, TertiaryOptionEnum.G));
    }

    @Test
    void testCompareTo() {
        assertEquals(0, name1.compareTo(name2));
        assertTrue(name2.compareTo(name3) < 0);
        assertTrue(name2.compareTo(name4) < 0);
        assertTrue(name2.compareTo(name5) < 0);
        assertTrue(name5.compareTo(name4) > 0);
        assertTrue(name5.compareTo(name3) > 0);
        assertTrue(name5.compareTo(name2) > 0);
    }
}