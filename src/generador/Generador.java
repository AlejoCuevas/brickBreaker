package generador;

import brick.Brick;
import java.awt.*;
import java.util.Random;

public class Generador {

    private Brick[][] mapa;
    private int fila, columna;
    int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

    public Generador(int nivel) {
        // Ajustar el número de filas y columnas según el nivel
        if (nivel == 3 || nivel == 4) {
            this.fila = (int) (screenWidth / 60) / 2; // Alcanzar la mitad de la pantalla
        } else if (nivel == 5) { // Nivel bonus
            this.fila = 7;
        } else {
            this.fila = 5 + nivel;
        }
        this.columna = screenWidth / 60;
        mapa = new Brick[fila][columna];
        Random random = new Random();

        // Generación de bloques
        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
                boolean esIndestructible;
                if (nivel == 5) { // Nivel bonus
                    esIndestructible = false; // No hay ladrillos indestructibles en el nivel bonus
                } else {
                    esIndestructible = (i == fila - 1) || (random.nextDouble() < 0.50); // Probabilidad de ser indestructible
                }

                // Se generan bloques aleatorios
                if (random.nextBoolean()) { // 50% de probabilidad de generar un bloque
                    mapa[i][j] = new Brick(j * 60 + 10, i * 30 + 50, 60, 30, esIndestructible);
                } else {
                    mapa[i][j] = null;
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

    public boolean noQuedanBloquesRojos() {
        for (Brick[] row : mapa) {
            for (Brick brick : row) {
                if (brick != null && brick.esRojo()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Brick[][] getMapa() {
        return mapa;
    }
}
