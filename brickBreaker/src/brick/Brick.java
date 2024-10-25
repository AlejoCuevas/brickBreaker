package brick;

import java.awt.Color;
import java.awt.Graphics;

public class Brick {

    private int x, y, width, height;
    private boolean esVisible;
    private boolean esIndestructible; // Indica si el ladrillo es indestructible

    public Brick(int x, int y, int width, int height, boolean esIndestructible) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.esVisible = true;
        this.esIndestructible = esIndestructible;
    }

    public void draw(Graphics g) {
        if (esVisible) {
            g.setColor(esIndestructible ? Color.GRAY : Color.RED); // Color gris si es indestructible
            g.fillRect(x, y, width, height);
        }
    }

    public void setVisible(boolean visible) {
        if (!esIndestructible) { // Solo se puede hacer visible si no es indestructible
            esVisible = visible;
        }
    }

    public boolean isVisible() {
        return esVisible;
    }

    public boolean esIndestructible() {
        return esIndestructible;
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
