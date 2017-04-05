/**
 * Created by lawzoom on 3/23/17.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.lang.reflect.Array;
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
    private Knight knight;
    JButton randomMove, randCont, thoughtfulMove, thoughtCont;
    private boolean clicked = true;
    int N = 8;
    int N2 = 8;

    public KnightsTourPanel(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.green);
        addMouseListener();
        setupBoard();
        setupKnight(0,0);
        repaint();
    }

    private void setupBoard() {
        //make changeable size
        board = new Cell[N][N2];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = new Cell(r, c);
            }
        }
        setupButtons();
    }

    public void setupButtons() {
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
                makeThoughtfulMove();
                startThoughtfulMove();
            }
        });
        this.setLayout(null);
        add(thoughtfulMove);
        thoughtfulMove.setVisible(true);
    }

    private void setupKnight(int r, int c) {
        //make based off mouse click
        knight = new Knight(r, c);
        board[r][c].setMoved();
        board[r][c].setCountVisited(knight.getMovei());
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if(board[x][y] != board[r][c]){
                    board[x][y].setWasVisited(false);
                    board[x][y].setUnMoved();
                }
            }
        }
    }

    // add the mouse listener.  This will only work for the
    // first click, and then after the first click, there should
    // be no more mouse listening!
    private void addMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(clicked) {
                    super.mouseClicked(mouseEvent);
                    int col = mouseEvent.getX() / Cell.CELL_HEIGHT;
                    int row = mouseEvent.getY() / Cell.CELL_WIDTH;
                    System.out.println("Row: " + row + "\tCol: " + col);
                    setupKnight(row, col);
                    clicked = !clicked;
                    repaint();
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                //System.out.println("R: " + r + " C: " + c);
                board[r][c].draw(g);
            }
        }
        knight.draw(g);
    }

    public void startRandomMove() {
        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                makeRandomMove();
            }
        }).start();
    }

    int bazinga = 0;

    public void startThoughtfulMove() {
        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                smartMove();
            }
        }).start();
    }

    private void smartMove() {
        knight.move(rLocs.get(knight.getMovei()), cLocs.get(knight.getMovei()), board);
        board[knight.getRow()][knight.getCol()].setCountVisited(knight.getMovei());
        repaint();
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

//            if (count > 100) {
//                int result = JOptionPane.showConfirmDialog(null, "Random Move has run out of places to go!" + "\n Reset Board?");
//                return false;
//            }
        }

        knight.move(newRow, newCol, board);
        board[knight.getRow()][knight.getCol()].setCountVisited(knight.getMovei());
//        System.out.println("Knight Moved to: " + "[ " + knight.getRow() + " " + knight.getCol() + " ]");
        repaint();
        return false;
    }

    /* make a move to a new location that ensures the best chance
     * for a complete traversal of the board.
     * if the knight is trapped (no new locations to move to)
     * then false is returned.  Otherwise, true is returned.
     */

    public boolean isFair(int x, int y, int sol[][]) {
        return (x >= 0 && x < N && y >= 0 && y < N && sol[x][y] == -1);
    }

    private boolean solveTour(int x, int y, int movei, int sol[][], int[] xMoves, int[] yMoves) {
        int k, nextX, nextY;


        if (movei == N * N2) {
            return true;
        }

        for (int g = 0; g < 8; g++) {
            nextX = x + xMoves[g];
            nextY = y + yMoves[g];
            if (isFair(nextX, nextY, sol)) {
                sol[nextX][nextY] = movei;
                if (solveTour(nextX, nextY, movei + 1, sol, xMoves, yMoves)) {
                    return true;
                } else {
                    sol[nextX][nextY] = -1;
                }
            }
        }
        return false;
    }


    public boolean makeThoughtfulMove() {

        int sol[][] = new int[N][N2];

        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N2; y++) {
                sol[x][y] = -1;
            }
        }

        int xMoves[] = {2, 1, -1, -2, -2, -1, 1, 2};
        int yMoves[] = {1, 2, 2, 1, -1, -2, -2, -1};

        sol[knight.getRow()][knight.getCol()] = 0;

        if (!solveTour(knight.getX(), knight.getY(), 1, sol, xMoves, yMoves)) {
            System.out.println("No Solution");
            return false;
        } else {
            printSol(sol);
        }
        return true;
    }

    int countraz = 1;

    ArrayList<Integer> rLocs = new ArrayList<Integer>();
    ArrayList<Integer> cLocs = new ArrayList<Integer>();

    public void printSol(int[][] sol) {
        for (int i = 0; i < sol.length; i++) {
            for (int j = 0; j < sol[0].length; j++) {
                System.out.print(sol[i][j] + " ");
            }
            System.out.println();
        }

        while (countraz < 64) {
            for (int y = 0; y < sol.length; y++) {
                for (int x = 0; x < sol[0].length; x++) {
                    if (sol[y][x] == countraz) {
                        cLocs.add(x);
                        rLocs.add(y);
                        countraz++;
                    }
                }
            }
        }
    }

}

