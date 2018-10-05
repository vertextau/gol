package com.vertextau.gol;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public final class UI {

    private World world;

    /*
    Auxiliary variables
     */
    private final int toolBarH = 50, toolBarW = 50;
    private static final int GAP = 2;

    private int worldRows, worldColumns;

    private int canvasWidth, canvasHeight, cellHeight, cellWidth;
    private int discreteStep;

    private boolean playState;

    /*
    Swing components
     */
    private JFrame frame;
    private JPanel canvas;

    private JButton playButton, resizeWorld;

    private final JFormattedTextField rowsInput, columnsInput;

    private JToolBar toolBar;

    /*
    Drawing components
     */
    private Image imageWorld;
    private Graphics graphics;


    public UI() {
        this(1, 50, 50);
    }

    public UI(int discreteStep, int worldRows, int worldColumns) {

        this.worldRows = worldRows;
        this.worldColumns = worldColumns;
        this.discreteStep = discreteStep;

        world = new World(worldRows, worldColumns);

        frame = new JFrame("Conway's Game of Life");

        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(imageWorld, 0, 0, null);
            }
        };

        canvas.setSize(700, 700);
        frame.add(canvas, BorderLayout.CENTER);

        JLabel coordinates = new JLabel("Coordinates: ");
        final JLabel rowsInputText = new JLabel("Rows: ");
        final JLabel columnsInputText = new JLabel("Columns: ");

        /*
        Buttons
         */
        resizeWorld = new JButton("Resize Grid");

        playButton = new JButton("Play");

        JButton clearButton = new JButton("Clear");

        JButton randomButton = new JButton("Random");

        JButton nextStep = new JButton("Next Step");

        /*
        Input fields
         */
        rowsInput = new JFormattedTextField();
        columnsInput = new JFormattedTextField();


        /*
        Container for user based components
         */
        toolBar = new JToolBar();
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);

        toolBar.setSize(toolBarW,toolBarH);

        frame.add(toolBar, BorderLayout.NORTH);

        toolBar.add(coordinates);
        toolBar.add(playButton);
        toolBar.add(clearButton);
        toolBar.add(randomButton);
        toolBar.add(nextStep);


        // WIP
        /*toolBar.add(rowsInputText);
        toolBar.add(rowsInput);
        toolBar.add(columnsInputText);
        toolBar.add(columnsInput);
        toolBar.add(resizeWorld);*/


        /*
        Calculate canvas height and width.
         */
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        /*
        Set up size of a frame based on canvas size.
        GAP is for a little alignment. *crutch*
         */
        frame.setSize(canvasWidth+GAP*2,canvasHeight+toolBarH+GAP);

        /*
        Calculate height and width of a cell.
         */
        calculateGeometry(worldRows, worldColumns);


        /*
        Run and stop world handler.
         */
        playButton.addActionListener((ae) -> {
            playState = !playState;
            if (playState) {
                playButton.setText("Pause");
            } else {
                playButton.setText("Play");
            }
        });

        /*
        Clear world.
        Works only when the world is stopped.
         */
        clearButton.addActionListener((ae) -> {
            if (!playState) {
                for (int r = 0; r < this.worldRows; ++r) {
                    for (int c = 0; c < this.worldColumns; ++c) {

                        Cell cell = world.getCell(r, c);

                        if (cell.getNewState()) {
                            cell.setOldState(false);

                            fillCell(Cell.deadCellColor, r, c);
                        }
                    }
                }

                canvas.repaint();
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle mouse clicks only when the world is stopped.
                if (!playState) {
                    Cell cell;

                    int x = e.getX();
                    int y = e.getY();

                    /*
                    Allows mouse action only on game board (canvas).
                     */
                    if (x > 0 && x < canvasWidth && y > 0 && y < canvasHeight) {
                        int column = x / cellWidth;
                        int row = y / cellHeight;

                        coordinates.setText("Coordinates: " + column + ", " + row + " ");
                        cell = world.getCell(row, column);

                        /*
                        If this is left mouse clicked then create life in the cell.
                         */
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            cell.setOldState(true);
                            fillCell(Cell.liveCellColor, row, column);

                        /*
                        If this is right mouse clicked then set the cell unpopulated.
                        */
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            cell.setOldState(false);
                            fillCell(Cell.deadCellColor, row, column);
                        }
                        canvas.repaint();
                    }
                }
            }
        });

        // WIP
        resizeWorld.addActionListener((ae) -> {
            int rows, columns;

            // Check for valid input for rows: (0, 100]
            if (InputChecker.checkInputInteger(rowsInput)) {
                rows = InputChecker.getValue();
            } else {
                return;
            }


            // Check for valid input for columns: (0, 100]
            if (InputChecker.checkInputInteger(columnsInput)) {
                columns = InputChecker.getValue();
            } else {
                return;
            }

            calculateGeometry(rows, columns);

            world = new World(rows, columns);

            frame.setSize(canvasWidth+GAP*2,canvasHeight+toolBarH+GAP);

            graphics.setColor(Color.lightGray);
            graphics.fillRect(0, 0, canvasWidth, canvasHeight);

            drawGrid();
            drawWorld();

            canvas.repaint();
        });

        randomButton.addActionListener((ae) -> {
            if (!playState) {
                Random random = new Random();
                int randomRow, randomColumn;

                int i = 2 * (worldRows + worldColumns);

                for (; i > 0; --i) {
                    randomRow = random.nextInt(worldRows);
                    randomColumn = random.nextInt(worldColumns);

                    Cell currentCell = world.getCell(randomRow, randomColumn);

                    if (!currentCell.getNewState()) {
                        currentCell.setOldState(true);
                        fillCell(Cell.liveCellColor, randomRow, randomColumn);
                    }
                }

                canvas.repaint();
            }
        });

        nextStep.addActionListener((ae) -> {
            if (!playState) {
                step();
            }
        });
    }

    public void initWorld() {

        frame.setVisible(true);

        imageWorld = canvas.createImage(canvasWidth, canvasHeight);
        graphics = imageWorld.getGraphics();

        // Start game with pause
        playState = false;

        drawGrid();
        drawWorld();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (playState) {
                    step();
                    try {
                        TimeUnit.SECONDS.sleep(discreteStep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 100);
    }

    /*
    Grouping together methods which run the world.
     */
    private void step() {
        world.changeState();
        drawWorld();
        canvas.repaint();
    }

    /*
    This method helps recalculate cell height and width if user picks
    new value for world rows and columns.
     */
    private void calculateGeometry(int rows, int columns) {
        worldRows = rows;
        worldColumns = columns;

        cellHeight = canvasHeight / worldRows;
        cellWidth = canvasWidth / worldColumns;
    }

    /*
    Method draws a grid based on cell height, width and world columns and rows.
     */
    private void drawGrid() {
        graphics.setColor(Color.BLACK);

        for (int r = 0; r <= worldRows; ++r) {
            // drawLine : (x1, y1), (x2, y2)
            graphics.drawLine(0, r * cellHeight, cellWidth*worldColumns, r * cellHeight);
        }

        for (int c = 0; c <= worldColumns; ++c) {
            graphics.drawLine(c * cellWidth, 0, c * cellWidth, cellHeight*worldRows);
        }
    }

    /*
    Call for a next discrete step for the world.
     */
    private void drawWorld() {
        for (int r = 0; r < worldRows; ++r) {
            for (int c = 0; c < worldColumns; ++c) {
                Cell cell = world.getCell(r, c);
                if (cell.isAlive()) {
                    fillCell(Cell.liveCellColor, r, c);
                } else {
                    fillCell(Cell.deadCellColor, r, c);
                }
            }
        }
        graphics.drawImage(imageWorld, 0, 0, null);
    }

    /*
    Simple wrapper for fill a cell with chosen color.
     */
    private void fillCell(Color color, int yCoordinate, int xCoordinate) {
        graphics.setColor(color);
        // fillRect : (x, y), (width, height)
        graphics.fillRect(xCoordinate * cellWidth + 1, yCoordinate * cellHeight + 1,
                cellWidth - 1, cellHeight - 1);
    }
}
