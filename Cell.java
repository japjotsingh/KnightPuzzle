/**
 * Created by lawzoom on 3/23/17.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

public class Cell {

    private int row, col;
    private int x, y;
    private Image image;
    public static int CELL_WIDTH = 50;
    public static int CELL_HEIGHT = 50;
    boolean wasVisited = false;
    public int countVisited;


    Cell(int row, int col) {
        row = row;
        col = col;
        x = col * CELL_WIDTH;
        y = row * CELL_HEIGHT;
        getImage();
    }

    private void getImage() {
        URL url = Cell.class.getResource("img/cell.png");
        try {
            image = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCountVisited(int i){
        countVisited = i;
    }


    public void setMoved() {
        URL url = Cell.class.getResource("img/moved.jpg");
        wasVisited = true;
        try {
            image = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUnMoved() {
        URL url = Cell.class.getResource("img/cell.png");
        wasVisited = false;
        try {
            image = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean wasVisited() {
        return wasVisited;
    }

    public void setWasVisited(boolean a){
        wasVisited = a;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, CELL_WIDTH, CELL_HEIGHT, null);
        if(countVisited>0 || wasVisited)
         g.drawString(Integer.toString(countVisited), x+20, y+30);
    }
}

