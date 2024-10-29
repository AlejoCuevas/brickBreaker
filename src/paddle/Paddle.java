package paddle;

import java.awt.*;

public class Paddle {
    public static final int WIDTH = 100; // Ancho de la pala
    public static final int HEIGHT = 20; // Altura de la pala
    private int x, y;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void moverIzquierda() {
        x -= 20; // Ajusta la velocidad de movimiento
    }

    public void moverDerecha() {
        x += 20; // Ajusta la velocidad de movimiento
    }

    public void setX(int x) {
        this.x = x;
    }
}
