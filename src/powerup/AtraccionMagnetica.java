package powerup;

import ball.Ball;
import java.awt.*;
import java.util.List;

public class AtraccionMagnetica extends PowerUp {

    public AtraccionMagnetica(int x, int y) {
        super(x, y);
    }

    @Override
    protected Color getColor() {
        return Color.CYAN; // Color para este power-up
    }

    @Override
    public void activar() {

    }

    // Método activador que recibe una lista de pelotas
    public void activar(List<Ball> balls) {
        System.out.println("Activando Atracción Magnética.");

        // Crear una nueva pelota y posicionarla en la parte inferior central
        Ball nuevaPelota = new Ball(x, y);
        nuevaPelota.setX(x); // Posición horizontal en el centro
        nuevaPelota.setY(y); // Posición en la parte inferior
        nuevaPelota.setyDir(-2); // Direcciona hacia arriba

        balls.add(nuevaPelota); // Agregar la pelota a la lista
    }
}
