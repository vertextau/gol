package com.vertextau.gol;

import javax.swing.JFormattedTextField;

public class InputChecker {
    private static int value;

    static boolean checkInputInteger(JFormattedTextField element) throws IllegalArgumentException {

        if (element == null) {
            throw new IllegalArgumentException();
        }

        try {
            value = Integer.parseInt(element.getText());
        } catch (NumberFormatException e) {
            element.setText("Error. Please enter a integer");
            return false;
        }

        if (value <= 0 || value > 100) {
            element.setText("Error. Number must be in range (0, 100]");
            return false;
        }

        return true;
    }

    static int getValue() {
        return value;
    }
}
