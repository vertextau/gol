package com.vertextau.gol;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javax.swing.JFormattedTextField;

class InputCheckerTest {

    private JFormattedTextField input;

    InputCheckerTest() {
        input = new JFormattedTextField();
    }

    @Test
    void checkInputIntegerCorrect() {
        String expectedValueCorrectInput = "42";
        boolean result;

        input.setText("42");
        result = InputChecker.checkInputInteger(input);

        assertEquals(expectedValueCorrectInput, input.getText());
        assertTrue(result);
    }

    @Test
    void checkInputIntegerNotInt() {
        String expectedValueNotIntInput = "Error. Please enter an integer";
        boolean result;

        input.setText("TEST");
        result = InputChecker.checkInputInteger(input);

        assertEquals(expectedValueNotIntInput, input.getText());
        assertFalse(result);
    }

    @Test
    void checkInputIntegerBadRange() {
        String expectedValueBadRangeInput = "Error. Number must be in range (0, 100]";
        boolean result;

        input.setText("123");
        result = InputChecker.checkInputInteger(input);

        assertEquals(expectedValueBadRangeInput, input.getText());
        assertFalse(result);
    }

    @Test
    void checkInputIntegerThrows() {
        JFormattedTextField t = new JFormattedTextField();

        assertThrows(IllegalArgumentException.class, () -> InputChecker.checkInputInteger(null));
    }
}
