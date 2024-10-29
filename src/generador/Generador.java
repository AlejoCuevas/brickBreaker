package generador;

import brick.Brick;

import java.awt.*;

public class Generador {

    private Brick[][] mapa;
    private int fila, columna;

    int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

    public Generador(int fila) {
        this.fila = fila;
        this.columna = screenWidth / 60;
        mapa = new Brick[fila][columna];
        int mitad = columna / 2;

        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
                // La última fila es indestructible y no tendrá bloques en el centro y en los laterales
                if (i == fila - 1 && (j == mitad - 1 || j == mitad - 2 || j == mitad)) {
                    mapa[i][j] = null; // Sin bloque en el centro y los adyacentes
                } else {
                    boolean esIndestructible = (i == fila - 1); // La última fila es indestructible
                    mapa[i][j] = new Brick(j * 60 + 10, i * 30 + 50, 60, 30, esIndestructible);
                }
            }
        }

    }

    public void draw(Graphics g) {
        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
                if (mapa[i][j] != null && mapa[i][j].esVisible()) {
                    mapa[i][j].draw(g);
                }
            }
        }
    }

    public Brick[][] getMapa() {
        return mapa;
    }
}