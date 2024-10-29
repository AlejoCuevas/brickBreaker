package brick;

import java.awt.*;
import java.util.HashMap;

public class Brick {
    private int x, y, width, height;
    private boolean esVisible;
    private boolean esIndestructible; // Indica si el ladrillo es indestructible
    private HashMap<Integer, Integer> impactosPorPelota; // Contador de impactos por cada pelota

    public Brick(int x, int y, int width, int height, boolean esIndestructible) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.esVisible = true;
        this.esIndestructible = esIndestructible;
        this.impactosPorPelota = new HashMap<>(); // Inicializa el mapa de impactos
    }

    public void draw(Graphics g) {
        if (esVisible) {
            g.setColor(esIndestructible ? Color.GRAY : Color.RED);
            g.fillRect(x, y, width, height);
        } else {
            System.out.println("Dibujar ladrillo oculto en: " + x + ", " + y);
        }
    }

    public void setVisible(boolean visible) {
        if (!esIndestructible) { // Solo se puede hacer visible si no es indestructible
            esVisible = visible;
        }
    }

    public boolean esVisible() {
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

    public boolean esRojo() {
        return esVisible && !esIndestructible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void impactar(int ballId) {
        if (esIndestructible) {
            impactosPorPelota.put(ballId, impactosPorPelota.getOrDefault(ballId, 0) + 1); // Incrementa el contador por pelota
            if (impactosPorPelota.get(ballId) >= 3) {
                esVisible = false; // Rompe el ladrillo después de 3 impactos por la misma pelota
                System.out.println("Ladrillo indestructible roto en: " + x + ", " + y);
            }
        }
    }
}
