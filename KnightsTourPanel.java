/**
 * Created by lawzoom on 3/23/17.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/*
 * This class will be the display and will get the starting position
 * of the knight using a mousePress.  It should also have the data
 * like the 2D array and the current location of the knight.  The
 * paintComponent method should redraw the view from the beginning, as it
 * always should.
 */

//***add a reset board button

public class KnightsTourPanel extends JPanel {

    // what private data is needed?
    private Cell[][] board;
    private int[][] values;
    private Knight knight;
    public static final int MOVED = 100000;
    JButton randomMove, randCont, thoughtfulMove, thoughtCont;
    private int counts = 1;

    public KnightsTourPanel(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.green);
        addMouseListener();
        setupBoard();
        setupKnight();

//        startRandomMove();
        //startThoughtfulMove();
    }

    private void setupBoard() {
        board = new Cell[8][8];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = new Cell(r, c);
            }
        }

        values = new int[8][8];

        randomMove = new JButton();
        randomMove.setText("Start Random Moves");
        randomMove.setBounds(20, 550, 175, 45);
        randomMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRandomMove();
            }
        });
        this.setLayout(null);
        add(randomMove);
        randomMove.setVisible(true);

        thoughtfulMove = new JButton();
        thoughtfulMove.setText("Start Thoughful Moves");
        thoughtfulMove.setBounds(220, 550, 175, 45);
        thoughtfulMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("making thoughtful Moves");
                makeThoughtfulMove(0);
//                startThoughtfulMove();
            }
        });
        this.setLayout(null);
        add(thoughtfulMove);
        thoughtfulMove.setVisible(true);

    }

    private int calculatePossibilities(int r, int c) {
        int[] rows = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] cols = {-1, 1, -2, 2, -2, 2, -1, 1};
        int num = 0;

        for (int i = 0; i < rows.length; i++) {
            if (r - rows[i] >= 0 && r - rows[i] < board.length &&
                    c - cols[i] >= 0 && c - cols[i] < board[0].length) {
                num += 1;
            }
        }
        return num;
    }

    private void setupKnight() {
        int r = 0;
        int c = 0;
        knight = new Knight(r, c);
        board[r][c].setMoved();
        board[r][c].setCountVisited(counts);
        values[r][c] = MOVED;
    }

    // add the mouse listener.  This will only work for the
    // first click, and then after the first click, there should
    // be no more mouse listening!
    private void addMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                int col = mouseEvent.getX() / Cell.CELL_HEIGHT;
                int row = mouseEvent.getY() / Cell.CELL_WIDTH;
                System.out.println("Row: " + row + "\tCol: " + col);
            }
        });
    }

    public void startRandomMove() {
        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                makeRandomMove();
            }
        }).start();
    }

    public void startThoughtfulMove() {
        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                makeThoughtfulMove(0);
            }
        }).start();
    }

    public void paintComponent(Graphics g) {
//        counts++;
        String no = Integer.toString(counts);
        super.paintComponent(g);
        // stuff to draw the board and knight
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                //System.out.println("R: " + r + " C: " + c);
                board[r][c].draw(g);
            }
        }
        knight.draw(g);
//        g.drawString(no, knight.getCol()*50, knight.getRow()*50);


    }

    /* make random move just selects a new location at random
     * if the knight is trapped (no new locations to move to)
     * then false is returned.  Otherwise, true is returned.
     * The knight's location should be updated and the
     */

    public boolean makeRandomMove() {

        int[] rows = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] cols = {-1, 1, -2, 2, -2, 2, -1, 1};

        int rand = (int) (Math.random() * 8);

        int newRow = knight.getRow() - rows[rand];
        int newCol = knight.getCol() - cols[rand];

        int count = 0;

        while (newRow < 0 || newRow >= board.length ||
                newCol < 0 || newCol >= board[0].length || board[newRow][newCol].wasVisited) {
            rand = (int) (Math.random() * 8);
            newRow = knight.getRow() - rows[rand];
            newCol = knight.getCol() - cols[rand];
            count++;

            if (count > 100) {
                int result = JOptionPane.showConfirmDialog(null, "Random Move has run out of places to go!" + "\n Reset Board?");
                if (result == JOptionPane.YES_OPTION) {
                    System.out.println("Clear Screen");
                    return false;
                } else {
                    System.out.println("leave board as is close message");
                }
                return false;
            }
        }


//        System.out.println(rand);
//        System.out.println("KR: " + knight.getRow() + " KC: " + knight.getCol());
//        System.out.println("NR: " + newRow + " NC: " + newCol );

        knight.move(newRow, newCol);
        board[knight.getRow()][knight.getCol()].setMoved();
        counts++;
        board[knight.getRow()][knight.getCol()].setCountVisited(counts);
        System.out.println("Knight Moved to: " + "[ " + knight.getRow() + " " + knight.getCol() + " ]");
        repaint();


//        if(knight.getRow()>0 && knight.getRow()<=board.length && knight.getCol()>0 && knight.getCol()<board[0].length) {
//            knight.move(rows[rand], cols[rand]);
//            board[knight.getRow()][knight.getCol()].setMoved();
//            System.out.println("R: " + knight.getRow() + "C: " + knight.getCol());
//            repaint();
//        }

        return false;
    }

    /* make a move to a new location that ensures the best chance
     * for a complete traversal of the board.
     * if the knight is trapped (no new locations to move to)
     * then false is returned.  Otherwise, true is returned.
     */

    public boolean checkWorks(int option) {
        int[] rows = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] cols = {-1, 1, -2, 2, -2, 2, -1, 1};
        int newRow = knight.getRow() - rows[option];
        int newCol = knight.getCol() - cols[option];

        int count = 0;

        while (newRow < 0 || newRow >= board.length ||
                newCol < 0 || newCol >= board[0].length || board[newRow][newCol].wasVisited) {
            option++;
            newRow = knight.getRow() - rows[option];
            newCol = knight.getCol() - cols[option];
            count++;

            if(count >100){
                return false;
            }
        }

        return true;

    }

    public boolean makeThoughtfulMove(int option) {

        //0-7
        int[] rows = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] cols = {-1, 1, -2, 2, -2, 2, -1, 1};

        System.out.println(checkWorks(5));

//        while (checkWorks(option)) {
//            System.out.println("checkWorks");
//            System.out.println(rows[option] + " " + cols[option]);
//            System.out.println(knight.getRow() + " " + knight.getCol());
//            knight.move(rows[option], cols[option]);
//            System.out.println(knight.getRow() + " " + knight.getCol());
//
//            checkWorks(option);
//        }
//
//        if (!checkWorks(option)) {
//            option++;
//            makeThoughtfulMove(option);
//        } else {
//            System.out.println("something failed");
//        }

        // below is what David and Rishab worked on in class when I was absent, it worked for all places but disregarded the restriction of
        //only going to each spot once, the code for going to each spot only once is above^^
//        int min = Integer.MAX_VALUE;
//        int bestIdx = -1;
//        for (int i = 0; i < rows.length; i++) {
//            if (knight.getRow() - rows[i] >= 0 && knight.getRow() - rows[i] < board.length &&
//                    knight.getCol() - cols[i] >= 0 && knight.getCol() - cols[i] < board[0].length) {
//                int cost = values[knight.getRow() - rows[i]][knight.getCol() - cols[i]];
//                values[knight.getRow() - rows[i]][knight.getCol() - cols[i]] -= 1;
//                if (cost < min && cost != MOVED) {
//                    min = cost;
//                    bestIdx = i;
//                }
//            }
//        }
//
//        if (bestIdx != -1) {
//            knight.move(knight.getRow() - rows[bestIdx], knight.getCol() - cols[bestIdx]);
//            board[knight.getRow()][knight.getCol()].setMoved();
//            values[knight.getRow()][knight.getCol()] = MOVED;
//        } else {
//            System.out.println("No more possible moves!");
//        }
//
//        repaint();
        return false;
    }


}

