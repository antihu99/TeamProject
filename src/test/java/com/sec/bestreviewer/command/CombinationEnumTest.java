package com.sec.bestreviewer.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CombinationEnumTest {
    CombinationEnum combinationEnum;

    @BeforeEach
    void setUp() {
        combinationEnum = CombinationEnum.AND;

    }

    @Test
    void testGetCombination() {
        assertEquals("-a", combinationEnum.getCombination());
    }

    @Test
    void testFromCombination_ValidValues_ReturnsCorrectEnum() {
        assertEquals(CombinationEnum.NONE, CombinationEnum.fromCombination(" "));
        assertEquals(CombinationEnum.OR, CombinationEnum.fromCombination("-o"));
        assertEquals(CombinationEnum.AND, CombinationEnum.fromCombination("-a"));
    }

    @Test
    void testFromCombination_InvalidValue_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> CombinationEnum.fromCombination("invalid"));
        assertEquals("Invalid combination name: invalid", exception.getMessage());
    }

}