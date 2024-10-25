package paddle;

import java.awt.*;

public class Paddle {

    private int x, y;
    private final int width = 200;
    private final int height = 20;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moverIzquierda() {
        if (x > 0) {
            x -= 20; // Mueve 20 píxeles hacia la izquierda
        }
    }

    public void moverDerecha() {
        if (x < Toolkit.getDefaultToolkit().getScreenSize().width - width) {
            x += 20; // Mueve 20 píxeles hacia la derecha
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
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
