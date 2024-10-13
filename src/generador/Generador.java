package generador;


import brick.Brick;

import java.awt.*;

public class Generador {

    private Brick[][] mapa;
    private int fila, columna;

    public Generador(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        mapa = new Brick[fila][columna];
        for(int i = 0; i < fila; i++) {
            for(int j = 0; j < columna; j++) {
                mapa[i][j] = new Brick(j * 60 + 50, i * 30 + 50, 60, 30);
            }

        }
    }

   public void draw(Graphics g) {
        for(int i = 0; i < fila; i++) {
            for(int j = 0; j < columna; j++) {
                if(mapa[i][j].isVisible()) {
                    mapa[i][j].draw(g);
                }
            }
        }

   }

   public Brick[][] getMapa() {
        return mapa;
   }
}
