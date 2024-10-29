package powerup;

import java.awt.*;

public class PowerUp {
    private int x, y;
    private final int width = 20;
    private final int height = 20;
    private boolean esActivo;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
        this.esActivo = true;
    }

    public void moverAbajo() {
        y += 2; // Velocidad de caída
    }

    public void draw(Graphics g) {
        if (esActivo) {
            g.setColor(Color.GREEN); // Color del power-up
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void desactivar() {
        esActivo = false;
    }

    public boolean esActivo() {
        return esActivo;
    }
}
