package com.sec.bestreviewer.field;

import com.sec.bestreviewer.util.TertiaryOptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {
    PhoneNumber phoneNumber1, phoneNumber2, phoneNumber3, phoneNumber4,
            phoneNumber5, phoneNumber6, phoneNumber7;

    @BeforeEach
    void setup() {
        phoneNumber1 = new PhoneNumber("010-1234-5678");
        phoneNumber2 = new PhoneNumber("010-1234-5678");
        phoneNumber3 = new PhoneNumber("000-1234-5678");
        phoneNumber4 = new PhoneNumber("010-0000-5678");
        phoneNumber5 = new PhoneNumber("010-1234-0000");
        phoneNumber6 = new PhoneNumber("010-1234-5679");
        phoneNumber7 = new PhoneNumber("010-1234-5677");
    }

    @Test
    void testPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(null));
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("010-1234-567"));
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("010-123456789"));
    }

    @Test
    void testGetters() {
        assertEquals("010", phoneNumber1.getFirst());
        assertEquals("1234", phoneNumber1.getMiddle());
        assertEquals("5678", phoneNumber1.getLast());
        assertEquals("010-1234-5678", phoneNumber1.getPhoneNumber());
    }

    @Test
    void testToString() {
        assertEquals("010-1234-5678", phoneNumber1.toString());
    }

    @Test
    void testEquals() {
        assertFalse(phoneNumber1.equals(new Name("ABC DEF")));
        assertTrue(phoneNumber1.equals("010-1234-5678"));
        assertFalse(phoneNumber1.equals("011-1234-5678"));
        assertFalse(phoneNumber1.equals("010-1230-5678"));
        assertFalse(phoneNumber1.equals("010-1234-5670"));
        assertTrue(phoneNumber1.equals(phoneNumber2));
        assertFalse(phoneNumber1.equals(phoneNumber3));
        assertFalse(phoneNumber1.equals(phoneNumber4));
        assertFalse(phoneNumber1.equals(phoneNumber5));
    }

    @Test
    void compareTo() {
        assertThrows(IllegalArgumentException.class, () -> phoneNumber1.compareTo(new Name("ABC DEF")));
        assertTrue(phoneNumber1.compareTo(phoneNumber6) < 0);
        assertTrue(phoneNumber1.compareTo(phoneNumber7) > 0);
    }

    @Test
    void equalsMiddle() {
        assertTrue(phoneNumber1.equalsMiddle("1234"));
    }

    @Test
    void equalsLast() {
        assertTrue(phoneNumber1.equalsLast("5678"));
    }

    @Test
    void testCompare() {
        assertTrue(phoneNumber1.compare(phoneNumber2, 1, TertiaryOptionEnum.NONE));
        assertTrue(phoneNumber1.compare(phoneNumber3, 2, TertiaryOptionEnum.NONE));
        assertTrue(phoneNumber1.compare(phoneNumber4, 3, TertiaryOptionEnum.NONE));
    }
}