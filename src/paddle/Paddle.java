package paddle;

import java.awt.Color;
import java.awt.Graphics;


public class Paddle {

    private int x, y;
    private final int width = 100;
    private final int height = 10;

    public Paddle(int x, int y) {

        this.x = x;
        this.y = y;

    }

    public void moverIzquierda() {

        if (x>0) {

            x -= 20;
        }

    }

    public void moverDerecha() {
        if (x< 600 - width) {

            x += 20;
        }

    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
