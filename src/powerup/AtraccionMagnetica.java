package powerup;

import java.awt.*;

public class AtraccionMagnetica extends PowerUp {

    public AtraccionMagnetica(int x, int y) {
        super(x, y);
    }


    protected Color getColor() {
        return Color.CYAN; // Color para este power-up
    }


    public void activar() {
        // Lógica para activar la atracción magnética
        System.out.println("Activando Atracción Magnética.");
        // Aquí puedes modificar la lógica de movimiento de la bola
    }

}