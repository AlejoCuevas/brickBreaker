package powerup;

import java.awt.*;

public abstract class PowerUp {
    protected int x, y;
    protected final int width = 20;
    protected final int height = 20;
    protected boolean esActivo;

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
            g.setColor(getColor()); // Color específico del power-up
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

    protected abstract Color getColor(); // Método para obtener el color del power-up

    public abstract void activar(); // Método para activar el efecto del power-up
}