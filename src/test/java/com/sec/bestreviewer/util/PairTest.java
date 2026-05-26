package com.sec.bestreviewer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void testEquals_WhenBothElementsEqual_ReturnsTrue() {
        Pair<String, Integer> p1 = Pair.create("key", 1);
        Pair<String, Integer> p2 = Pair.create("key", 1);
        assertTrue(p1.equals(p2));
    }

    @Test
    void testEquals_WhenFirstElementsDifferent_ReturnsFalse() {
        Pair<String, Integer> p1 = Pair.create("key1", 1);
        Pair<String, Integer> p2 = Pair.create("key2", 1);
        assertFalse(p1.equals(p2));
    }

    @Test
    void testEquals_WhenSecondElementsDifferent_ReturnsFalse() {
        Pair<String, Integer> p1 = Pair.create("key", 1);
        Pair<String, Integer> p2 = Pair.create("key", 2);
        assertFalse(p1.equals(p2));
    }

    @Test
    void testEquals_WithNull_ReturnsFalse() {
        Pair<String, Integer> p1 = Pair.create("key", 1);
        assertFalse(p1.equals(null));
    }

    @Test
    void testEquals_WithNonPairObject_ReturnsFalse() {
        Pair<String, Integer> p1 = Pair.create("key", 1);
        assertFalse(p1.equals("non-pair"));
    }

    @Test
    void testHashCode_EqualPairsHaveSameCode() {
        Pair<String, Integer> p1 = Pair.create("key", 1);
        Pair<String, Integer> p2 = Pair.create("key", 1);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testHashCode_NullElements_ReturnsZeroXor() {
        Pair<String, Integer> p = Pair.create(null, null);
        assertEquals(0, p.hashCode());
    }

    @Test
    void testToString_ReturnsCorrectFormat() {
        Pair<String, Integer> p = Pair.create("test", 123);
        assertEquals("Pair{test 123}", p.toString());
    }

    @Test
    void testToString_WithNullValues() {
        Pair<String, Integer> p = Pair.create(null, null);
        assertEquals("Pair{null null}", p.toString());
    }
}