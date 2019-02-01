package com.vertextau.gol;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JToolBar;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class EntryWindow implements ActionListener {

    private class MenuImgLoader extends Component {
        private BufferedImage mainMenuImage = null;

        MenuImgLoader() {
            try {
                mainMenuImage = ImageIO.read(new File("resources/images/MainMenuImage.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void paint(Graphics g) {
            g.drawImage(mainMenuImage, 0, 0, null);
        }

        public Dimension getPreferredSize() {
            if (mainMenuImage == null) {
                return new Dimension(100,100);
            } else {
                return new Dimension(mainMenuImage.getWidth(null), mainMenuImage.getHeight(null));
            }
        }
    }

    private JFrame entryFrame;

    private JFormattedTextField rowsInput, columnsInput;

    public EntryWindow() {
        entryFrame = new JFrame("Conway's Game of Life: Menu");
        entryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        entryFrame.setResizable(false);
        entryFrame.setSize(320, 350);
        entryFrame.setLocationRelativeTo(null);
        entryFrame.setLayout(new BorderLayout());

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(this);
        startButton.setActionCommand("OpenGameBorder");

        JToolBar entryToolBar = new JToolBar();
        entryToolBar.setOrientation(JToolBar.HORIZONTAL);
        entryToolBar.setFloatable(false);

        final JLabel rowsLabel = new JLabel("Rows: ");
        final JLabel columnsLabel = new JLabel("Columns: ");

        rowsInput = new JFormattedTextField();
        rowsInput.setValue(50);
        columnsInput = new JFormattedTextField();
        columnsInput.setValue(50);

        entryFrame.add(new MenuImgLoader(), BorderLayout.NORTH);

        entryFrame.add(entryToolBar, BorderLayout.SOUTH);
        entryToolBar.add(startButton);
        entryToolBar.add(rowsLabel);
        entryToolBar.add(rowsInput);
        entryToolBar.add(columnsLabel);
        entryToolBar.add(columnsInput);
        entryFrame.pack();
        entryFrame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        int rows, columns;

        if (command.equals("OpenGameBorder")) {
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

            entryFrame.dispose();

            SwingUtilities.invokeLater(() -> {
                new UI(1, rows, columns).initWorld();
            });
        }
    }
}
