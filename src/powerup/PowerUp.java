package powerup;

import java.awt.*;
import java.util.Random;

public class PowerUp {

    private int x, y;
    private final int width = 30;
    private final int height = 30;
    private int ySpeed = 2;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void mover() {
        y += ySpeed; // Mueve el power-up hacia abajo
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }
}
