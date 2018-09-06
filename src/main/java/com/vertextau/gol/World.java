package com.vertextau.gol;

public class World {

    private int rows;
    private int columns;
    private Cell[][] map;

    public World(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        map = new Cell[rows][columns];

        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < columns; ++c) {
                map[r][c] = new Cell(r, c);
            }
        }
    }

    public Cell getCell(int r, int c) {
        return map[r][c];
    }

    public void changeState() {
        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                Cell cell = map[row][column];

                if (cell.getOldState()) {
                    if (checkNeighbours(cell) < 2 || checkNeighbours(cell) > 3) {
                        cell.setNewState(false);
                    }
                    else {
                        cell.setNewState(true);
                    }
                }
                else {
                    if (checkNeighbours(cell) == 3) {
                        cell.setNewState(true);
                    }
                }
            }
        }
    }

    private int checkNeighbours(Cell cell) throws IllegalArgumentException {

        if (cell == null) {
            throw new IllegalArgumentException();
        }

        int aliveNeighbours = 0;
        int xCoordinate = cell.getxCoordinate();
        int yCoordinate = cell.getyCoordinate();

        int column, row;

        for (int y = -1; y <= 1; ++y) {
            row = yCoordinate + y;

            /*
            Verify boundary conditions.
            Basically we are connecting left and right edges and up and down edges
            to create some sort of torus.
            https://en.wikipedia.org/wiki/Torus
             */
            if (row < 0) {
                row = rows - 1;
            }
            if(row >= rows) {
                row = 0;
            }

            for (int x = -1; x <= 1; ++x) {
                column = xCoordinate + x;

                if (column < 0) {
                    column = columns - 1;
                }
                if(column >= columns) {
                    column = 0;
                }

                if (map[row][column].getOldState()) {
                    aliveNeighbours++;
                }
            }
        }

        /*
        Algorithm above has an issue. It counts not only neighbours
        but the cell too. So if the cell is alive, we decrement aliveNeighbours.
         */
        if (map[yCoordinate][xCoordinate].getOldState()) {
            return --aliveNeighbours;
        } else {
            return aliveNeighbours;
        }
    }
}
