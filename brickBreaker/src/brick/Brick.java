package brick;

import java.awt.Color;
import java.awt.Graphics;


public class Brick {

    private int x, y, width, height;
    private boolean esVisible;

    public Brick(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.esVisible = true;

    }

    public void draw(Graphics g) {
        if(esVisible) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public void setVisible(boolean visible) {

        esVisible = visible;
    }

    public boolean isVisible() {

        return esVisible;

    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
}
