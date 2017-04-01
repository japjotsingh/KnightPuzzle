/**
 * Created by lawzoom on 3/23/17.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

/*
 * This class will be the display and will get the starting position
 * of the knight using a mousePress.  It should also have the data
 * like the 2D array and the current location of the knight.  The
 * paintComponent method should redraw the view from the beginning, as it
 * always should.
 */

public class KnightsTourPanel extends JPanel {

    // what private data is needed?
    private Cell[][] board;
    private int[][] values;
    private Knight knight;
    public static final int MOVED = 100000;
    JButton randomMove, randCont, thoughtfulMove, thoughtCont;

    public KnightsTourPanel(int w, int h) {
        this.setPreferredSize(new Dimension(w,h));
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
        randomMove.setText("Random Move");
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
        thoughtfulMove.setText("Thoughful Move");
        thoughtfulMove.setBounds(220, 550, 175, 45);
        thoughtfulMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startThoughtfulMove();
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
                int row = mouseEvent.getX()/Cell.CELL_HEIGHT;
                int col = mouseEvent.getY()/Cell.CELL_WIDTH;
                System.out.println("Row: " + row + "\tCol: " + col);
            }
        });
    }

    public void startRandomMove() {
        new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                makeRandomMove();
            }
        }).start();
    }

    public void startThoughtfulMove() {
        new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                makeThoughtfulMove();
            }
        }).start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // stuff to draw the board and knight
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                //System.out.println("R: " + r + " C: " + c);
                board[r][c].draw(g);
            }
        }
        knight.draw(g);
    }
    /* make random move just selects a new location at random
     * if the knight is trapped (no new locations to move to)
     * then false is returned.  Otherwise, true is returned.
     * The knight's location should be updated and the
     */
    public boolean makeRandomMove() {
        int[] rows = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] cols = {-1, 1, -2, 2, -2, 2, -1, 1};
        int rand = (int)(Math.random()*8);
        while (knight.getRow() - rows[rand] < 0 || knight.getRow() - rows[rand] >= board.length ||
                knight.getCol() - cols[rand] < 0 || knight.getRow() - cols[rand] >= board[0].length) {
            rand = (int)(Math.random()*8);
        }

        knight.move(knight.getRow()-rows[rand], knight.getCol()-cols[rand]);
        board[knight.getRow()][knight.getCol()].setMoved();
        System.out.println("R: " + knight.getRow() + "\tC: " + knight.getCol());
        repaint();
        return false;
    }
    /* make a move to a new location that ensures the best chance
     * for a complete traversal of the board.
     * if the knight is trapped (no new locations to move to)
     * then false is returned.  Otherwise, true is returned.
     */
    public boolean makeThoughtfulMove() {
        int[] rows = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] cols = {-1, 1, -2, 2, -2, 2, -1, 1};
        int min = Integer.MAX_VALUE;
        int bestIdx = -1;
        for (int i = 0; i < rows.length; i++) {
            if (knight.getRow()-rows[i] >= 0 && knight.getRow()-rows[i] < board.length &&
                    knight.getCol()-cols[i] >= 0 && knight.getCol()-cols[i] < board[0].length) {
                int cost = values[knight.getRow() - rows[i]][knight.getCol() - cols[i]];
                values[knight.getRow() - rows[i]][knight.getCol() - cols[i]] -= 1;
                if (cost < min && cost != MOVED) {
                    min = cost;
                    bestIdx = i;
                }
            }
        }

        if (bestIdx != -1) {
            knight.move(knight.getRow()-rows[bestIdx], knight.getCol()-cols[bestIdx]);
            board[knight.getRow()][knight.getCol()].setMoved();
            values[knight.getRow()][knight.getCol()] = MOVED;
        } else {
            System.out.println("No more possible moves!");
        }

        repaint();
        return false;
    }


}

