package powerup;

import java.awt.*;

public class FakePowerUp extends PowerUp {


    public FakePowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    protected Color getColor() {
        return Color.ORANGE; // Color para este power-up
    }

    @Override
    public void activar() {
        // Lógica para activar el cañón de rebote
        System.out.println("Cañón de Rebote activado.");
    }

}
