/**
 * Created by lawzoom on 3/23/17.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

public class Knight {

    private int row, col;
    private int x, y;
    private Image image;
    private int CELL_WIDTH = 50;
    private int CELL_HEIGHT = 50;
    private int movei;

    Knight(int row, int col) {
        this.row = row;
        this.col = col;
        x = col * CELL_WIDTH;
        y = row * CELL_HEIGHT;
        getImage();
        movei = 0;
    }

    private void getImage() {
        URL url = Knight.class.getResource("img/knight.jpg");
        try {
            image = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return this.col;
    }

    public int getY() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public void move(int row, int col, Cell board[][]) {
        this.row = row;
        this.col = col;
        x = col * CELL_WIDTH;
        y = row * CELL_HEIGHT;
        board[row][col].setMoved();
        movei++;
    }

    public int getMovei(){
        return movei;
    }

    public void setMovei(int i){
        movei = i;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, CELL_WIDTH, CELL_HEIGHT, null);
    }
}

