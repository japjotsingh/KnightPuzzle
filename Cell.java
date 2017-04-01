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

    public void setMoved() {
        URL url = Cell.class.getResource("img/moved.jpg");
        try {
            image = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, CELL_WIDTH, CELL_HEIGHT, null);
    }
}

