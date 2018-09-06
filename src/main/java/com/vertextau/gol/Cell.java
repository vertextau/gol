package com.vertextau.gol;

import java.awt.Color;

public class Cell {
    private boolean oldState;
    private boolean newState;

    private int xCoordinate;
    private int yCoordinate;

    public static Color liveCellColor = Color.RED;
    public static Color deadCellColor = Color.GRAY;

    public Cell(int y, int x) {
        xCoordinate = x;
        yCoordinate = y;
    }

    /*
    Copy a new state to an old so we can work
    with new discrete step.
    Beware, call this method once by each step for
    each instance of a cell.
     */
    public boolean isAlive() {
        oldState = newState;
        return newState;
    }

    public void setOldState(boolean state) {
        newState = oldState = state;
    }

    public boolean getOldState() {
        return oldState;
    }

    public void setNewState(boolean state) {
        newState = state;
    }

    public boolean getNewState() {
        return newState;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }
}
